package com.github.decioamador.jdocsgen.translation;

import java.util.Collection;

/**
 * This class is intended to assist {@link Translator}
 */
public final class TranslatorUtils {

    private TranslatorUtils() {
        throw new UnsupportedOperationException(
                "It isn't supposed to create an instance of this class. This is an utility class");
    }

    /**
     * Translate an object to a {@link String}
     *
     * @param agg
     *            If you want or not to aggregate
     * @param sep
     *            Separator used to aggregate each value
     * @param translator
     *            Translator used to resolve fields
     * @param agg
     *            Wherever you want to aggregate fields
     * @param o
     *            Object that you want to resolve
     * @param field
     *            Field used
     * @return The translated value of an object
     */
    public static String translateObject(final boolean agg, final String sep, final Translator translator,
            final Object o, final String field) {
        String translated;
        Object temp = o;
        if (agg && (temp instanceof Collection || temp.getClass().isArray())) {
            if (o instanceof Collection) {
                temp = ((Collection<?>) o).toArray();
            }

            translated = translator.getValue((Object[]) temp, field, sep);
        } else {
            translated = translator.getValue(temp, field);
        }
        return translated;
    }

}
