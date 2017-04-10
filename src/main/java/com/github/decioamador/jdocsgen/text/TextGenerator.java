package com.github.decioamador.jdocsgen.text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.github.decioamador.jdocsgen.translation.TranslatorCollection;
import com.github.decioamador.jdocsgen.translation.TranslatorHelper;
import com.github.decioamador.jdocsgen.utils.FieldResolution;

/**
 * This class has the purpose of generating text
 */
public class TextGenerator implements AutoCloseable {

	private final XWPFDocument document;

	/**
	 * Constructor of TextGenerator
	 *
	 * @param document
	 *            Document that will be used
	 */
	public TextGenerator(final XWPFDocument document) {
		this.document = document;
	}

	/**
	 * It generates a paragraph base on the arguments <br>
	 * <br>
	 * <b>Tip:</b> You should use {@link ArrayList} on labels and fields
	 *
	 * @param obj
	 *            Object that will be use to generate line
	 * @param options
	 *            Options to generate this table
	 * @param labels
	 *            The labels of the fields
	 * @param fields
	 *            The path of the field
	 * @param translator
	 *            A collection used to translate
	 * @return the paragraph being used
	 */
	public XWPFParagraph generateParagraph(final Object obj, final TextOptions options,
			final List<String> labels, final List<String> fields,
			final TranslatorCollection translator) {

		final XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run;

		Class<?> clazz;
		Object o;
		String field;

		if(obj != null){
			clazz = obj.getClass();
			for(int i=0;i<fields.size();i++){
				field = fields.get(i);
				o = FieldResolution.resolveField(clazz, obj, field);

				run = paragraph.createRun();
				run.setText(labels.get(i) + options.getBetweenLabelAndField());
				run = paragraph.createRun();
				run.setText(TranslatorHelper.getValue(o, fields.get(i), translator));
				run.addBreak();
			}
		}

		return paragraph;
	}

	/**
	 * It generates a table base on the arguments <br>
	 * <br>
	 * <b>Tip:</b> You should use {@link ArrayList} on labels and fields
	 *
	 * @param objs
	 *            Objects that will be the lines
	 * @param titles
	 *            The title of the columns
	 * @param fields
	 *            The path of the field
	 * @param translator
	 *            A collection used to translate
	 * @return the table generated
	 */
	public XWPFTable generateTable(final Collection<?> objs,
			final List<String> titles, final List<String> fields,
			final TranslatorCollection translator) {

		int rowNum = 0;
		Object o;
		Class<?> clazz;
		String field;

		final XWPFTable table = document.createTable(objs.size()+1, titles.size());
		XWPFTableRow row = table.getRow(rowNum++);

		for(int i=0;i<titles.size();i++){
			row.getCell(i).setText(titles.get(i));
		}

		for(final Object obj : objs){
			if(obj != null){
				clazz = obj.getClass();
				row = table.getRow(rowNum++);

				for(int i=0;i<fields.size();i++){
					field = fields.get(i);
					o = FieldResolution.resolveField(clazz, obj, field);
					row.getCell(i).setText(TranslatorHelper.getValue(o, field, translator));
				}
			}
		}

		return table;
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
		document.write(bos);
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
		document.write(outputStream);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		document.close();
	}
}