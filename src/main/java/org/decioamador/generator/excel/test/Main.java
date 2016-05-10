package org.decioamador.generator.excel.test;

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

import org.decioamador.generator.excel.ExcelGenerator;
import org.decioamador.generator.excel.test.model.Model1;
import org.decioamador.generator.excel.test.model.Model2;
import org.decioamador.generator.excel.test.model.Model3;

public class Main {

	public static void main(String [] args) throws Exception{
		
		OutputStream os = new FileOutputStream(new File("test.xls"));
		
		List<String> columns = Arrays.asList(
				"Referencia","Criado em","Id","Nome",
				"Guid2","Label","Guid3","Number");
		
		List<String> fields = Arrays.asList(
				"Guid","CreatedAt","Id","Name","Model2.Guid",
				"Model2.Label","Model2.Model3.Guid","Model2.Model3.Number");
		
		Set<String> fieldsToTranslate = new HashSet<>();
		fieldsToTranslate.add("Name");
		
		Map<String,String> translator = new HashMap<>();
		translator.put("people","pessoas");
		translator.put("life","vida");
		translator.put("date","data");
		translator.put("time","tempo");
		
		List<Model1> objs = new ArrayList<>();
		objs.add(new Model1("guid1", 1L, new Date(), "people", 
				new Model2("guidMD1","label1", new Model3("guidMDMD1", 1))));
		objs.add(new Model1("guid2", 2L, new Date(), "life", 
				new Model2("guidMD2","label2", new Model3("guidMDMD2", 2))));
		objs.add(new Model1("guid3", 3L, new Date(), "date", 
				new Model2("guidMD3","label3", new Model3("guidMDMD3", 3))));
		objs.add(new Model1("guid4", 4L, new Date(), "time", 
				new Model2("guidMD4","label4", new Model3("guidMDMD4", 4))));
		
		try(ExcelGenerator eg = new ExcelGenerator()){
			eg.generate(objs, columns, fields, fieldsToTranslate, translator);
			eg.write(os);
		} catch (Exception e) {
			System.out.println("Trace: "+e.getMessage());
		}
	}
	
}
