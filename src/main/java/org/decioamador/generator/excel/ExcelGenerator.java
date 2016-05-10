package org.decioamador.generator.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
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

import javax.annotation.PostConstruct;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.decioamador.generator.excel.model.ExcelOptionId;

/**
 * @author D&eacute;cio Amador
 */
public class ExcelGenerator implements Closeable, AutoCloseable {
	
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

	public ExcelGenerator() {
		sdf = new SimpleDateFormat(dateFormat);
	}
	
	public ExcelGenerator(Map<ExcelOptionId,Object> options) {
		resolveOptions(options);
		sdf = new SimpleDateFormat(dateFormat);
	}
	
	private void resolveOptions(Map<ExcelOptionId,Object> options){
		for(Entry<ExcelOptionId, Object> option : options.entrySet()){
			if(option.getValue() != null){
				switch(option.getKey()){
					case DATE_FORMAT :
						dateFormat = option.getValue().toString();
					break;
					case INICIAL_POSITION:
						initialPosition = Integer.valueOf(option.getValue().toString());
					break;
					case AUTOSIZE:
						autosize = Boolean.valueOf(option.getValue().toString());
					break;
				}
			}
		}
	}
	
	public void generate(List<?> objs, List<String> columns, List<String> fields, 
			Set<String> fieldsToTranslate, Map<String,String> translator) throws Exception {
		boolean toTranslate;
		int rowNum = initialPosition, columnNum = initialPosition;
		
		Object o = null;
		Class<?> clazz;
		Method m;
		
		HSSFRow row;
		HSSFCell cell;
		
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
				toTranslate = fieldsToTranslate.contains(field);
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
	
	public void write(InputStream is) throws IOException{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		wb.write(bos);
		is = new ByteArrayInputStream(bos.toByteArray());
	}
	
	public void write(OutputStream os) throws IOException{
		wb.write(os);
	}

	@Override
	public void close() throws IOException {
		wb.close();
	}
}