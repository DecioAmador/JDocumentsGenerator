package com.github.decioamador.jdocsgen.text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import com.github.decioamador.jdocsgen.Constants;
import com.github.decioamador.jdocsgen.JDocsGenException;
import com.github.decioamador.jdocsgen.translation.TranslatorCollection;
import com.github.decioamador.jdocsgen.translation.TranslatorHelper;

/**
 * This class has the purpose of generating text
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
 * @since 1.1.0.0
 */
public class TextGenerator implements Closeable, AutoCloseable{

	private XWPFDocument document;

	/**
	 * Constructor of TextGenerator
	 * 
	 * @param document
	 *            Document that will be used
	 */
	public TextGenerator(XWPFDocument document) {
		this.document = document;
	}

	/**
	 * It generates the document base on the arguments <br>
	 * <br>
	 * <b>Tip:</b> You should use ArrayList on labels and fields
	 * 
	 * @param obj
	 * @param options
	 *            Options to generate this table
	 * @param obj
	 *            Object that will be use to generate line
	 * @param columns
	 *            The labels of the fields
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
	 * @return the paragraph being used
	 * @throws Exception
	 *             If the document can't be generated
	 * @since 1.1.0.0
	 */
	public XWPFParagraph generate(Object obj, TextOptions options,
			List<String> labels, List<String> fields,
			TranslatorCollection translator) throws JDocsGenException {
		boolean going;
		int j;

		XWPFParagraph paragraph = document.createParagraph();
		XWPFRun run;

		Class<?> clazz, temp;
		Method m;
		String [] mthds;
		Object o;
		
		if(obj != null){
			clazz = obj.getClass();
			for(int i=0;i<fields.size();i++){
				mthds = fields.get(i).split("[.]");
				temp = clazz;
				o = obj;
				j = 0;
				going = true;
				while(j<mthds.length && going){
					try {
						m = temp.getDeclaredMethod(Constants.GET+mthds[j], Constants.EMPTY_ARRAY_CLASS);
					} catch (NoSuchMethodException | SecurityException e) {
						throw new JDocsGenException("One of the fields isn't correct.",e);
					}
					if(m != null){
						try {
							o = m.invoke(o, Constants.EMPTY_ARRAY_OBJECT);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							throw new JDocsGenException(e);
						}
						if(o != null){
							temp = o.getClass();
						} else {
							going = false;
						}
					}
					j++;
				}

				run = paragraph.createRun();
				run.setText(labels.get(i) + options.getBetweenLabelAndField());
				run = paragraph.createRun();
				run.setText(TranslatorHelper.getValue(o, fields.get(i), translator));
				run.addBreak();
				if(options.isAddBreakLineBeetweenLines()){
					run.addBreak();
				}
			}
		}
		
		return paragraph;
	}
	
	public XWPFTable generateTable(final List<?> objs,
			final List<String> columns,final List<String> fields,
			final TranslatorCollection translator) throws JDocsGenException {
		int i;
		String [] mthds;
		boolean going;
		
		Object o;
		Class<?> clazz, temp;
		Method m;
		
		XWPFTable table = document.createTable();
		XWPFTableRow row;
		
		row = table.getRow(0);
		for(int w=0;w<columns.size();w++){
			if(w==0){
				row.getCell(0).setText(columns.get(w));
			} else {
				row.addNewTableCell().setText(columns.get(w));
			}
		}
		
		for(final Object obj : objs){
			if(obj != null){
				clazz = obj.getClass();
				row = table.createRow();
				for(int w=0;w<columns.size();w++){
					mthds = fields.get(0).split("[.]");
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
					row.getCell(w).setText(TranslatorHelper.getValue(o, fields.get(0), translator));
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
	 * @since 1.1.0.0
	 */
	public ByteArrayInputStream write() throws IOException{
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
	 * @since 1.1.0.0
	 */
	public void write(final OutputStream outputStream) throws IOException{
		document.write(outputStream);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.1.0.0
	 */
	@Override
	public void close() throws IOException {
		document.close();
	}
}