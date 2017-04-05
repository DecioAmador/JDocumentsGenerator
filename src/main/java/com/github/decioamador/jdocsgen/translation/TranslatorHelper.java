package com.github.decioamador.jdocsgen.translation;

import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.MissingResourceException;

import com.github.decioamador.jdocsgen.utils.Constants;

/**
 * Helper class to translate fields
 * @since 1.1.0.0
 */
public class TranslatorHelper {

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
	 * @since 1.1.0.0
	 */
	public static String getValue(final Object obj, final String field, final TranslatorCollection trans) {
		String result = null;

		if (obj != null && field != null) {
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
	 * @since 1.1.0.0
	 */
	private static String getTranslatorCollectionValue(final Object obj, final String field, final TranslatorCollection trans) {
		Object temp = obj;
		String result = null;

		if(trans != null){
			if (trans.getFieldsToResources() != null && trans.getResourceBundle() != null
					&& trans.getFieldsToResources().contains(field)) {
				try {
					result = trans.getResourceBundle().getString(temp.toString());
				} catch (MissingResourceException | ClassCastException e) {}

			} else if (trans.getFieldsToFormat() != null && trans.getFieldsToFormat().keySet().contains(field)) {
				if(temp instanceof Calendar){
					temp = ((Calendar) temp).getTime();
				}
				result = trans.getFieldsToFormat().get(field).format(temp);

			} else if (trans.getDatesToFormat() != null && trans.getDatesToFormat().keySet().contains(field)) {
				result = trans.getDatesToFormat().get(field).format((TemporalAccessor) temp);

			} else if (trans.getFieldsToMap() != null && trans.getMap() != null && trans.getFieldsToMap().contains(field)) {
				result = trans.getMap().get(temp.toString());
			}
		}

		return result;
	}

}