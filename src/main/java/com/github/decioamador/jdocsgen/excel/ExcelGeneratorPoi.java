package com.github.decioamador.jdocsgen.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

import com.github.decioamador.jdocsgen.excel.model.ExcelOptionPoi;

/**
 * This class is an implementation using org.apache.poi
 * @author D&eacute;cio Amador
 */
public class ExcelGeneratorPoi implements ExcelGenerator  {
	
	private static final String EMPTY_STRING = "";
	private static final Class<?>[] EMPTY_ARRAY_CLASS = new Class[0];
	private static final Object[] EMPTY_ARRAY_OBJECT = new Object[0];
	private static final String GET = "get";
	
	private int initPosRow = 0;
	private int initPosCol = 0;
	private Boolean autosize = false;
	private String dateFormat = "yyyy-MM-dd";
	private DateFormat sdf; 
	
	private HSSFWorkbook wb;
	private HSSFSheet ws;
	
	{
		wb = new HSSFWorkbook();
		ws = wb.createSheet();
	}

	public ExcelGeneratorPoi() {
		postConstruct();
	}
	
	public ExcelGeneratorPoi(Map<ExcelOptionPoi,Object> options) {
		resolveOptions(options);
		postConstruct();
	}
	
	/**
	 * Resolves the options available
	 * 
	 * @param options
	 *            Options of the this implementation
	 */
	private void resolveOptions(Map<ExcelOptionPoi,Object> options){
		for(Entry<ExcelOptionPoi, Object> option : options.entrySet()){
			if(option.getValue() != null){
				switch(option.getKey()){
					case DATE_FORMAT :
						if(option.getValue() instanceof String){
							dateFormat = option.getValue().toString();
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
				}
			}
		}
	}

	/**
	 * It should work like the annotation that I didn't use it 
	 * so this can still be used in Java SE.
	 */
	private void postConstruct(){
		sdf = new SimpleDateFormat(dateFormat);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
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

	@Override
	public void generate(List<?> objs, List<String> columns, List<String> fields, Set<String> fieldsToTrans,
			Map<String, String> translator, Set<String> resrcBunToTrans, ResourceBundle rb) throws Exception {
		generate(objs,columns,fields,fieldsToTrans,translator,null,null,resrcBunToTrans,rb);
	}

	@Override
	public void generate(List<?> objs, List<String> columns, List<String> fields, Set<String> resrcBunToTrans,
			ResourceBundle rb) throws Exception {
		generate(objs,columns,fields,null,null,null,null,resrcBunToTrans,rb);
	}

	@Override
	public void generate(List<?> objs, List<String> columns, List<String> fields, Set<String> propsToTrans,
			Properties prop) throws Exception {
		generate(objs,columns,fields,null,null,propsToTrans,prop,null,null);
	}

	@Override
	public void generate(List<?> objs, List<String> columns, List<String> fields) throws Exception {
		generate(objs,columns,fields,null,null,null,null,null,null);
	}
	
	@Override
	public void generate(List<?> objs, List<String> columns, List<String> fields, Set<String> fieldsToTrans,
			Map<String, String> translator, Set<String> propsToTrans, Properties prop) throws Exception {
		generate(objs,columns,fields,fieldsToTrans,translator,propsToTrans,prop,null,null);
	}
	
	@Override
	public void generate(List<?> objs, List<String> columns, List<String> fields, 
			Set<String> fieldsToTrans, Map<String, String> translator) throws Exception {
		generate(objs,columns,fields,fieldsToTrans,translator,null,null,null,null);
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
				result = prop.getProperty(obj.toString());
			}else if(resrcBunToTrans != null && rb != null
					&& resrcBunToTrans.contains(field)){
				try{
					result = rb.getString(obj.toString());
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
	 * {@inheritDoc}
	 * @return 
	 */
	@Override
	public ByteArrayInputStream write() throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		wb.write(bos);
		return new ByteArrayInputStream(bos.toByteArray());
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(OutputStream os) throws IOException{
		wb.write(os);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		wb.close();
	}

}