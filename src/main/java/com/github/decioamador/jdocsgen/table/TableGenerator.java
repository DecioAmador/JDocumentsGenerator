package com.github.decioamador.jdocsgen.table;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFSheet;

import com.github.decioamador.jdocsgen.translation.TranslatorCollection;
import com.github.decioamador.jdocsgen.translation.TranslatorHelper;
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
		this.workbook = workbook;
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
	 *            A collection used to translate
	 * @return the sheet being used
	 */
	public Sheet generateTable(final String sheetName,
			final TableOptions options, final Collection<?> objs,
			final List<String> titles, final List<String> fields,
			final TranslatorCollection translator) {

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
	 *            A collection used to translate
	 */
	public void generateTable(final Sheet sheet, final TableOptions options,
			final Collection<?> objs, final List<String> titles,
			final List<String> fields, final TranslatorCollection translator) {

		int rowNum = options.getInitPosRow(), columnNum = options.getInitPosCol();
		Class<?> clazz;
		Object o;

		Cell cell;
		Row row;

		row = sheet.createRow(rowNum++);
		for(final String title : titles){
			cell = row.createCell(columnNum++);
			cell.setCellValue(title);
			cell.setCellStyle(options.getTitlesStyle());
		}

		for(final Object obj : objs){
			if(obj != null){
				clazz = obj.getClass();
				columnNum = options.getInitPosCol();
				row = sheet.createRow(rowNum++);
				for(final String field : fields){
					o = FieldResolution.resolveField(clazz, obj, field);
					cell = row.createCell(columnNum++);
					cell.setCellValue(TranslatorHelper.getValue(o, field, translator));
					cell.setCellStyle(options.getFieldsStyle());
				}
			}
		}

		autosizeColumns(sheet, options, titles.size());
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
		if(options.isAutosize()){
			final boolean track = sheet instanceof SXSSFSheet;
			for(int i=options.getInitPosCol(); i<(options.getInitPosCol() + titlesSize); i++){
				if(track){
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
	 *            If an I/O error occurs. In particular, an IOException may be
	 *            thrown if the output stream has been closed.
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
	 *            If an I/O error occurs. In particular, an IOException may be
	 *            thrown if the output stream has been closed.
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

}