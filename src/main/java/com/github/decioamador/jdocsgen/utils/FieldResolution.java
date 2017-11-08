package com.github.decioamador.jdocsgen.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import com.github.decioamador.jdocsgen.JDocsGenException;

/**
 * This class has the purpose of auxiliary in the path resolution of the fields
 */
public final class FieldResolution {

    private static final String GET = "get";
    private static final String DOT = "\\.";

    private FieldResolution() {
        // It isn't supposed to create an instance of this class
    }

    /**
     * Resolve a field
     *
     * @param obj
     *            Object being used
     * @param field
     *            field path like EL
     * @return The object resolved
     */
    public static Object resolveField(final Object obj, final String field) {
        int i = 0;
        Object o = obj;
        String mthName;
        Class<?> temp = obj.getClass();
        final String[] mthds = field.split(DOT);

        while (o != null && i < mthds.length) {
            Method m;
            try {
                mthName = transformToGet(mthds[i++]);
                m = temp.getMethod(mthName);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new JDocsGenException(e);
            }

            try {
                o = m.invoke(o);
            } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
                throw new JDocsGenException(e);
            }
            if (o != null) {
                temp = o.getClass();
            }
        }
        return o;
    }

    /**
     * Resolve a field and aggregates elements in case it encounters a collection or
     * an array
     *
     * @param obj
     *            Object root
     * @param field
     *            field path
     * @return The object resolved that can be an array in case it encountered a
     *         collection or an array or the object itself
     */
    public static Object resolveFieldAggregation(final Object obj, final String field) {
        final String[] mthds = field.split(DOT);
        Class<?> temp = obj.getClass();
        Collection<?> c;
        Object[] arrayObj;
        Object o = obj;
        String mthName;
        int i = 0;

        while (o != null && i < mthds.length) {
            Method m;
            try {
                mthName = transformToGet(mthds[i++]);
                m = temp.getMethod(mthName);
            } catch (NoSuchMethodException | SecurityException e) {
                throw new JDocsGenException(e);
            }
            o = resolveMethodAggregation(o, m);

            if (o != null) {
                if (o instanceof Collection) {
                    c = (Collection<?>) o;
                    if (c.isEmpty()) {
                        o = null;
                    } else {
                        temp = c.iterator().next().getClass();
                    }
                } else if (o.getClass().isArray()) {
                    arrayObj = (Object[]) o;
                    if (arrayObj.length == 0) {
                        o = null;
                    } else {
                        temp = arrayObj[0].getClass();
                    }
                } else {
                    temp = o.getClass();
                }
            }
        }

        return o;
    }

    /**
     * Invokes the method to his object and aggregates the elements in case they are
     * collections or arrays
     *
     * @param obj
     *            object to which you should invoke
     * @param m
     *            method being invoked
     * @return object resolved
     */
    private static Object resolveMethodAggregation(final Object obj, final Method m) {
        Collection<?> c;
        Object[] arrayObj;
        Object[] objs;
        Object o = obj;
        int i = 0;

        try {
            if (o instanceof Collection) {
                c = (Collection<?>) o;
                objs = new Object[c.size()];
                for (final Object aux : c) {
                    if (aux != null) {
                        objs[i++] = m.invoke(aux);
                    }
                }
                o = aggregateElements(objs);
            } else if (o.getClass().isArray()) {
                arrayObj = (Object[]) o;
                objs = new Object[arrayObj.length];
                for (final Object aux : arrayObj) {
                    if (aux != null) {
                        objs[i++] = m.invoke(aux);
                    }
                }
                o = aggregateElements(objs);
            } else {
                o = m.invoke(o);
            }
        } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
            throw new JDocsGenException(e);
        }
        return o;
    }

    /**
     * Aggregates elements in case they are lists or arrays
     *
     * @param o
     *            array of objects that can contain arrays or collections in each
     *            element
     * @return an array with all the elements aggregated. It returns an untouched
     *         array in case the parameter is null, empty or the first element is
     *         null
     */
    private static Object[] aggregateElements(final Object[] o) {
        Object[] result = o;
        if (o != null && o.length != 0 && o[0] != null) {
            if (o[0].getClass().isArray()) {
                result = aggregateArrays(o);
            } else if (o[0] instanceof Collection) {
                result = aggregateCollections(o);
            }
        }
        return result;
    }

    /**
     * Aggregates the elements of a Collection into a Example: <br>
     * [ [a,b,c], [z,x,y] ] <br>
     * =><br>
     * [a,b,c,z,x,y]<br>
     *
     * @param o
     *            object array that each element is an array
     * @return a collection of all the elements aggregated
     * @required o must not be null
     */
    private static Object[] aggregateArrays(final Object[] o) {
        Object[] innerArray;
        int pointer = 0;
        int size = 0;

        // Calculate size
        for (int i = 0; i < o.length; i++) {
            innerArray = (Object[]) o[i];
            if (innerArray != null) {
                size += innerArray.length;
            }
        }

        // Put the elements in the same array
        final Object[] result = new Object[size];
        for (int i = 0; i < o.length; i++) {
            innerArray = (Object[]) o[i];
            if (innerArray != null) {
                for (final Object aux : innerArray) {
                    if (aux != null) {
                        result[pointer++] = aux;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Aggregates the elements of a Collection into a Example: <br>
     * [ [a,b,c], [z,x,y] ] <br>
     * =><br>
     * [a,b,c,z,x,y]<br>
     *
     * @param o
     *            object array that each element is a collection
     * @return a collection of all the elements aggregated
     */
    private static Object[] aggregateCollections(final Object[] o) {
        Collection<?> innerCollection;
        int pointer = 0;
        int size = 0;

        // Calculate size
        for (int i = 0; i < o.length; i++) {
            innerCollection = (Collection<?>) o[i];
            if (innerCollection != null) {
                size += innerCollection.size();
            }
        }

        // Put the elements in the same array
        final Object[] result = new Object[size];
        for (int i = 0; i < o.length; i++) {
            innerCollection = (Collection<?>) o[i];
            if (innerCollection != null) {
                for (final Object aux : innerCollection) {
                    if (aux != null) {
                        result[pointer++] = aux;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Transform a string 'name' into 'getName'
     *
     * @param part
     *            the field that you want the get
     * @return The name of a get method
     */
    private static String transformToGet(final String part) {
        final StringBuilder sb = new StringBuilder(GET);
        final char[] aux = part.toCharArray();
        aux[0] = Character.toUpperCase(aux[0]);
        sb.append(aux);
        return sb.toString();
    }

}