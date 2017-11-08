package com.github.decioamador.jdocsgen.text;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.decioamador.jdocsgen.model.DataAnimal;
import com.github.decioamador.jdocsgen.model.animal.Animal;
import com.github.decioamador.jdocsgen.translation.Translator;
import com.github.decioamador.jdocsgen.translation.TranslatorCollection;
import com.github.decioamador.jdocsgen.translation.TranslatorHelper;

public class TextGeneratorTests {

    static Stream<XWPFDocument> getDocuments() {
        final Builder<XWPFDocument> builder = Stream.builder();

        builder.add(new XWPFDocument());

        return builder.build();
    }

    static Stream<Arguments> generateParagraphArguments() {
        final Builder<Arguments> builder = Stream.builder();

        final String[] fields = new String[] { "kingdom", "specie", "weight", "birthdate", "transport" };
        final String[] labels = new String[] { "Kingdom", "Specie", "Weight", "Birthdate", "Transport" };
        final TextOptions options = new TextOptions();
        options.setBetweenLabelAndField(" - ");
        options.setAggregate(true);
        options.setSeperatorAgg("; ");
        final TranslatorCollection collection = new TranslatorCollection();
        collection.setRawPrint(new HashSet<>());
        Collections.addAll(collection.getRawPrint(), fields);
        final Translator translator = new TranslatorHelper(collection);

        getDocuments().forEach((final XWPFDocument doc) -> {
            for (final Animal a : DataAnimal.getAnimals1()) {
                builder.add(Arguments.of(doc, a, options, labels, fields, translator));
            }
        });

        final TextOptions options2 = new TextOptions();
        getDocuments().forEach((final XWPFDocument doc) -> {
            for (final Animal a : DataAnimal.getAnimals1()) {
                builder.add(Arguments.of(doc, a, options2, labels, fields, translator));
            }
        });

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("generateParagraphArguments")
    public void generateParagraph(final XWPFDocument doc, final Object obj, final TextOptions options,
            final String[] labels, final String[] fields, final Translator translator) throws IOException {

        try (TextGenerator tg = new TextGenerator(doc)) {
            assertNotNull(tg.getDocument());
            final XWPFParagraph paragraph = tg.generateParagraph(obj, options, labels, fields, translator);
            if (obj == null) {
                assertTrue(paragraph.getParagraphText().trim().isEmpty());
            } else {
                assertFalse(paragraph.getParagraphText().trim().isEmpty());
            }
        }
    }

    static Stream<Arguments> generateTableArguments() {
        final Builder<Arguments> builder = Stream.builder();

        final String[] fields = new String[] { "kingdom", "specie", "weight", "birthdate", "transport" };
        final String[] labels = new String[] { "Kingdom", "Specie", "Weight", "Birthdate", "Transport" };
        final TextOptions options = new TextOptions();
        options.setBetweenLabelAndField(" - ");
        options.setAggregate(true);
        options.setSeperatorAgg("; ");

        final TranslatorCollection collection = new TranslatorCollection();
        collection.setRawPrint(new HashSet<>());
        Collections.addAll(collection.getRawPrint(), fields);
        final Translator translator = new TranslatorHelper(collection);

        final List<Animal> animals = Arrays.asList(DataAnimal.getAnimals1());

        getDocuments().forEach((final XWPFDocument doc) -> {
            builder.add(Arguments.of(doc, animals, options, labels, fields, translator));
        });

        final TextOptions options2 = new TextOptions();
        getDocuments().forEach((final XWPFDocument doc) -> {
            builder.add(Arguments.of(doc, animals, options2, labels, fields, translator));
        });

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("generateTableArguments")
    public void generateTable(final XWPFDocument doc, final Collection<?> objs, final TextOptions options,
            final String[] labels, final String[] fields, final Translator translator) throws IOException {

        try (TextGenerator tg = new TextGenerator(doc)) {
            assertNotNull(tg.getDocument());
            final XWPFTable table = tg.generateTable(objs, options, labels, fields, translator);

            final int nonNullObjs = (int) objs.stream().filter(Objects::nonNull).count();
            assertEquals(nonNullObjs + 1, table.getNumberOfRows());

            for (int i = 0; i < nonNullObjs + 1; i++) {
                final XWPFTableRow row = table.getRow(i);
                assertNotNull(row);
                for (int j = 0; j < fields.length; j++) {
                    final XWPFTableCell cell = row.getCell(j);
                    assertNotNull(cell);
                }
            }
        }
    }

    static Stream<Arguments> writeArguments() {
        final Builder<Arguments> builder = Stream.builder();

        getDocuments().forEach((final XWPFDocument doc) -> {
            final TextGenerator tg = new TextGenerator(doc);

            final String[] titles = new String[] { "Kingdom", "Specie", "Weight", "Birthdate", "Transport" };
            final String[] fields = new String[] { "kingdom", "specie", "weight", "birthdate", "transport" };
            final List<Animal> objs = Arrays.asList(DataAnimal.getAnimals1());
            final TranslatorCollection transCol = new TranslatorCollection();
            transCol.setRawPrint(new HashSet<>());
            Collections.addAll(transCol.getRawPrint(), fields);
            final Translator translator = new TranslatorHelper(transCol);
            final TextOptions options = new TextOptions();

            tg.generateTable(objs, options, titles, fields, translator);

            builder.add(Arguments.of(tg));
        });

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("writeArguments")
    public void writeInputStream(final TextGenerator tg) throws Exception {
        try {
            assertNotNull(tg.getDocument());
            final InputStream bais = tg.write();
            assertTrue(bais.available() > 50);
            bais.close();
        } finally {
            tg.close();
        }
    }

    @ParameterizedTest
    @MethodSource("writeArguments")
    public void writeOutputStream(final TextGenerator tg) throws Exception {
        try {
            final Path p = Paths.get(String.format("./writeTest%s.xlsx", UUID.randomUUID().toString()));
            final OutputStream os = Files.newOutputStream(p);
            assertNotNull(tg.getDocument());
            assertTrue(Files.size(p) == 0L);
            tg.write(os);
            assertTrue(Files.size(p) > 50L);
            os.close();
            Files.delete(p);
        } finally {
            tg.close();

        }
    }
}
