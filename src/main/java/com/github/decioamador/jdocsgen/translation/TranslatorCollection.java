package com.github.decioamador.jdocsgen.translation;

import java.text.Format;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Utility class to store all the supports things needed for translation
 *
 * @since 1.1.0.0
 */
public class TranslatorCollection {

	private Set<String> fieldsToMap;
	private Map<String, String> map;

	private Set<String> fieldsToResources;
	private ResourceBundle resourceBundle;

	private Map<String, Format> fieldsToFormat;
	private Map<String, DateTimeFormatter> datesToFormat;

	public TranslatorCollection(){
		super();
	}

	public TranslatorCollection(final Set<String> fieldsToResources, final ResourceBundle resourceBundle) {
		super();
		this.fieldsToResources = fieldsToResources;
		this.resourceBundle = resourceBundle;
	}

	public TranslatorCollection(final Set<String> fieldsToResources, final ResourceBundle resourceBundle,
			final Map<String, Format> fieldsToFormat) {
		super();
		this.fieldsToResources = fieldsToResources;
		this.resourceBundle = resourceBundle;
		this.fieldsToFormat = fieldsToFormat;
	}

	public TranslatorCollection(final Map<String, String> map,
			final Set<String> fieldsToResources, final ResourceBundle resourceBundle,
			final Map<String, Format> fieldsToFormat, final Map<String, DateTimeFormatter> datesToFormat) {
		super();
		this.map = map;
		this.fieldsToResources = fieldsToResources;
		this.resourceBundle = resourceBundle;
		this.fieldsToFormat = fieldsToFormat;
		this.datesToFormat = datesToFormat;
	}

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
	public Set<String> getFieldsToResources() {
		return fieldsToResources;
	}
	public void setFieldsToResources(final Set<String> fieldsToResources) {
		this.fieldsToResources = fieldsToResources;
	}
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	public void setResourceBundle(final ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
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