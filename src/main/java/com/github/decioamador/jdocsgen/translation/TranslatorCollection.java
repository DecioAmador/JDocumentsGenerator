package com.github.decioamador.jdocsgen.translation;

import java.text.Format;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Utility class to store all the supports things needed for translation
 */
public class TranslatorCollection {

    private Set<String> fieldsToMap;
    private Map<String, String> map;

    private Set<String> rawPrint;
    private Map<String, ResourceBundle> resourceBundleMap;

    private Map<String, Format> fieldsToFormat;
    private Map<String, DateTimeFormatter> datesToFormat;

    // GET'S and SET'S
    public Set<String> getFieldsToMap() {
	return fieldsToMap;
    }

    public void setFieldsToMap(final Set<String> fieldsToMap) {
	this.fieldsToMap = fieldsToMap;
    }

    public Map<String, String> getMap() {
	return map;
    }

    public void setMap(final Map<String, String> map) {
	this.map = map;
    }

    public Set<String> getRawPrint() {
	return rawPrint;
    }

    public void setRawPrint(final Set<String> rawPrint) {
	this.rawPrint = rawPrint;
    }

    public Map<String, ResourceBundle> getResourceBundleMap() {
	return resourceBundleMap;
    }

    public void setResourceBundleMap(final Map<String, ResourceBundle> resourceBundleMap) {
	this.resourceBundleMap = resourceBundleMap;
    }

    public Map<String, Format> getFieldsToFormat() {
	return fieldsToFormat;
    }

    public void setFieldsToFormat(final Map<String, Format> fieldsToFormat) {
	this.fieldsToFormat = fieldsToFormat;
    }

    public Map<String, DateTimeFormatter> getDatesToFormat() {
	return datesToFormat;
    }

    public void setDatesToFormat(final Map<String, DateTimeFormatter> datesToFormat) {
	this.datesToFormat = datesToFormat;
    }
}