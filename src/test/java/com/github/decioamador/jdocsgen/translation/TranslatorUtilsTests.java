package com.github.decioamador.jdocsgen.translation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
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
        final TranslatorCollection transCol = new TranslatorCollection();
        transCol.setRawPrint(new HashSet<String>());
        transCol.getRawPrint().add("arg1");
        transCol.getRawPrint().add("arg2");
        transCol.getRawPrint().add("arg3");
        final Translator translator = new TranslatorHelper(transCol);

        // Arg1 - Collection
        final List<Object> objs1 = Arrays.stream(DataAnimal.getPets1()).map(p -> p != null ? p.getName() : p)
                .collect(Collectors.toList());

        final String expected1 = "Buddy; Duke; Tigger";
        builder.add(Arguments.of(tg, true, "; ", translator, objs1, "arg1", expected1));

        // Arg2 - Array
        final Object objs2 = objs1.toArray();
        final String expected2 = "Buddy; Duke; Tigger";
        builder.add(Arguments.of(tg, true, "; ", translator, objs2, "arg2", expected2));

        // Arg3 - Object
        final Object obj3 = "Kitty";
        final Object expected3 = "Kitty";

        builder.add(Arguments.of(tg, true, ";", translator, obj3, "arg3", expected3));
        builder.add(Arguments.of(tg, false, ";", translator, obj3, "arg3", expected3));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("translateObjectArguments")
    public void translateObject(final TableGenerator tg, final boolean agg, final String sep,
            final Translator translator, final Object o, final String field, final String expected)
                    throws Exception {

        final String result = TranslatorUtils.translateObject(agg, sep, translator, o, field);
        assertNotNull(tg.getWorkbook());
        assertEquals(expected, result);
    }

}
