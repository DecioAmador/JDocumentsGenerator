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

import com.github.decioamador.jdocsgen.translation.TranslatorCollection;
import com.github.decioamador.jdocsgen.translation.TranslatorHelper;
import com.github.decioamador.jdocsgen.utils.FieldResolution;

/**
 * This class has the purpose generating tables
 *
 * @since 1.0.0.0
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
	 * @param columns
	 *            The title of the columns
	 * @param fields
	 *            The path of the field
	 * @param translator
	 *            A collection used to translate
	 * @return the sheet being used
	 * @since 1.0.0.0
	 */
	public Sheet generateSheet(final String sheetName,
			final TableOptions options, final Collection<?> objs,
			final List<String> columns, final List<String> fields,
			final TranslatorCollection translator) {

		final Sheet sheet = workbook.createSheet(sheetName);
		generateTable(sheet, options, objs, columns, fields, translator);
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
	 * @param columns
	 *            The title of the columns
	 * @param fields
	 *            The path of the field
	 * @param translator
	 *            A collection used to translate
	 * @since 1.0.0.0
	 */
	public void generateTable(final Sheet sheet,
			final TableOptions options, final Collection<?> objs,
			final List<String> columns, final List<String> fields,
			final TranslatorCollection translator) {

		int rowNum = options.getInitPosRow(), columnNum = options.getInitPosCol();
		Class<?> clazz;
		Object o;

		Cell cell;
		Row row;

		row = sheet.createRow(rowNum++);
		for(final String column : columns){
			cell = row.createCell(columnNum++);
			cell.setCellValue(column);
			cell.setCellStyle(options.getColumnsStyle());
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

		if(options.isAutosize()){
			for(int j = options.getInitPosCol(); j <= columnNum; j++){
				sheet.autoSizeColumn(j);
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
	 * @since 1.0.0.0
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
	 * @since 1.0.0.0
	 */
	public void write(final OutputStream outputStream) throws IOException {
		workbook.write(outputStream);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @since 1.0.0.0
	 */
	@Override
	public void close() throws IOException {
		workbook.close();
	}

}