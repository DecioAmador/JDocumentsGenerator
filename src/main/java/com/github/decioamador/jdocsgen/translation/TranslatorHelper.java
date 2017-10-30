package com.github.decioamador.jdocsgen.translation;

import java.text.Format;
import java.text.MessageFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * Helper class to translate fields
 */
public class TranslatorHelper implements Translator {

    private final TranslatorCollection translatorCollection;

    public TranslatorHelper(final TranslatorCollection translatorCollection) {
        this.translatorCollection = Objects.requireNonNull(translatorCollection);
    }

    /**
     * This methods makes the textual representation of a value
     *
     * @param obj
     *            Object to process
     * @param field
     *            Field to process
     * @param sep
     *            Separator
     * @return the representation value of the object
     */
    @Override
    public String getValue(final Object[] objs, final String field, final String sep) {
        boolean nonFirstElem = false;
        String result = null;
        String value;

        if (objs != null && objs.length != 0) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < objs.length; i++) {
                value = getValue(objs[i], field);
                if (value != null) {
                    if (nonFirstElem) {
                        sb.append(sep);
                    }
                    sb.append(value);
                    nonFirstElem = true;
                }
            }

            if (sb.length() != 0) {
                result = sb.toString();
            }
        }
        return result;
    }

    /**
     * This methods makes the textual representation of a value
     *
     * @param obj
     *            Object to process
     * @param field
     *            Field to process
     * @return the representation value of the object
     */
    @Override
    public String getValue(final Object obj, final String field) {
        String result = null;

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
     *            Field to process
     * @return the representation of the object
     */
    protected String getTranslatorCollectionValue(final Object obj, final String field) {
        String result = null;

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
     *            field like an EL path being used to resolve an object
     * @param trans
     *            translator collection
     * @return the representation of the object
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
     *            field like an EL path being used to resolve an object
     * @param obj
     *            Object that shouldn't be null
     * @return the representation of the object
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
     * @return translator collection being used
     */
    public TranslatorCollection getTranslatorCollection() {
        return translatorCollection;
    }

}