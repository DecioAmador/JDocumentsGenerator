package com.github.decioamador.jdocsgen.table;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.github.decioamador.jdocsgen.Constants;
import com.github.decioamador.jdocsgen.JDocsGenException;
import com.github.decioamador.jdocsgen.translation.TranslatorCollection;
import com.github.decioamador.jdocsgen.translation.TranslatorHelper;

/**
 * This class has the purpose generating tables
 * 
 * <br>
 * <br>Copyright 2016 Décio Amador <br>
 * <br>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License. <br>
 * You may obtain a copy of the License at <br>
 * <br>
 *     http://www.apache.org/licenses/LICENSE-2.0 <br>
 * <br>
 * Unless required by applicable law or agreed to in writing, software <br>
 * distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License. <br>
 * 
 * @author Décio Amador
 * @since 1.0.0.0
 */
public class TableGenerator implements Closeable, AutoCloseable {
	
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
	 * @param fieldsToTranslate
	 *            Fields that you want to translate
	 * @param translator
	 *            It have the key and the value to translate
	 * @param propsToTrans
	 *            Fields that you want to translate with Properties
	 * @param prop
	 *            Properties to used
	 * @param resrcBunToTrans
	 *            Fields that you want to translate with ResourceBundle
	 * @param rb
	 *            ResoruceBundle to be used
	 * @return the sheet being used
	 * @throws JDocsGenException
	 *             If the document can't be generated
	 * @since 1.0.0.0
	 */
	public Sheet generate(final String sheetName, 
			final TableOptions options, final List<?> objs, 
			final List<String> columns, final List<String> fields, 
			final TranslatorCollection translator) throws JDocsGenException {
		int rowNum = options.getInitPosRow(), columnNum = options.getInitPosCol(), i;
		String [] mthds;
		boolean going;
		
		Object o;
		Class<?> clazz, temp;
		Method m;
		
		Sheet ws = workbook.createSheet(sheetName);
		Row row;
		Cell cell;
		
		row = ws.createRow(rowNum++);
		for(final String column : columns){
			cell = row.createCell(columnNum++);
			cell.setCellValue(column);
			cell.setCellStyle(options.getColumnsStyle());
		}
		
		for(final Object obj : objs){
			if(obj != null){
				clazz = obj.getClass();
				columnNum = options.getInitPosCol();
				row = ws.createRow(rowNum++);
				for(final String field : fields){
					mthds = field.split("[.]");
					temp = clazz;
					o = obj;
					i = 0;
					going = true;
					while(i<mthds.length && going){
						try {
							m = temp.getDeclaredMethod(Constants.GET+mthds[i],
									Constants.EMPTY_ARRAY_CLASS);
						} catch (NoSuchMethodException | SecurityException e) {
							throw new JDocsGenException(e);
						}
						if(m != null){
							try {
								o = m.invoke(o, Constants.EMPTY_ARRAY_OBJECT);
							} catch (IllegalAccessException 
									| InvocationTargetException 
									| IllegalArgumentException e) {
								throw new JDocsGenException(e);
							}
							if(o != null){
								temp = o.getClass();
							} else {
								going = false;
							}
						}
						i++;
					}
					cell = row.createCell(columnNum++);
					cell.setCellValue(TranslatorHelper.getValue(o, field, translator));
					cell.setCellStyle(options.getFieldsStyle());
				}
			}
		}
		
		if(options.isAutosize()){
			for(int j = options.getInitPosCol(); j <= columnNum; j++){
				ws.autoSizeColumn(j);
			}
		}
		return ws;
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
	public ByteArrayInputStream write() throws IOException{
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
	public void write(final OutputStream outputStream) throws IOException{
		workbook.write(outputStream);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0.0
	 */
	@Override
	public void close() throws IOException {
		workbook.close();
	}
	
}