package com.github.decioamador.jdocsgen.test.text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.github.decioamador.jdocsgen.test.Model1;
import com.github.decioamador.jdocsgen.test.Model2;
import com.github.decioamador.jdocsgen.test.Model3;
import com.github.decioamador.jdocsgen.text.TextGenerator;
import com.github.decioamador.jdocsgen.text.TextOptions;
import com.github.decioamador.jdocsgen.translation.TranslatorCollection;

public class TextGeneratorTest {

	public static void main(String[] args) throws Exception {
		final String filename = "test.docx";
		
		final List<String> labels = Arrays.asList(
				"Referencia","Data","Id","Nome",
				"Guid2","Rotulo","Guid3","Numero"); // PT
		
		final List<String> fields = Arrays.asList(
				"Guid","Date","Id","Name",
				"Model2.Guid","Model2.Label",
				"Model2.Model3.Guid","Model2.Model3.Number");
		
		final TranslatorCollection translator = new TranslatorCollection();
		
		final Map<String,String> fieldsToMap = new HashMap<>(); // EN to PT
		fieldsToMap.put("people","pessoas");
		fieldsToMap.put("life","vida");
		fieldsToMap.put("time","tempo");
		fieldsToMap.put("world","mundo");
		translator.setMap(fieldsToMap);
		
		final Set<String> map = new HashSet<>();
		map.add("Name");
		translator.setFieldsToMap(map);
		
		final Model1 obj = new Model1("guid1", 111L, new Date(), "people", 
				new Model2("guidMD1","label1", new Model3("guidMDMD1", 1)));
		final Model1 obj2 = new Model1("guid2", 222L, new Date(), "time", null);
		final Model1 obj3 = new Model1("guid3", 333L, new Date(), "life", 
				new Model2("guidMD3","label3", new Model3("guidMDMD3", 3)));
		final Model1 obj4 = new Model1("guid4", 444L, new Date(), "hope", 
				new Model2("guidMD4","label4", new Model3(null, 4)));
		
		final TextOptions options = new TextOptions();
		options.setBetweenLabelAndField(" - ");
		
		final Map<String,Format> formats = new HashMap<>();
		formats.put("Date", new SimpleDateFormat("dd/MM/yyyy"));
		translator.setFieldsToFormat(formats);
		
		final XWPFDocument document = new XWPFDocument();
		
		try(TextGenerator tg = new TextGenerator(document); 
				OutputStream os = new FileOutputStream(new File(filename))){
			tg.generate(obj, options, labels, fields, translator);
			tg.generate(obj2, options, labels, fields, translator);
			tg.generate(obj3, options, labels, fields, translator);
			tg.generate(obj4, options, labels, fields, translator);
			tg.write(os);
		}
	}
}