package com.github.decioamador.jdocsgen.translation;

public interface Translator {

    String getValue(final Object obj, final String field);

    String getValue(final Object[] objs, final String field, final String sep);

}
