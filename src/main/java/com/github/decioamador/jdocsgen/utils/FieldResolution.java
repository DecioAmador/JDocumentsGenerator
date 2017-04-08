package com.github.decioamador.jdocsgen.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.github.decioamador.jdocsgen.JDocsGenException;

/**
 * This class has the purpose of generating text
 *
 * @since 1.1.0.0
 */
public class FieldResolution {

	private FieldResolution(){
		// It isn't supposed to create an instance of this class
	}

	/**
	 * Resolve a field
	 *
	 * @param clazz
	 *            Classe being used
	 * @param obj
	 *            Object being used
	 * @param field
	 *            field path like EL
	 * @return The object being resolved
	 */
	public static Object resolveField(final Class<?> clazz, final Object obj, final String field) {
		int i = 0;
		Object o = obj;
		boolean going = true;
		Class<?> temp = clazz;
		final String[] mthds = field.split(Constants.DOT);

		while(i < mthds.length && going){
			Method m;
			try {
				m = temp.getMethod(transformToGet(mthds[i]));
			} catch (NoSuchMethodException | SecurityException e) {
				throw new JDocsGenException(e);
			}
			if(m != null){
				try {
					o = m.invoke(o);
				} catch (IllegalAccessException | InvocationTargetException	| IllegalArgumentException e) {
					throw new JDocsGenException(e);
				}
				if(o != null){
					temp = o.getClass();
				} else {
					going = false;
				}
			}
			i++;
		}
		return o;
	}

	/**
	 * Transform a string 'name' into 'getName'
	 *
	 * @param part
	 *            the field that you want the get
	 * @return The name of a get method
	 */
	private static String transformToGet(final String part){
		final StringBuilder sb = new StringBuilder(Constants.GET);
		final char[] aux = part.toCharArray();
		aux[0] = Character.toUpperCase(aux[0]);
		sb.append(aux);
		return sb.toString();
	}

}