package com.github.decioamador.jdocsgen.translation;

/**
 * High level translator, used to convert an object into its representation<br>
 * You can use the class {@link TranslatorHelper}.
 */
public interface Translator {

    /**
     * It will be used when the field result isn't a collection or an array
     *
     * @param obj
     *            A object
     * @param field
     *            A string that represents a field
     * @return Returns the representation of the object
     */
    String getValue(final Object obj, final String field);

    /**
     * It will be used when the field result is a collection or an array
     *
     * @param objs
     *            An array of objects
     * @param field
     *            A string that represents a field
     * @param sep
     *            the string that will separate each on of the objects
     * @return Returns the representation of a group of objects
     */
    String getValue(final Object[] objs, final String field, final String sep);

}
