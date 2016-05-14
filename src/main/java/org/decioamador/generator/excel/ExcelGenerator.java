package org.decioamador.generator.excel;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This generates an Excel Document based on the object itself
 * 
 * @author D&eacute;cio Amador
 */
public interface ExcelGenerator extends Closeable, AutoCloseable {

	/**
	 * It generates the document base on the arguments
	 * 
	 * @param objs
	 *            Objects that will be the lines
	 * @param columns
	 *            The title of the columns
	 * @param fields
	 *            The path of the field
	 * @param fieldsToTranslate
	 *            Fields that you want to translate
	 * @param translator
	 *            It have the key and the value to translate
	 * @throws Exception
	 *             if the document can't be generated
	 * @requires the size of the columns as to be the same as the fields
	 */
	public void generate(List<?> objs, List<String> columns, List<String> fields, Set<String> fieldsToTranslate,
			Map<String, String> translator) throws Exception;

	/**
	 * Writes the generated document on the stream
	 * 
	 * @param is
	 * @throws IOException
	 *             if an I/O error occurs. In particular, an IOException may be
	 *             thrown if the output stream has been closed.
	 */
	public void write(InputStream is) throws IOException;

	/**
	 * Writes the generated document on the stream
	 * 
	 * @param os
	 * @throws IOException
	 *             if an I/O error occurs. In particular, an IOException may be
	 *             thrown if the output stream has been closed.
	 */
	public void write(OutputStream os) throws IOException;

	/**
	 * {@inheritDoc}
	 */
	public void close() throws IOException;

}