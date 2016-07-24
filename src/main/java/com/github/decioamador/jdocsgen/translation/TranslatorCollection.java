package com.github.decioamador.jdocsgen.translation;

import java.text.Format;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Utility class to store all the supports things needed for translation
 * 
 * <br>
 * <br>Copyright 2016 Décio Amador <br>
 * <br>
 * Licensed under the Apache License, Version 2.0 (the "License"); <br>
 * you may not use this file except in compliance with the License. <br>
 * You may obtain a copy of the License at <br>
 * <br>
 *     http://www.apache.org/licenses/LICENSE-2.0 <br>
 * <br>
 * Unless required by applicable law or agreed to in writing, software <br>
 * distributed under the License is distributed on an "AS IS" BASIS, <br>
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. <br>
 * See the License for the specific language governing permissions and <br>
 * limitations under the License. <br>
 * 
 * @author Décio Amador
 * @since 1.1.0.0
 */
public class TranslatorCollection {

	private Set<String> fieldsToMap;
	private Map<String,String> map;
	
	private Set<String> fieldsToProperties;
	private Properties properties; 
	
	private Set<String> fieldsToResources;
	private ResourceBundle resourceBundle;
	
	private Map<String, Format> fieldsToFormat;
	
	public TranslatorCollection(){
		super();
	}

	public TranslatorCollection(Set<String> fieldsToMap, Map<String, String> map, Set<String> fieldsToProperties,
			Properties properties, Set<String> fieldsToResources, ResourceBundle resourceBundle,
			Map<String, Format> fieldsToFormat) {
		super();
		this.fieldsToMap = fieldsToMap;
		this.map = map;
		this.fieldsToProperties = fieldsToProperties;
		this.properties = properties;
		this.fieldsToResources = fieldsToResources;
		this.resourceBundle = resourceBundle;
		this.fieldsToFormat = fieldsToFormat;
	}

	// GET'S and SET'S
	public Set<String> getFieldsToMap() {
		return fieldsToMap;
	}
	public void setFieldsToMap(Set<String> fieldsToMap) {
		this.fieldsToMap = fieldsToMap;
	}
	public Map<String, String> getMap() {
		return map;
	}
	public void setMap(Map<String, String> map) {
		this.map = map;
	}
	public Set<String> getFieldsToProperties() {
		return fieldsToProperties;
	}
	public void setFieldsToProperties(Set<String> fieldsToProperties) {
		this.fieldsToProperties = fieldsToProperties;
	}
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public Set<String> getFieldsToResources() {
		return fieldsToResources;
	}
	public void setFieldsToResources(Set<String> fieldsToResources) {
		this.fieldsToResources = fieldsToResources;
	}
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
	public void setResourceBundle(ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;
	}
	public Map<String, Format> getFieldsToFormat() {
		return fieldsToFormat;
	}
	public void setFieldsToFormat(Map<String, Format> fieldsToFormat) {
		this.fieldsToFormat = fieldsToFormat;
	}
}