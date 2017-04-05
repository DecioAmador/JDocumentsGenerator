package com.github.decioamador.jdocsgen.demo.table;

import java.io.BufferedOutputStream;
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
import java.util.UUID;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.github.decioamador.jdocsgen.table.TableGenerator;
import com.github.decioamador.jdocsgen.table.TableOptions;
import com.github.decioamador.jdocsgen.test.Model1;
import com.github.decioamador.jdocsgen.test.Model2;
import com.github.decioamador.jdocsgen.test.Model3;
import com.github.decioamador.jdocsgen.translation.TranslatorCollection;

public class TableGeneratorDemo {

	public static void main(final String[] args) throws Exception {
		final String filename = "test.xlsx";

		final List<String> columns = Arrays.asList(
				"Referencia","Data","Id","Nome",
				"Uuid2","Rotulo","Uuid3","Numero"); // PT

		final List<String> fields = Arrays.asList(
				"uuid","date","id","name",
				"model2.uuid","model2.label",
				"model2.model3.uuid","model2.model3.number");

		final TranslatorCollection translator = new TranslatorCollection();

		final Set<String> fieldsToTranslate = new HashSet<>();
		fieldsToTranslate.add("name");
		translator.setFieldsToMap(fieldsToTranslate);

		final Map<String,String> mapTrans = new HashMap<>(); // EN to PT
		mapTrans.put("people","pessoas");
		mapTrans.put("life","vida");
		mapTrans.put("time","tempo");
		mapTrans.put("world","mundo");
		translator.setMap(mapTrans);

		final List<Model1> objs = new ArrayList<>();
		objs.add(new Model1(UUID.randomUUID().toString(), 111L, new Date(), "people",
				new Model2(UUID.randomUUID().toString(),"label1", new Model3(UUID.randomUUID().toString(), 1))));
		objs.add(new Model1(UUID.randomUUID().toString(), 222L, new Date(), "life",
				new Model2(UUID.randomUUID().toString(),"label2", new Model3(UUID.randomUUID().toString(), 2))));
		objs.add(new Model1(UUID.randomUUID().toString(), 333L, new Date(), "world",
				new Model2(UUID.randomUUID().toString(),"label3", new Model3(UUID.randomUUID().toString(), 3))));
		objs.add(new Model1(UUID.randomUUID().toString(), 444L, new Date(), "time",
				new Model2(UUID.randomUUID().toString(),"label4", new Model3(UUID.randomUUID().toString(), 4))));
		objs.add(new Model1(UUID.randomUUID().toString(), 555L, new Date(), "love",
				new Model2(UUID.randomUUID().toString(),"label5", null)));
		objs.add(new Model1(UUID.randomUUID().toString(), 666L, new Date(), "time", null));
		objs.add(null);
		objs.add(new Model1(UUID.randomUUID().toString(), 888L, new Date(), "hope",
				new Model2(UUID.randomUUID().toString(),"label8", new Model3(null, 8))));

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
		formats.put("date", new SimpleDateFormat("yyyy-MM-dd"));
		translator.setFieldsToFormat(formats);

		final Workbook wb = new XSSFWorkbook();

		try(TableGenerator tg = new TableGenerator(wb);
				OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(filename)))){
			tg.generateSheet("Sheet Name 1", options, objs, columns, fields, translator);
			tg.generateSheet("Sheet Name 2", options, objs2, columns, fields, translator);
			tg.write(os);
		}
	}
}