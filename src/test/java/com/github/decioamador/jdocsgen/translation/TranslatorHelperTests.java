package com.github.decioamador.jdocsgen.translation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.DecimalFormat;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.stream.Stream.Builder;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.github.decioamador.jdocsgen.model.DataAnimal;
import com.github.decioamador.jdocsgen.model.DataProduct;

public class TranslatorHelperTests {

    static Stream<Arguments> getValueArrayArguments() {
        Stream<Arguments> prevArgs = Stream.concat(getTranslatorCollectionValueArguments(), handleFormatArguments());
        prevArgs = Stream.concat(prevArgs, handleDateTimeFormatArguments());
        prevArgs = Stream.concat(prevArgs, handleResourceBundleArguments());
        prevArgs = Stream.concat(prevArgs, handleMapArgumentsWithField());

        final Builder<Arguments> builder = Stream.builder();
        prevArgs.forEach((final Arguments args) -> {
            final Object[] objs = args.get();
            builder.add(Arguments.of(objs[0], objs[1], new Object[] { objs[2] }, ", ", objs[3]));
        });

        final TranslatorHelper transHelper = new TranslatorHelper(new TranslatorCollection());

        // Arg1 - nulls
        builder.add(Arguments.of(null, transHelper, null, null, null));

        // Arg2 - empty
        builder.add(Arguments.of(null, transHelper, new Object[0], null, null));

        // Arg3
        final String field = "arg3";
        final Object[] objs = new Object[DataAnimal.getPets2().length];
        for (int i = 0; i < DataAnimal.getPets2().length; i++) {
            if (DataAnimal.getPets2()[i] != null) {
                objs[i] = DataAnimal.getPets2()[i].getName();
            } else {
                objs[i] = null;
            }
        }
        transHelper.getTranslatorCollection().setRawPrint(new HashSet<>());
        transHelper.getTranslatorCollection().getRawPrint().add(field);
        final String expected1 = "Daisy, Teddy, Rocky";
        builder.add(Arguments.of(field, transHelper, objs, ", ", expected1));

        // Agr4 - Different separator
        final String field2 = "arg4";
        final Object[] objs2 = new Object[DataAnimal.getPets3().length];
        for (int i = 0; i < DataAnimal.getPets3().length; i++) {
            if (DataAnimal.getPets3()[i] != null) {
                objs2[i] = DataAnimal.getPets3()[i].getName();
            } else {
                objs2[i] = null;
            }
        }
        transHelper.getTranslatorCollection().getRawPrint().add(field2);
        final String expected2 = "Oliver;Angel;Kitty";
        builder.add(Arguments.of(field2, transHelper, objs2, ";", expected2));

        // Arg5 - Object with nulls
        final String field3 = "arg5";
        final Object[] objs3 = new String[] { null, "potato", null, "onion" };
        transHelper.getTranslatorCollection().getRawPrint().add(field3);
        final String expected3 = "potato;onion";
        builder.add(Arguments.of(field3, transHelper, objs3, ";", expected3));

        // Arg6 - Object with nulls
        final String field4 = "arg6";
        final Object[] objs4 = new String[] { "potato", null, "onion", null };
        transHelper.getTranslatorCollection().getRawPrint().add(field4);
        final String expected4 = "potato;onion";
        builder.add(Arguments.of(field4, transHelper, objs4, ";", expected4));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("getValueArrayArguments")
    public void getValueArray(final String field, final TranslatorHelper transHelper, final Object[] obj,
            final String separator, final String expected) throws Exception {

        final String result = transHelper.getValue(obj, field, separator);
        assertEquals(expected, result);
    }

    static Stream<Arguments> getValueArguments() {
        final Builder<Arguments> builder = Stream.builder();

        final TranslatorHelper transHelper = new TranslatorHelper(new TranslatorCollection());
        builder.add(Arguments.of(null, transHelper, null, null));
        builder.add(Arguments.of(null, transHelper, UUID.randomUUID().toString(), null));
        builder.add(Arguments.of(UUID.randomUUID().toString(), transHelper, UUID.randomUUID().toString(), null));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource({ "getValueArguments", "getTranslatorCollectionValueArguments", "handleFormatArguments",
            "handleDateTimeFormatArguments", "handleResourceBundleArguments", "handleMapArgumentsWithField" })
    public void getValue(final String field, final TranslatorHelper transHelper, final Object obj,
            final String expected) throws Exception {

        final String result = transHelper.getValue(obj, field);
        assertEquals(expected, result);
    }

    static Stream<Arguments> getTranslatorCollectionValueArguments() {
        final Builder<Arguments> builder = Stream.builder();

        // Arg1 - Empty collections
        final TranslatorCollection trans = new TranslatorCollection();
        trans.setResourceBundleMap(Collections.emptyMap());
        trans.setFieldsToFormat(Collections.emptyMap());
        trans.setDatesToFormat(Collections.emptyMap());
        trans.setFieldsToMap(Collections.emptySet());
        trans.setRawPrint(Collections.emptySet());
        builder.add(Arguments.of(UUID.randomUUID().toString(), new TranslatorHelper(trans),
                UUID.randomUUID().toString(), null));

        // Arg2 - Raw Print
        final TranslatorCollection trans2 = new TranslatorCollection();
        trans2.setRawPrint(new HashSet<>());
        final String field = UUID.randomUUID().toString();
        trans2.getRawPrint().add(field);
        final UUID obj = UUID.randomUUID();
        builder.add(Arguments.of(field, new TranslatorHelper(trans2), obj, obj.toString()));

        // Arg3 - Empty Translator Collection
        final TranslatorCollection trans3 = new TranslatorCollection();
        builder.add(Arguments.of(null, new TranslatorHelper(trans3), null, null));

        // Arg4 - Doesn't contain map key
        final TranslatorCollection trans4 = new TranslatorCollection();
        trans4.setFieldsToMap(Collections.emptySet());
        trans4.setMap(Collections.emptyMap());
        builder.add(Arguments.of(UUID.randomUUID().toString(), new TranslatorHelper(trans4), null, null));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource({ "getTranslatorCollectionValueArguments", "handleFormatArguments", "handleDateTimeFormatArguments",
            "handleResourceBundleArguments" })
    public void getTranslatorCollectionValue(final String field, final TranslatorHelper transHelper, final Object obj,
            final String expected) throws Exception {

        final String result = transHelper.getTranslatorCollectionValue(obj, field);
        assertEquals(expected, result);
    }

    static Stream<Arguments> handleMapArgumentsWithField() {
        final Builder<Arguments> builder = Stream.builder();
        handleMapArguments().forEach((final Arguments arg) -> {
            final Object[] argsArray = arg.get();

            final TranslatorHelper transHelper = (TranslatorHelper) argsArray[0];
            transHelper.getTranslatorCollection().setFieldsToMap(new HashSet<>());

            final String field = UUID.randomUUID().toString();
            transHelper.getTranslatorCollection().getFieldsToMap().add(field);

            builder.add(Arguments.of(field, transHelper, argsArray[1], argsArray[2]));
        });
        return builder.build();
    }

    static Stream<Arguments> handleMapArguments() {
        final Builder<Arguments> builder = Stream.builder();

        final TranslatorCollection translator = new TranslatorCollection();
        translator.setFieldsToMap(new HashSet<>());
        translator.setMap(DataProduct.getTranslatorGrains());
        builder.add(Arguments.of(new TranslatorHelper(translator), "Rice", "Ilayisi"));

        final TranslatorCollection translator2 = new TranslatorCollection();
        translator2.setFieldsToMap(new HashSet<>());
        translator2.setMap(DataProduct.getTranslatorDairies());
        builder.add(Arguments.of(new TranslatorHelper(translator2), "Cheese", "Ushizi"));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("handleMapArguments")
    public void handleMap(final TranslatorHelper transHelper, final Object obj, final String expected)
            throws Exception {

        final String result = transHelper.handleMap(obj);
        assertEquals(expected, result);
    }

    static Stream<Arguments> handleDateTimeFormatArguments() {
        final Builder<Arguments> builder = Stream.builder();

        final TranslatorCollection translator = new TranslatorCollection();
        translator.setDatesToFormat(new HashMap<>());
        final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        translator.getDatesToFormat().put("datetimeformatter", dtf);
        final Object obj1 = LocalDateTime.of(2000, 1, 2, 16, 30);
        final String expected1 = "2000-01-02 16:30";

        builder.add(Arguments.of("datetimeformatter", new TranslatorHelper(translator), obj1, expected1));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("handleDateTimeFormatArguments")
    public void handleDateTimeFormat(final String field, final TranslatorHelper transHelper, final Object obj,
            final String expected) throws Exception {

        final String result = transHelper.handleDateTimeFormat(obj, field);
        assertEquals(expected, result);
    }

    static Stream<Arguments> handleResourceBundleArguments() {
        final Builder<Arguments> builder = Stream.builder();

        final TranslatorCollection translator = new TranslatorCollection();
        translator.setResourceBundleMap(new HashMap<>());
        translator.getResourceBundleMap().put("name", DataProduct.getResourceBundleProductsNames());

        builder.add(Arguments.of("name", new TranslatorHelper(translator), "Strawberry", "Ijikijolo"));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("handleResourceBundleArguments")
    public void handleResourceBundle(final String field, final TranslatorHelper transHelper, final Object obj,
            final String expected) throws Exception {

        final String result = transHelper.handleResourceBundle(obj, field);
        assertEquals(expected, result);
    }

    static Stream<Arguments> handleFormatArguments() {
        final Builder<Arguments> builder = Stream.builder();

        final TranslatorCollection translator = new TranslatorCollection();
        final TranslatorHelper transHelper = new TranslatorHelper(translator);
        translator.setFieldsToFormat(new HashMap<>());

        Format fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        translator.getFieldsToFormat().put("birthdate", fmt);
        final String expected1 = "2001-12-01 15:15";

        builder.add(Arguments.of("birthdate", transHelper, DataAnimal.getAnimals1()[1].getBirthdate(), expected1));

        final NumberFormat nf = DecimalFormat.getCurrencyInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setCurrency(DataProduct.getEuro());

        translator.getFieldsToFormat().put("price", nf);
        final String expected2 = "12,35 €";

        builder.add(Arguments.of("price", transHelper, 12.34568, expected2));

        fmt = new MessageFormat("My favorite vegetable is {0}");
        translator.getFieldsToFormat().put("vegetable", fmt);
        final String expected3 = "My favorite vegetable is potatoes";

        builder.add(Arguments.of("vegetable", transHelper, "potatoes", expected3));

        return builder.build();
    }

    @ParameterizedTest
    @MethodSource("handleFormatArguments")
    public void handleFormat(final String field, final TranslatorHelper transHelper, final Object obj,
            final String expected) throws Exception {

        final String result = transHelper.handleFormat(obj, field);
        assertEquals(expected, result);
    }

}
