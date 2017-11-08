package com.github.decioamador.jdocsgen.translation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.decioamador.jdocsgen.model.DataAnimal;
import com.github.decioamador.jdocsgen.table.TableGenerator;

public class TranslatorUtilsTests {

    static Stream<Arguments> translateObjectArguments() {
        final Builder<Arguments> builder = Stream.builder();

        final TableGenerator tg = new TableGenerator(new XSSFWorkbook());
        final TranslatorCollection translator = new TranslatorCollection();
        translator.setRawPrint(new HashSet<String>());
        translator.getRawPrint().add("arg1");
        translator.getRawPrint().add("arg2");
        translator.getRawPrint().add("arg3");
        final TranslatorHelper transHelper = new TranslatorHelper(translator);

        // Arg1 - Collection
        final List<String> objs1 = new ArrayList<>(DataAnimal.getPets1().length);
        for (int i = 0; i < DataAnimal.getPets1().length; i++) {
            if (DataAnimal.getPets1()[i] != null) {
                objs1.add(DataAnimal.getPets1()[i].getName());
            } else {
                objs1.add(null);
            }
        }

        final String expected1 = "Buddy; Duke; Tigger";
        builder.add(Arguments.of(tg, true, "; ", transHelper, objs1, "arg1", expected1));

        // Arg2 - Array
        final Object objs2 = objs1.toArray();
        final String expected2 = "Buddy; Duke; Tigger";
        builder.add(Arguments.of(tg, true, "; ", transHelper, objs2, "arg2", expected2));

        // Arg3 - Object
        final Object obj3 = "Kitty";
        final Object expected3 = "Kitty";

        builder.add(Arguments.of(tg, true, ";", transHelper, obj3, "arg3", expected3));
        builder.add(Arguments.of(tg, false, ";", transHelper, obj3, "arg3", expected3));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("translateObjectArguments")
    public void translateObject(final TableGenerator tg, final boolean agg, final String sep,
            final TranslatorHelper transHelper, final Object o, final String field, final String expected)
            throws Exception {

        final String result = TranslatorUtils.translateObject(agg, sep, transHelper, o, field);
        assertNotNull(tg.getWorkbook());
        assertEquals(expected, result);
    }

}
