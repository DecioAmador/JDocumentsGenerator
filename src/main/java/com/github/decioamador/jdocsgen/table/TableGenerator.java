package com.github.decioamador.jdocsgen.table;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import com.github.decioamador.jdocsgen.translation.Translator;
import com.github.decioamador.jdocsgen.utils.FieldResolution;

/**
 * This class has the purpose generating tables
 */
public class TableGenerator implements AutoCloseable {

    private final Workbook workbook;

    /**
     * Contructor of TableGenerator
     *
     * @param workbook
     *            Workbook that will be used
     */
    public TableGenerator(final Workbook workbook) {
        this.workbook = Objects.requireNonNull(workbook);
    }

    /**
     * It generates the document base on the arguments
     *
     * @param sheetName
     *            The name of the sheet
     * @param options
     *            Options to generate this table
     * @param objs
     *            Objects that will be the lines
     * @param titles
     *            The title of the columns
     * @param fields
     *            The path of the field
     * @param translator
     *            A translator being used
     * @return the sheet being used
     */
    public Sheet generateTable(final String sheetName, final TableOptions options, final Collection<?> objs,
            final List<String> titles, final List<String> fields, final Translator translator) {

        final Sheet sheet = workbook.createSheet(sheetName);
        generateTable(sheet, options, objs, titles, fields, translator);
        return sheet;
    }

    /**
     * It generates the document base on the arguments
     *
     * @param sheet
     *            Sheet to put the table
     * @param options
     *            Options to generate this table
     * @param objs
     *            Objects that will be the lines
     * @param titles
     *            The title of the columns
     * @param fields
     *            The path of the field
     * @param translator
     *            A translator being used
     */
    public void generateTable(final Sheet sheet, final TableOptions options, final Collection<?> objs,
            final List<String> titles, final List<String> fields, final Translator translator) {

        int rowNum = options.getInitPosRow(), columnNum = options.getInitPosCol();
        final int firstLineField = options.getInitPosRow() + 1;
        final boolean agg = options.isAggregate();
        String translated;
        boolean hasStyle;
        Object o;

        CellStyle fieldStyle;
        Cell cell;
        Row row;

        row = sheet.createRow(rowNum++);
        generateTitles(row, options, titles);

        hasStyle = options.getFieldsStyle() != null;
        for (final Object obj : objs) {
            if (obj != null) {
                columnNum = options.getInitPosCol();
                row = sheet.createRow(rowNum);
                for (final String field : fields) {
                    if (agg) {
                        o = FieldResolution.resolveFieldAggregation(obj, field);
                    } else {
                        o = FieldResolution.resolveField(obj, field);
                    }

                    cell = row.createCell(columnNum++);

                    if (o != null) {
                        translated = translateObject(options, translator, o, field);

                        if (translated != null) {
                            cell.setCellValue(translated);
                        }
                    }

                    if (hasStyle) {
                        fieldStyle = options.getFieldsStyle();

                        // Prevail the title style
                        if (rowNum == firstLineField && options.getTitlesStyle() != null) {
                            fieldStyle = prevailTitlesStyle(options);
                        }
                        cell.setCellStyle(fieldStyle);
                    }
                }
                rowNum++;
            }
        }

        autosizeColumns(sheet, options, titles.size());
    }

    /**
     * Prevail bottom border titles<br>
     * Most templates do this
     * 
     * @param options
     *            Options being used
     * @param fieldStyle
     *            style being used on fields
     * @return Style for the first line in the fields
     */
    private CellStyle prevailTitlesStyle(final TableOptions options) {
        final CellStyle result = workbook.createCellStyle();
        result.cloneStyleFrom(options.getFieldsStyle());
        result.setBorderTop(options.getTitlesStyle().getBorderBottomEnum());
        result.setTopBorderColor(options.getTitlesStyle().getBottomBorderColor());
        return result;
    }

    /**
     * Translate an object to a {@link String}
     *
     * @param options
     *            Options being used
     * @param translator
     *            translator used to resolve fields
     * @param agg
     *            wherever you want to aggregate fields
     * @param o
     *            object that you want to resolve
     * @param field
     *            field used
     * @return the translated value of an object
     */
    private String translateObject(final TableOptions options, final Translator translator, final Object o,
            final String field) {
        Object temp = o;
        String translated;
        if (options.isAggregate() && (temp instanceof Collection || temp.getClass().isArray())) {
            if (o instanceof Collection) {
                temp = ((Collection<?>) o).toArray();
            }

            translated = translator.getValue((Object[]) temp, field, options.getSeperatorAgg());
        } else {
            translated = translator.getValue(temp, field);
        }
        return translated;
    }

    /**
     * Generates the titles
     *
     * @param row
     *            the row that you want to put the titles
     * @param options
     *            options being used
     * @param titles
     *            titles to put on the row
     */
    private void generateTitles(final Row row, final TableOptions options, final List<String> titles) {
        final boolean hasStyle = options.getTitlesStyle() != null;
        int columnNum = options.getInitPosCol();
        Cell cell;

        for (final String title : titles) {
            cell = row.createCell(columnNum++);
            cell.setCellValue(title);
            if (hasStyle) {
                cell.setCellStyle(options.getTitlesStyle());
            }
        }
    }

    /**
     * Autosize the columns
     *
     * @param sheet
     *            sheet being used
     * @param options
     *            options being used
     * @param titlesSize
     *            size of the titles
     */
    private void autosizeColumns(final Sheet sheet, final TableOptions options, final int titlesSize) {
        if (options.isAutosize()) {
            final boolean track = sheet instanceof SXSSFSheet;
            for (int i = options.getInitPosCol(); i < options.getInitPosCol() + titlesSize; i++) {
                if (track) {
                    ((SXSSFSheet) sheet).trackColumnForAutoSizing(i);
                }
                sheet.autoSizeColumn(i);
            }
        }
    }

    /**
     * Writes the generated document on the stream
     *
     * @return ByteArrayInputStream it was the content of the file generated
     * @throws IOException
     *             If an I/O error occurs. In particular, an IOException may be
     *             thrown if the output stream has been closed.
     */
    public ByteArrayInputStream write() throws IOException {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        workbook.write(bos);
        return new ByteArrayInputStream(bos.toByteArray());
    }

    /**
     * Writes the generated document on the stream, writing on the existing one
     *
     * @param outputStream
     *            OutputStream that will have the content
     * @throws IOException
     *             If an I/O error occurs. In particular, an IOException may be
     *             thrown if the output stream has been closed.
     */
    public void write(final OutputStream outputStream) throws IOException {
        workbook.write(outputStream);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        workbook.close();
    }

    /**
     * Workbook that is being used by the {@link TableGenerator}
     * 
     * @return workbook being used
     */
    public Workbook getWorkbook() {
        return workbook;
    }

}