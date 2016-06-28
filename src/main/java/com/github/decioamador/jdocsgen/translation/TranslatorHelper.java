package com.github.decioamador.jdocsgen.translation;

import java.util.MissingResourceException;

import com.github.decioamador.jdocsgen.Constants;

/**
 * Helper class to translate fields
 * 
 * @author Décio Amador
 * @since 1.1.0.0
 */
public final class TranslatorHelper {

	private TranslatorHelper() {
		// It isn't supposed to create an instance of this class
	}
	
	/**
	 * This methods makes the textual representation of a value
	 * 
	 * @param obj
	 *            Object to process
	 * @param field
	 *            Field to process
	 * @param trans
	 *            TranslatorCollection being used
	 * @return the representation value of the object
	 */
	public static String getValue(final Object obj, final String field, final TranslatorCollection trans) {
		String result = null;
		if (obj != null && field != null && !field.isEmpty()) {
			if (trans != null) {
				result = getTranslatorCollectionValue(obj, field, trans);
			}
			if (result == null) {
				result = obj.toString();
			}
		} else {
			result = Constants.EMPTY_STRING;
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
	 * @param trans
	 *            translator being used
	 * @return the representation of the object
	 */
	private static String getTranslatorCollectionValue(final Object obj, final String field,
			final TranslatorCollection trans) {
		String result = null;
		if (trans.getFieldsToMap() != null && trans.getMap() != null && trans.getFieldsToMap().contains(field)) {
			result = trans.getMap().get(obj.toString());
		} else if (trans.getFieldsToProperties() != null && trans.getProperties() != null
				&& trans.getFieldsToProperties().contains(field)) {
			result = trans.getProperties().getProperty(obj.toString());
		} else if (trans.getFieldsToResources() != null && trans.getResourceBundle() != null
				&& trans.getFieldsToResources().contains(field)) {
			try {
				result = trans.getResourceBundle().getString(obj.toString());
			} catch (MissingResourceException | ClassCastException e) {}
		} else if (trans.getFieldsToFormat() != null && trans.getFieldsToFormat().keySet().contains(field)) {
			result = trans.getFieldsToFormat().get(field).format(obj.toString());
		}
		return result;
	}

}