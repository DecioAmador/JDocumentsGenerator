package com.github.decioamador.jdocsgen.translation;

import java.text.Format;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Helper class to translate fields
 */
public class TranslatorHelper implements Translator {

    private final TranslatorCollection translatorCollection;

    public TranslatorHelper(final TranslatorCollection translatorCollection) {
        this.translatorCollection = Objects.requireNonNull(translatorCollection);
    }

    /**
     * {@inheritDoc}
     *
     * @return The representation of the value or null in case it doesn't have any
     */
    @Override
    public String getValue(final Object[] objs, final String field, final String sep) {
        String result = "";

        if (objs != null && objs.length != 0) {
            final Stream<Object> stream = Arrays.stream(objs);

            result = stream.map(o -> getValue(o, field)).filter(str -> str != null && !str.isEmpty()).distinct()
                    .collect(Collectors.joining(sep));
        }

        return result;
    }

    /**
     * {@inheritDoc}
     *
     * @return The representation of the value or null in case it doesn't have any
     */
    @Override
    public String getValue(final Object obj, final String field) {
        String result = "";

        if (obj != null && field != null) {
            result = getTranslatorCollectionValue(obj, field);
        }

        return result;
    }

    /**
     * It process a TranslatorCollection
     *
     * @param obj
     *            Object to process
     * @param field
     *            Field that is used as an id
     * @return The representation of the object
     */
    protected String getTranslatorCollectionValue(final Object obj, final String field) {
        String result = "";

        if (translatorCollection.getRawPrint() != null && translatorCollection.getRawPrint().contains(field)) {
            result = obj.toString();

        } else if (translatorCollection.getResourceBundleMap() != null
                && translatorCollection.getResourceBundleMap().containsKey(field)) {
            result = handleResourceBundle(obj, field);

        } else if (translatorCollection.getFieldsToFormat() != null
                && translatorCollection.getFieldsToFormat().containsKey(field)) {
            result = handleFormat(obj, field);

        } else if (translatorCollection.getDatesToFormat() != null
                && translatorCollection.getDatesToFormat().containsKey(field)) {
            result = handleDateTimeFormat(obj, field);

        } else if (translatorCollection.getFieldsToMap() != null && translatorCollection.getMap() != null
                && translatorCollection.getFieldsToMap().contains(field)) {
            result = handleMap(obj);
        }

        return result;
    }

    /**
     * Transform an object into a translated string using a {@link Map}
     *
     * @param obj
     *            Object that shouldn't be null
     * @return the representation of the object
     */
    protected String handleMap(final Object obj) {
        final Map<String, String> map = translatorCollection.getMap();
        return map.get(obj.toString());
    }

    /**
     * Transform an object that is a {@link TemporalAccessor} into a formated date
     * using a {@link DateTimeFormatter}
     *
     * @param obj
     *            Object that must be also a {@link TemporalAccessor}
     * @param field
     *            Field like an EL path being used to resolve an object
     * @return The representation of the object
     */
    protected String handleDateTimeFormat(final Object obj, final String field) {
        final DateTimeFormatter dateTimeFormatter = translatorCollection.getDatesToFormat().get(field);
        return dateTimeFormatter.format((TemporalAccessor) obj);
    }

    /**
     * Transform an object into a translated string using a {@link ResourceBundle}
     *
     * @param obj
     *            Object that shouldn't be null
     * @param field
     *            field like an EL path being used to resolve an object
     * @return the representation of the object
     */
    protected String handleResourceBundle(final Object obj, final String field) {
        final ResourceBundle bundle = translatorCollection.getResourceBundleMap().get(field);
        return bundle.getString(obj.toString());
    }

    /**
     * Transform an object into a formated string using a {@link Format}
     *
     * @param field
     *            Field like an EL path being used to resolve an object
     * @param obj
     *            Object that shouldn't be null
     * @return The representation of the object
     */
    protected String handleFormat(final Object obj, final String field) {
        String result;
        Object temp = obj;
        final Format format = translatorCollection.getFieldsToFormat().get(field);

        if (temp instanceof Calendar) {
            temp = ((Calendar) temp).getTime();
        }

        if (format instanceof MessageFormat) {
            result = MessageFormat.format(((MessageFormat) format).toPattern(), obj);
        } else {
            result = format.format(temp);
        }

        return result;
    }

    /**
     * TranslatorCollection that is wrapped in this instance of the
     * {@link TranslatorHelper}
     *
     * @return Translator collection being used
     */
    public TranslatorCollection getTranslatorCollection() {
        return translatorCollection;
    }

}