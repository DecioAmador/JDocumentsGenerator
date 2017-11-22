package com.github.decioamador.jdocsgen.text;

import java.io.File;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.apache.poi.xwpf.usermodel.XWPFDocument;

import com.github.decioamador.jdocsgen.model.DataAnimal;
import com.github.decioamador.jdocsgen.model.animal.Animal;
import com.github.decioamador.jdocsgen.translation.Translator;
import com.github.decioamador.jdocsgen.translation.TranslatorCollection;
import com.github.decioamador.jdocsgen.translation.TranslatorHelper;

public class TextGeneratorDemo {

    public static void main(final String[] args) throws Exception {

        // Create document
        final XWPFDocument document = new XWPFDocument();

        // Titles / Labels
        final String[] titles = new String[] { "Kingdom", "Specie", "Weight", "Birthdate", "Transport" };

        // Fields
        final String[] fields = new String[] { "kingdom", "specie", "weight", "birthdate", "transport" };

        // Animals
        final List<Animal> objs = Arrays.asList(DataAnimal.getAnimals1());

        // Translator
        final TranslatorCollection transCol = new TranslatorCollection();

        // Prints by calling toString
        transCol.setRawPrint(new HashSet<>());
        Collections.addAll(transCol.getRawPrint(), "kingdom", "specie");

        // Formats the birth date
        transCol.setFieldsToFormat(new HashMap<>());
        transCol.getFieldsToFormat().put("birthdate", new SimpleDateFormat("dd/MM/yyyy"));

        // Formats the weight
        final NumberFormat nf = DecimalFormat.getInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        transCol.getFieldsToFormat().put("weight", nf);

        // Translate into a more legible text
        transCol.setFieldsToMap(new HashSet<>());
        transCol.getFieldsToMap().add("transport");
        transCol.setMap(new HashMap<>());
        transCol.getMap().put("TERRESTRIAL", "Terrestrial");
        transCol.getMap().put("AQUATIC", "Aquatic");
        transCol.getMap().put("AIR", "Air");

        final Translator translator = new TranslatorHelper(transCol);

        // Options
        final TextOptions options = new TextOptions();
        options.setBetweenLabelAndField(" - ");
        options.setSeperatorAgg("; ");
        options.setAggregate(true);

        // Generate text based documents
        try (TextGenerator tg = new TextGenerator(document);
                OutputStream os = Files.newOutputStream(Paths.get(String.format(".%cdemo.docx", File.separatorChar)))) {

            // Generate table (each line represent each object)
            tg.generateTable(objs, options, titles, fields, translator);

            // Generate paragraphs (each paragraph represent each object)
            for (final Object o : objs) {
                tg.generateParagraph(o, options, titles, fields, translator);
            }

            // Write document
            tg.write(os);
        }
    }
}
