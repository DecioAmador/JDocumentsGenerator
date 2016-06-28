package com.github.decioamador.jdocsgen.test.table;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.decioamador.jdocsgen.table.TableGenerator;
import com.github.decioamador.jdocsgen.table.TableOptions;
import com.github.decioamador.jdocsgen.test.Model1;
import com.github.decioamador.jdocsgen.test.Model2;
import com.github.decioamador.jdocsgen.test.Model3;
import com.github.decioamador.jdocsgen.translation.TranslatorCollection;

public class TableGeneratorTest {
	
	public static void main(String[] args) throws Exception {
		final String filename = "test.xlsx";
		
		final List<String> columns = Arrays.asList(
				"Referencia","Data","Id","Nome",
				"Guid2","Rotulo","Guid3","Numero"); // PT
		
		final List<String> fields = Arrays.asList(
				"Guid","Date","Id","Name",
				"Model2.Guid","Model2.Label",
				"Model2.Model3.Guid","Model2.Model3.Number");
		
		final TranslatorCollection translator = new TranslatorCollection();
		
		final Set<String> fieldsToTranslate = new HashSet<>();
		fieldsToTranslate.add("Name");
		translator.setFieldsToMap(fieldsToTranslate);
		
		final Map<String,String> mapTrans = new HashMap<>(); // EN to PT
		mapTrans.put("people","pessoas");
		mapTrans.put("life","vida");
		mapTrans.put("time","tempo");
		mapTrans.put("world","mundo");
		translator.setMap(mapTrans);
		
		final List<Model1> objs = new ArrayList<>();
		objs.add(new Model1("guid1", 111L, new Date(), "people", 
				new Model2("guidMD1","label1", new Model3("guidMDMD1", 1))));
		objs.add(new Model1("guid2", 222L, new Date(), "life", 
				new Model2("guidMD2","label2", new Model3("guidMDMD2", 2))));
		objs.add(new Model1("guid3", 333L, new Date(), "world", 
				new Model2("guidMD3","label3", new Model3("guidMDMD3", 3))));
		objs.add(new Model1("guid4", 444L, new Date(), "time", 
				new Model2("guidMD4","label4", new Model3("guidMDMD4", 4))));
		objs.add(new Model1("guid5", 555L, new Date(), "love", 
				new Model2("guidMD5","label5", null)));
		objs.add(new Model1("guid6", 666L, new Date(), "time", null));
		objs.add(null);
		objs.add(new Model1("guid8", 888L, new Date(), "hope", 
				new Model2("guidMD8","label8", new Model3(null, 8))));
		
		final List<Model1> objs2 = new ArrayList<>(objs);
		objs2.remove(0);
		objs2.remove(2);
		objs2.remove(4);
		objs2.add(objs2.get(0));
		objs2.add(objs2.get(2));
		
		final TableOptions options = new TableOptions();
		options.setAutosize(true);
		options.setInitPosRow(2);
		options.setInitPosCol(1);
		
		final Map<String,Format> formats = new HashMap<>();
		formats.put("Date", new SimpleDateFormat("dd/MM/yyyy"));
		translator.setFieldsToFormat(formats);
		
		Workbook wb = new XSSFWorkbook();
		
		try(TableGenerator tg = new TableGenerator(wb);
				OutputStream os = new FileOutputStream(new File(filename))){
			tg.generate("Sheet0", options, objs, columns, fields, translator);
			tg.generate("Sheet1", options, objs2, columns, fields, translator);
			tg.write(os);
		}
	}
}