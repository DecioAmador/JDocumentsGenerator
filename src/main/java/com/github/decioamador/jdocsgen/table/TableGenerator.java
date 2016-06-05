package com.github.decioamador.jdocsgen.table;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This class has the purpose of generating a table
 */
public class TableGenerator implements Closeable, AutoCloseable  {
	
	private static final String EMPTY_STRING = "";
	private static final Class<?>[] EMPTY_ARRAY_CLASS = new Class[0];
	private static final Object[] EMPTY_ARRAY_OBJECT = new Object[0];
	private static final String GET = "get";
	
	private int initPosRow = 0;
	private int initPosCol = 0;
	private Boolean autosize = false;
	private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
	private String prefixResourceBundle = null;
	private String prefixProperties = null;
	
	private HSSFWorkbook wb;
	private HSSFSheet ws;

	{
		wb = new HSSFWorkbook();
		ws = wb.createSheet();
	}

	public TableGenerator() {
	}
	
	public TableGenerator(Map<TableOption,Object> options) {
		resolveOptions(options);
	}
	
	/**
	 * Resolves the options available
	 * 
	 * @param options
	 *            Options of the this implementation
	 */
	private void resolveOptions(Map<TableOption,Object> options){
		for(Entry<TableOption, Object> option : options.entrySet()){
			if(option.getValue() != null){
				switch(option.getKey()){
					case DATE_FORMAT :
						if(option.getValue() instanceof String){
							sdf = new SimpleDateFormat(option.getValue().toString());
						}
						break;
					case INICIAL_POSITION_ROW:
						if(option.getValue() instanceof Integer){
							initPosRow = (Integer) option.getValue();
						}
						break;
					case INICIAL_POSITION_COLUMN:
						if(option.getValue() instanceof Integer){
							initPosCol = (Integer) option.getValue();
						}
						break;
					case AUTOSIZE_COLUMNS:
						if(option.getValue() instanceof Boolean){
							autosize = Boolean.valueOf(option.getValue().toString());
						}
						break;
					case PREFIX_PROPERTIES:
						if(option.getValue() instanceof String){
							prefixProperties = option.getValue().toString();
						}
						break;
					case PREFIX_RESOURCE_BUNDLE:
						if(option.getValue() instanceof String){
							prefixResourceBundle = option.getValue().toString();
						}
						break;
				}
			}
		}
	}

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
	 * @param propsToTrans
	 *            Fields that you want to translate with Properties
	 * @param prop
	 *            Properties to used
	 * @param resrcBunToTrans
	 *            Fields that you want to translate with ResourceBundle
	 * @param rb
	 *            ResoruceBundle to be used
	 * @throws Exception
	 *             If the document can't be generated
	 * @since 1.0.0.0
	 */
	public void generate(List<?> objs, List<String> columns, List<String> fields, 
			Set<String> fieldsToTranslate, Map<String,String> translator,
			Set<String> propsToTrans, Properties prop, 
			Set<String> resrcBunToTrans, ResourceBundle rb) throws Exception {
		int rowNum = initPosRow, columnNum = initPosCol, i;
		String [] mthds;
		boolean going;
		
		Object o = null;
		Class<?> clazz, temp;
		Method m;
		
		HSSFRow row;
		HSSFCell cell;
		
		row = ws.createRow(rowNum++);
		for(String column : columns){
			cell = row.createCell(columnNum++);
			cell.setCellValue(column);
		}
		
		for(Object obj : objs){
			if(obj != null){
				clazz = obj.getClass();
				columnNum = initPosCol;
				row = ws.createRow(rowNum++);
				for(String field : fields){
					mthds = field.split("[.]");
					temp = clazz;
					o = obj;
					i = 0;
					going = true;
					while(i<mthds.length && going){
						m = temp.getDeclaredMethod(GET+mthds[i], EMPTY_ARRAY_CLASS);
						if(m != null){
							o = m.invoke(o, EMPTY_ARRAY_OBJECT);
							if(o != null){
								temp = o.getClass();
							} else {
								going = false;
							}
						}
						i++;
					}
					cell = row.createCell(columnNum++);
					cell.setCellValue(getValue(o,field,fieldsToTranslate,
							translator,propsToTrans,prop,resrcBunToTrans,rb));
				}
			}
		}
		
		if(autosize){
			for(int j = initPosCol; j <= columnNum; j++){
				ws.autoSizeColumn(j);
			}
		}
	}

	/**
	 * It generates the document base on the arguments
	 * 
	 * @param objs
	 *            Objects that will be the lines
	 * @param columns
	 *            The title of the columns
	 * @param fields
	 *            The path of the field
	 * @param fieldsToTrans
	 *            Fields that you want to translate
	 * @param translator
	 *            It have the key and the value to translate
	 * @param resrcBunToTrans
	 *            Fields that you want to translate with ResourceBundle
	 * @param rb
	 *            ResoruceBundle to be used
	 * @throws Exception
	 *             If the document can't be generated
	 * @since 1.0.0.0
	 */
	public void generate(List<?> objs, List<String> columns, List<String> fields, 
			Set<String> fieldsToTrans, Map<String, String> translator, 
			Set<String> resrcBunToTrans, ResourceBundle rb) throws Exception {
		generate(objs,columns,fields,fieldsToTrans,translator,null,null,resrcBunToTrans,rb);
	}

	/**
	 * It generates the document base on the arguments
	 * 
	 * @param objs
	 *            Objects that will be the lines
	 * @param columns
	 *            The title of the columns
	 * @param fields
	 *            The path of the field
	 * @param resrcBunToTrans
	 *            Fields that you want to translate with ResourceBundle
	 * @param rb
	 *            ResoruceBundle to be used
	 * @throws Exception
	 *             If the document can't be generated
	 * @since 1.0.0.0
	 */
	public void generate(List<?> objs, List<String> columns, List<String> fields, 
			Set<String> resrcBunToTrans, ResourceBundle rb) throws Exception {
		generate(objs,columns,fields,null,null,null,null,resrcBunToTrans,rb);
	}
	
	/**
	 * It generates the document base on the arguments
	 * 
	 * @param objs
	 *            Objects that will be the lines
	 * @param columns
	 *            The title of the columns
	 * @param fields
	 *            The path of the field
	 * @param fieldsToTrans
	 *            Fields that you want to translate
	 * @param translator
	 *            It have the key and the value to translate
	 * @param propsToTrans
	 *            Fields that you want to translate with Properties
	 * @param prop
	 *            Properties to used
	 * @throws Exception
	 *            If the document can't be generated
	 * @since 1.0.0.0
	 */
	public void generate(List<?> objs, List<String> columns, List<String> fields, 
			Set<String> fieldsToTrans, Map<String, String> translator, 
			Set<String> propsToTrans, Properties prop) throws Exception {
		generate(objs,columns,fields,fieldsToTrans,translator,propsToTrans,prop,null,null);
	}

	/**
	 * It generates the document base on the arguments
	 * 
	 * @param objs
	 *            Objects that will be the lines
	 * @param columns
	 *            The title of the columns
	 * @param fields
	 *            The path of the field
	 * @param propsToTrans
	 *            Fields that you want to translate with Properties
	 * @param prop
	 *            Properties to used
	 * @throws Exception
	 *            If the document can't be generated
	 * @since 1.0.0.0
	 */
	public void generate(List<?> objs, List<String> columns, List<String> fields, 
			Set<String> propsToTrans, Properties prop) throws Exception {
		generate(objs,columns,fields,null,null,propsToTrans,prop,null,null);
	}
	
	/**
	 * It generates the document base on the arguments
	 * 
	 * @param objs
	 *            Objects that will be the lines
	 * @param columns
	 *            The title of the columns
	 * @param fields
	 *            The path of the field
	 * @param fieldsToTrans
	 *            Fields that you want to translate
	 * @param translator
	 *            It have the key and the value to translate
	 * @throws Exception
	 *             If the document can't be generated
	 * @since 1.0.0.0
	 */
	public void generate(List<?> objs, List<String> columns, List<String> fields, 
			Set<String> fieldsToTrans, Map<String, String> translator) throws Exception {
		generate(objs,columns,fields,fieldsToTrans,translator,null,null,null,null);
	}

	/**
	 * It generates the document base on the arguments
	 * 
	 * @param objs
	 *            Objects that will be the lines
	 * @param columns
	 *            The title of the columns
	 * @param fields
	 *            The path of the field
	 * @throws Exception
	 *            If the document can't be generated
	 * @since 1.0.0.0
	 */
	public void generate(List<?> objs, List<String> columns, List<String> fields) throws Exception {
		generate(objs,columns,fields,null,null,null,null,null,null);
	}
	
	/**
	 * This methods makes the textual representation of a value
	 * 
	 * @param obj
	 *            Object to process
	 * @param field
	 *            Field to process
	 * @param fieldsToTrans
	 *            Fields to translate
	 * @param translator
	 *            It has the the key and the translation value
	 * @return String the representation value of the object
	 */
	private String getValue(Object obj, String field, 
			Set<String> fieldsToTrans, Map<String,String> translator,
			Set<String> propsToTrans, Properties prop,
			Set<String> resrcBunToTrans, ResourceBundle rb){
		String result = null;
		if(obj != null && field != null && !field.isEmpty()){
			if(fieldsToTrans != null && translator != null
					&& fieldsToTrans.contains(field)){
				result = translator.get(obj.toString());
			} else if(propsToTrans != null && prop != null
					&& propsToTrans.contains(field)){
				if(prefixProperties == null){
					result = prop.getProperty(obj.toString());
				} else {
					result = prop.getProperty(prefixProperties+obj.toString());
				}
			}else if(resrcBunToTrans != null && rb != null
					&& resrcBunToTrans.contains(field)){
				try{
					if(prefixResourceBundle == null){
						result = rb.getString(obj.toString());
					} else {
						result = rb.getString(prefixResourceBundle+obj.toString());
					}
				} catch(MissingResourceException | ClassCastException e) {
					result = EMPTY_STRING;
				}
			} else if(obj instanceof Date){
				result = sdf.format((Date) obj);
			} else {
				result = obj.toString();
			}
		}
		if(result == null){
			result = EMPTY_STRING;
		}
		return result;
	}
	
	/**
	 * Returns the workbook being used
	 * @return workbook
	 * @since 1.0.0.0
	 */
	public HSSFWorkbook getHSSFWorkBook(){
		return wb;
	}
	
	/**
	 * Returns the sheet being used
	 * @return sheet
	 * @since 1.0.0.0
	 */
	public HSSFSheet getHSSFSheet(){
		return ws;
	}
	
	/**
	 * Writes the generated document on the stream
	 * 
	 * @return InputStream it was the content of the file generated
	 * @throws IOException
	 *            If an I/O error occurs. In particular, an IOException may be
	 *            thrown if the output stream has been closed.
	 * @since 1.0.0.0
	 */
	public ByteArrayInputStream write() throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		wb.write(bos);
		return new ByteArrayInputStream(bos.toByteArray());
	}
	
	/**
	 * Writes the generated document on the stream, writing on the existing one
	 * 
	 * @param os
	 *            OutputStream that will have the content
	 * @throws IOException
	 *            If an I/O error occurs. In particular, an IOException may be
	 *            thrown if the output stream has been closed.
	 * @since 1.0.0.0
	 */
	public void write(OutputStream os) throws IOException{
		wb.write(os);
	}

	/**
	 * {@inheritDoc}
	 * @since 1.0.0.0
	 */
	public void close() throws IOException {
		wb.close();
	}

}