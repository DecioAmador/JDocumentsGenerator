package org.decioamador.generator.excel;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.decioamador.generator.excel.model.ExcelOptionPOI;

/**
 * This class is an implementation using org.apache.poi
 * @author D&eacute;cio Amador
 */
public class ExcelGeneratorPOI implements ExcelGenerator  {
	
	private static final List<Class<?>> CLASSES = Arrays.asList(
			String.class,StringBuilder.class,StringBuffer.class,
			Double.class,Float.class,Short.class,Integer.class,Long.class,
			Date.class,Boolean.class,Byte.class);
	private static final Class<?>[] EMPTY_ARRAY_CLASS = new Class[0];
	private static final Object[] EMPTY_ARRAY_OBJECT = new Object[0];
	private static final String GET = "get";
	private static final String GET_CLASS = "getClass";
	
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

	public ExcelGeneratorPOI() {
		postConstruct();
	}
	
	public ExcelGeneratorPOI(Map<ExcelOptionPOI,Object> options) {
		resolveOptions(options);
		postConstruct();
	}
	
	/**
	 * Resolves the options available
	 * 
	 * @param options
	 *            Options of the this implementation
	 */
	private void resolveOptions(Map<ExcelOptionPOI,Object> options){
		for(Entry<ExcelOptionPOI, Object> option : options.entrySet()){
			if(option.getValue() != null){
				switch(option.getKey()){
					case DATE_FORMAT :
						if(option.getValue() instanceof String){
							dateFormat = option.getValue().toString();
						}
						break;
					case INICIAL_POSITION:
						if(option.getValue() instanceof Point){
							Point p = (Point) option.getValue();
							initPosRow = new Double(p.getX()).intValue();
							initPosCol = new Double(p.getY()).intValue();
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
	 * It should work like the annotation that I didn't use it, 
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
			Set<String> fieldsToTranslate, Map<String,String> translator) throws Exception {
		int rowNum = initPosRow, columnNum = initPosCol, i;
		boolean going;
		Object o = null;
		Class<?> clazz;
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
					String [] mthds = field.split("[.]");
					Class<?> temp = clazz;
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
					cell.setCellValue(getValue(o, field, fieldsToTranslate, translator));
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
	 * {@inheritDoc}
	 */
	@Override
	public void generate(List<?> objs, List<String> columns, Set<String> fieldsToTrans, 
			Map<String,String> translator) throws Exception {
		int rowNum = initPosRow, columnNum = initPosCol;
		List<DualString> rowValues;
		HSSFRow row;
		HSSFCell cell;
		
		row = ws.createRow(rowNum++);
		for(String column : columns){
			cell = row.createCell(columnNum++);
			cell.setCellValue(column);
		}
		
		for(Object obj : objs){
			if(obj != null){
				columnNum = initPosCol;
				rowValues = new ArrayList<>();
				row = ws.createRow(rowNum++);
				handleObject(obj,row,rowValues,"",fieldsToTrans,translator);
				Collections.sort(rowValues);
				for(DualString ds : rowValues){
					cell = row.createCell(columnNum++);
					cell.setCellValue(ds.getValue());
				}
			}
		}
		
		if(autosize){
			for(int i = initPosCol; i <= columnNum; i++){
				ws.autoSizeColumn(i);
			}
		}
	}
	
	/**
	 * Recursive method to handle the object
	 * 
	 * @param o
	 *            Object being handle
	 * @param row
	 *            Row being process
	 * @param rowValues
	 *            Row values
	 * @param field
	 *            Field path
	 * @param fieldsToTrans
	 *            Fields that you want to translate
	 * @param translator
	 *            It have the key and the value to translate
	 * @throws Exception
	 *             Whenever the object can't be handle
	 */
	private void handleObject(Object o, HSSFRow row, List<DualString> rowValues, String field,
			Set<String> fieldsToTrans, Map<String,String> translator) throws Exception{
		if(o != null){
			for(Method m : o.getClass().getMethods()){
				if(m.getName().startsWith(GET) && !m.getName().equals(GET_CLASS)){
					Object temp = m.invoke(o, EMPTY_ARRAY_OBJECT);
					if(temp != null){
						String aux = field+"."+m.getName().substring(3);
						if(!CLASSES.contains(temp.getClass())){
							handleObject(temp,row,rowValues,aux,fieldsToTrans,translator);
						} else {
							rowValues.add(new DualString(m.getName(),getValue(
									temp,aux.substring(1),fieldsToTrans,translator)));
						}
					}
				}
			}
		}
	}
	
	/**
	 * Utility class 
	 */
	private static class DualString implements Comparable<DualString>{
		
		private String name;
		private String value;
		
		public DualString(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		public String getName(){
			return name;
		}

		@Override
		public int compareTo(DualString o) {
			return name.compareTo(o.getName());
		}
		
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
			Set<String> fieldsToTrans, Map<String,String> translator){
		if(obj != null){
			
			if(fieldsToTrans != null && translator != null){
				if(fieldsToTrans.contains(field)){
					return translator.get(obj.toString());
				}
			}
			
			if(obj instanceof Date){
				return sdf.format((Date) obj);
			}
			
			return obj.toString();
		} else {
			return "";
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(InputStream is) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		wb.write(bos);
		is = new ByteArrayInputStream(bos.toByteArray());
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