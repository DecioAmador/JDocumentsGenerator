package org.decioamador.generator.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.decioamador.generator.excel.model.ExcelOption;

/**
 * This class is an implementation using org.apache.poi
 * @author D&eacute;cio Amador
 */
public class ExcelGeneratorPOI implements ExcelGenerator  {
	
	private static final Class<?>[] EMPTY_ARRAY_CLASS = new Class[0];
	private static final Object[] EMPTY_ARRAY_OBJECT = new Object[0];
	private static final String GET = "get";
	
	private int initialPosition = 0;
	private Boolean autosize = false;
	private String dateFormat = "yyyy-MM-dd hh:mm:ss";
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
	
	public ExcelGeneratorPOI(Map<ExcelOption,Object> options) {
		resolveOptions(options);
		postConstruct();
	}
	
	/**
	 * Resolves the options available
	 * @param options
	 */
	private void resolveOptions(Map<ExcelOption,Object> options){
		for(Entry<ExcelOption, Object> option : options.entrySet()){
			if(option.getValue() != null){
				switch(option.getKey()){
					case DATE_FORMAT :
						if(option.getValue() instanceof String){
							dateFormat = option.getValue().toString();
						}
						break;
					case INICIAL_POSITION:
						if(option.getValue() instanceof Integer){
							initialPosition = Integer.valueOf(option.getValue().toString());
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
	 * It should work like the annotation that I didn't use so that this can
	 * still be used in Java SE.
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
		boolean toTranslate = false;
		int rowNum = initialPosition, columnNum = initialPosition;
		
		Object o = null;
		Class<?> clazz;
		Method m;
		
		HSSFRow row;
		HSSFCell cell;
		
		if(columns.size() != fields.size()){
			throw new IllegalArgumentException("The columns don't have the same size as the fields");
		}
		
		// Put titles
		row = ws.createRow(rowNum++);
		for(String column : columns){
			cell = row.createCell(columnNum++);
			cell.setCellValue(column);
		}
		
		// Put values
		for(Object obj : objs){
			clazz = obj.getClass();
			columnNum = initialPosition;
			row = ws.createRow(rowNum++);
			for(String field : fields){
				String [] mthds = field.split("[.]");
				Class<?> temp = clazz;
				o = obj;
				for(String s : mthds){
					m = temp.getDeclaredMethod(GET+s, EMPTY_ARRAY_CLASS);
					o = m.invoke(o, EMPTY_ARRAY_OBJECT);
					if(o != null){
						temp = o.getClass();
					} else {
						break;
					}
				}
				cell = row.createCell(columnNum++);
				if(fieldsToTranslate != null){
					toTranslate = fieldsToTranslate.contains(field);
				}
				cell.setCellValue(getValue(o, toTranslate, translator));
			}
		}
		
		// Autosize
		if(autosize){
			for(int i = initialPosition; i <= columnNum; i++){
				ws.autoSizeColumn(i);
			}
		}
	}
	
	/**
	 * This methods makes the textual representation of a value
	 * @param obj
	 * @param toTranslate
	 * @param translator
	 * @return String 
	 */
	private String getValue(Object obj, boolean toTranslate, Map<String,String> translator){
		if(obj != null){
			if(toTranslate){
				return translator.get(obj.toString());
			} else if(obj instanceof Date){
				return sdf.format((Date) obj);
			} else {
				return obj.toString();
			}
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