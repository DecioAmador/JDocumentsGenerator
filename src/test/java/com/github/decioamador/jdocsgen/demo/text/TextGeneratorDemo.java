package com.github.decioamador.jdocsgen.demo.text;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.github.decioamador.jdocsgen.model.Model1;
import com.github.decioamador.jdocsgen.model.Model2;
import com.github.decioamador.jdocsgen.model.Model3;
import com.github.decioamador.jdocsgen.text.TextGenerator;
import com.github.decioamador.jdocsgen.text.TextOptions;
import com.github.decioamador.jdocsgen.translation.TranslatorCollection;

public class TextGeneratorDemo {

	public static void main(final String[] args) throws Exception {
		final String filename = "test.docx";
		final String filename2 = "test2.docx";

		final List<String> labels = Arrays.asList(
				"Referencia","Data","Id","Nome",
				"Uuid2","Rotulo","Uuid3","Numero"); // PT

		final List<String> fields = Arrays.asList(
				"uuid","date","id","name",
				"model2.uuid","model2.label",
				"model2.model3.uuid","model2.model3.number");

		final TranslatorCollection translator = new TranslatorCollection();

		translator.setMap(new HashMap<>()); // EN to PT
		translator.getMap().put("people","pessoas");
		translator.getMap().put("life","vida");
		translator.getMap().put("time","tempo");
		translator.getMap().put("world","mundo");

		translator.setFieldsToMap(new HashSet<>());
		translator.getFieldsToMap().add("name");

		translator.setFieldsToFormat(new HashMap<>());
		translator.getFieldsToFormat().put("date", new SimpleDateFormat("yyyy-MM-dd"));

		final List<Model1> objs = new ArrayList<>();
		Model1 obj = new Model1(UUID.randomUUID().toString(), 111L, new Date(), "people",
				new Model2(UUID.randomUUID().toString(),"label1", new Model3(UUID.randomUUID().toString(), 1)));
		objs.add(obj);
		obj = new Model1(UUID.randomUUID().toString(), 222L, new Date(), "time", null);
		objs.add(obj);
		obj = new Model1(UUID.randomUUID().toString(), 333L, new Date(), "life",
				new Model2(UUID.randomUUID().toString(),"label3", new Model3(UUID.randomUUID().toString(), 3)));
		objs.add(obj);
		obj = new Model1(UUID.randomUUID().toString(), 444L, null, "hope",
				new Model2(UUID.randomUUID().toString(),"label4", new Model3(null, 4)));
		objs.add(obj);

		final TextOptions options = new TextOptions();
		options.setBetweenLabelAndField(" - ");

		XWPFDocument document = new XWPFDocument();
		try(TextGenerator tg = new TextGenerator(document);
				OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(filename)))){

			for(final Object o : objs){
				tg.generateParagraph(o, options, labels, fields, translator);
			}

			tg.write(os);
		}

		document = new XWPFDocument();
		try(TextGenerator tg = new TextGenerator(document);
				OutputStream os = new BufferedOutputStream(new FileOutputStream(new File(filename2)))){
			tg.generateTable(objs, labels, fields, translator);
			tg.write(os);
		}
	}
}