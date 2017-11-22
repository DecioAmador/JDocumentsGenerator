package com.github.decioamador.jdocsgen.table;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Objects;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import com.github.decioamador.jdocsgen.translation.Translator;
import com.github.decioamador.jdocsgen.translation.TranslatorUtils;
import com.github.decioamador.jdocsgen.utils.FieldResolution;

/**
 * This class has the purpose generating table based documents
 */
public class TableGenerator implements AutoCloseable {

    private final Workbook workbook;

    /**
     * Creates a {@linkplain TableGenerator} by using a specified instance of a
     * {@link Workbook}
     *
     * @param workbook
     *            High level representation of a Excel workbook.
     */
    public TableGenerator(final Workbook workbook) {
        this.workbook = Objects.requireNonNull(workbook);
    }

    /**
     * Create a new {@linkplain Sheet} for the wrapped {@linkplain Workbook}.
     * Generates a table that the objects will be represented in each line and the
     * titles will be put in the line above. Return the sheet created.
     *
     * @param sheetName
     *            The name to set for the sheet.
     * @param options
     *            Options to generate this table ({@link TableOptions})
     * @param objs
     *            Objects that will represent the lines of the table
     * @param titles
     *            An array that each element is the title for a column
     * @param fields
     *            An array that each element is an EL path for a field
     * @param translator
     *            Gives the user an opportunity to change the value of a field by
     *            another (internationalization, format currency, format dates)
     * @return A {@linkplain Sheet} that contains the information in table
     */
    public Sheet generateTable(final String sheetName, final TableOptions options, final Collection<?> objs,
            final String[] titles, final String[] fields, final Translator translator) {

        final Sheet sheet = workbook.createSheet(sheetName);
        generateTable(sheet, options, objs, titles, fields, translator);
        return sheet;
    }

    /**
     * Generates a table that the objects will be represented in each line and the
     * titles will be put in the line above.
     *
     * @param sheet
     *            A {@linkplain Sheet} that should be created from the wrapped
     *            {@linkplain Workbook}
     * @param options
     *            Options to generate this table ({@link TableOptions})
     * @param objs
     *            Objects that will represent the lines of the table
     * @param titles
     *            An array that each element is the title for a column
     * @param fields
     *            An array that each element is an EL path for a field
     * @param translator
     *            Gives the user an opportunity to change the value of a field by
     *            another (internationalization, format currency, format dates)
     */
    public void generateTable(final Sheet sheet, final TableOptions options, final Collection<?> objs,
            final String[] titles, final String[] fields, final Translator translator) {

        final boolean prevailTitlesStyle = options.isPrevailTitlesStyle();
        final boolean hasTitlesStyle = options.getTitlesStyle() != null;
        final boolean hasStyle = options.getFieldsStyle() != null;
        final boolean agg = options.isAggregate();
        final int firstLineField = options.getInitPosRow() + 1;
        int rowNum = options.getInitPosRow();
        int columnNum;
        String translated;
        final String sep = options.getSeperatorAgg();
        Object o;

        CellStyle fieldStyle;
        Cell cell;
        Row row;

        // Generate titles
        row = sheet.createRow(rowNum++);
        generateTitles(row, options, titles);

        for (final Object obj : objs) {
            if (obj != null) {

                // Create row
                columnNum = options.getInitPosCol();
                row = sheet.createRow(rowNum);

                for (final String field : fields) {

                    // Create cell
                    cell = row.createCell(columnNum++);

                    // Resolve field
                    if (agg) {
                        o = FieldResolution.resolveFieldAggregation(obj, field);
                    } else {
                        o = FieldResolution.resolveField(obj, field);
                    }

                    // Translate
                    if (o != null) {
                        translated = TranslatorUtils.translateObject(agg, sep, translator, o, field);

                        if (translated != null) {
                            cell.setCellValue(translated);
                        }
                    }

                    // Apply style
                    if (hasStyle) {
                        fieldStyle = options.getFieldsStyle();

                        if (prevailTitlesStyle && hasTitlesStyle && rowNum == firstLineField) {
                            fieldStyle = prevailTitlesStyle(options);
                        }
                        cell.setCellStyle(fieldStyle);
                    }
                }
                rowNum++;
            }
        }

        autoSizeColumns(sheet, options, titles.length);
    }

    /**
     * Prevail bottom border titles<br>
     * It will set the top border line of the first field as the bottom border of
     * the titles <br>
     * Most templates do this
     *
     * @param options
     *            Options to generate this table ({@link TableOptions})
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
     * Puts the titles on the first line of the table, just above the fields
     *
     * @param row
     *            The row that will be used to put the titles
     * @param options
     *            Options to generate this table ({@link TableOptions}). In this
     *            case are used to give the position of the titles and their style
     * @param titles
     *            An array of String in each element is a title
     */
    private void generateTitles(final Row row, final TableOptions options, final String[] titles) {
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
     * Auto size the columns, i.e. adjust the size of the columns to its content
     *
     * @param sheet
     *            A {@linkplain Sheet}
     * @param options
     *            A {@linkplain TableOptions}
     * @param columnsNum
     *            The numbers of columns in the table
     */
    private void autoSizeColumns(final Sheet sheet, final TableOptions options, final int columnsNum) {
        if (options.isAutoSize()) {
            final boolean track = sheet instanceof SXSSFSheet;
            for (int i = options.getInitPosCol(); i < options.getInitPosCol() + columnsNum; i++) {
                if (track) {
                    ((SXSSFSheet) sheet).trackColumnForAutoSizing(i);
                }
                sheet.autoSizeColumn(i);
            }
        }
    }

    /**
     * Writes the generated document on the stream and return a stream to read
     *
     * @return {@linkplain ByteArrayInputStream} it was the content of the file
     *         generated
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
     * Writes the generated document on the stream
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
     * Workbook that is wrapped inside this instance of the {@link TableGenerator}
     *
     * @return workbook being used
     */
    public Workbook getWorkbook() {
        return workbook;
    }

}
