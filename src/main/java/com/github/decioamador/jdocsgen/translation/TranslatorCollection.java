package com.github.decioamador.jdocsgen.translation;

import java.text.Format;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Utility class to store all the supports things needed for translation or
 * format
 */
public class TranslatorCollection {

    private Set<String> fieldsToMap;
    private Map<String, String> map;

    private Set<String> rawPrint;

    private Map<String, ResourceBundle> resourceBundleMap;

    private Map<String, Format> fieldsToFormat;
    private Map<String, DateTimeFormatter> datesToFormat;

    /**
     * The fields that you want to use on the map
     *
     * @return A set of strings that each element should be an EL representation of
     *         a field
     */
    public Set<String> getFieldsToMap() {
        return fieldsToMap;
    }

    /**
     * The fields that you want to use on the map
     *
     * @param fieldsToMap
     *            A set of strings that each element should be an EL representation
     *            of a field
     */
    public void setFieldsToMap(final Set<String> fieldsToMap) {
        this.fieldsToMap = fieldsToMap;
    }

    /**
     * Used when the value of the object is a key of a map
     *
     * @return A Map that will be used to resolve the field specified on
     *         {@link TranslatorCollection#getFieldsToMap()}
     */
    public Map<String, String> getMap() {
        return map;
    }

    /**
     * Used when the value of the object is a key of a map
     *
     * @param map
     *            A Map that will be used to resolve the field specified on
     *            {@link TranslatorCollection#getFieldsToMap()}
     */
    public void setMap(final Map<String, String> map) {
        this.map = map;
    }

    /**
     * Fields that you want to call {@link Object#toString()}
     *
     * @return A set of string that represent fields
     */
    public Set<String> getRawPrint() {
        return rawPrint;
    }

    /**
     * Fields that you want to call {@link Object#toString()}
     *
     * @param rawPrint
     *            A set of string that represent fields
     */
    public void setRawPrint(final Set<String> rawPrint) {
        this.rawPrint = rawPrint;
    }

    /**
     * Used when the value of the field is a key for a resourceBundle
     *
     * @return A Map that the key is a string representation of a field an the
     *         respective {@linkplain ResourceBundle} that you want to use
     */
    public Map<String, ResourceBundle> getResourceBundleMap() {
        return resourceBundleMap;
    }

    /**
     * Used when the value of the field is a key for a resourceBundle
     *
     * @param resourceBundleMap
     *            A Map that the key is a string representation of a field an the
     *            respective {@linkplain ResourceBundle} that you want to use
     */
    public void setResourceBundleMap(final Map<String, ResourceBundle> resourceBundleMap) {
        this.resourceBundleMap = resourceBundleMap;
    }

    /**
     * Used to format field (Dates, Currency, Numbers or Messages)
     *
     * @return A Map that the key is a string representation of a field an the
     *         respective {@linkplain Format} that you want to use
     */
    public Map<String, Format> getFieldsToFormat() {
        return fieldsToFormat;
    }

    /**
     * Used to format field (Dates, Currency, Numbers or Messages)
     *
     * @param fieldsToFormat
     *            A Map that the key is a string representation of a field an the
     *            respective {@linkplain Format} that you want to use
     */
    public void setFieldsToFormat(final Map<String, Format> fieldsToFormat) {
        this.fieldsToFormat = fieldsToFormat;
    }

    /**
     * Used to format {@link TemporalAccessor} fields
     *
     * @return A Map that the key is a string representation of a field an the
     *         respective {@linkplain DateTimeFormatter} that you want to use
     */
    public Map<String, DateTimeFormatter> getDatesToFormat() {
        return datesToFormat;
    }

    /**
     * Used to format {@link TemporalAccessor} fields
     *
     * @param datesToFormat
     *            A Map that the key is a string representation of a field an the
     *            respective {@linkplain DateTimeFormatter} that you want to use
     */
    public void setDatesToFormat(final Map<String, DateTimeFormatter> datesToFormat) {
        this.datesToFormat = datesToFormat;
    }

}