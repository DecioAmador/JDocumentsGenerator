package com.github.decioamador.jdocsgen.text;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.github.decioamador.jdocsgen.translation.TranslatorCollection;

public class TextGeneratorDemo {

    public static void main(final String[] args) throws Exception {
        final String filename = "demo.docx";
        final String filename2 = "demo2.docx";

        final List<String> labels = Arrays.asList("Referencia", "Data", "Id", "Nome", "Uuid2", "Rotulo", "Uuid3",
                "Numero"); // PT

        final List<String> fields = Arrays.asList("uuid", "date", "id", "name", "model2.uuid", "model2.label",
                "model2.model3.uuid", "model2.model3.number");

        final TranslatorCollection translator = new TranslatorCollection();

        translator.setMap(new HashMap<>()); // EN to PT
        translator.getMap().put("people", "pessoas");
        translator.getMap().put("life", "vida");
        translator.getMap().put("time", "tempo");
        translator.getMap().put("world", "mundo");

        translator.setFieldsToMap(new HashSet<>());
        translator.getFieldsToMap().add("name");

        translator.setFieldsToFormat(new HashMap<>());
        translator.getFieldsToFormat().put("date", new SimpleDateFormat("yyyy-MM-dd"));

        final TextOptions options = new TextOptions();
        options.setBetweenLabelAndField(" - ");

        XWPFDocument document = new XWPFDocument();
        try (TextGenerator tg = new TextGenerator(document);
                OutputStream os = Files.newOutputStream(Paths.get(filename))) {

            // for (final Object o : objs) {
            // tg.generateParagraph(o, options, labels, fields, translator);
            // }

            tg.write(os);
        }

        document = new XWPFDocument();
        try (TextGenerator tg = new TextGenerator(document);
                OutputStream os = Files.newOutputStream(Paths.get(filename2))) {
            // tg.generateTable(objs, labels, fields, translator);
            tg.write(os);
        }
    }
}