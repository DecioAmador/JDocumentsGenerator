package com.github.decioamador.jdocsgen.table.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.github.decioamador.jdocsgen.table.TableGenerator;
import com.github.decioamador.jdocsgen.table.TableOption;

public class Test {

	public static void main(String [] args){
		
		List<String> columns = Arrays.asList(
				"Referencia","Data","Id","Nome",
				"Guid2","Label","Guid3","Number");
		
		List<String> fields = Arrays.asList(
				"Guid","Date","Id","Name","Model2.Guid",
				"Model2.Label","Model2.Model3.Guid","Model2.Model3.Number");
		
		Set<String> fieldsToTranslate = new HashSet<>();
		fieldsToTranslate.add("Name");
		
		Map<String,String> translator = new HashMap<>(); // EN to PT
		translator.put("people","pessoas");
		translator.put("life","vida");
		translator.put("date","data");
		translator.put("time","tempo");
		
		List<Model1> objs = new ArrayList<>();
		objs.add(new Model1("guid1", 111L, new Date(), "people", 
				new Model2("guidMD1","label1", new Model3("guidMDMD1", 1))));
		objs.add(new Model1("guid2", 222L, new Date(), "life", 
				new Model2("guidMD2","label2", new Model3("guidMDMD2", 2))));
		objs.add(new Model1("guid3", 333L, new Date(), "date", 
				new Model2("guidMD3","label3", new Model3("guidMDMD3", 3))));
		objs.add(new Model1("guid4", 444L, new Date(), "time", 
				new Model2("guidMD4","label4", new Model3("guidMDMD4", 4))));
		objs.add(new Model1("guid5", 555L, new Date(), "time", 
				new Model2("guidMD5","label5", null)));
		objs.add(new Model1("guid6", 666L, new Date(), "time", null));
		objs.add(null);
		objs.add(new Model1("guid8", 888L, new Date(), "time", 
				new Model2("guidMD8","label8", new Model3(null, 8))));
		
		Map<TableOption, Object> options = new HashMap<>();
		options.put(TableOption.AUTOSIZE_COLUMNS, true);
		options.put(TableOption.INICIAL_POSITION_ROW, 2);
		options.put(TableOption.INICIAL_POSITION_COLUMN, 1);
		options.put(TableOption.DATE_FORMAT, "dd/MM/yyyy");
		
		try(TableGenerator eg = new TableGenerator(options);
				OutputStream os = new FileOutputStream(new File("test.xls"))){
			eg.generate(objs, columns, fields, fieldsToTranslate, translator);
			eg.write(os);
		} catch (Exception e) {
			System.out.println("Generation Error.");
		}
	}
	
}
