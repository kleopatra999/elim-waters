package com.church.elim.utils;

import org.codehaus.jackson.map.introspect.AnnotatedClass;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/31/13
 * Time: 11:54 PM
 */
public class CustomJacksonIntrospector extends JacksonAnnotationIntrospector {
    @Override
    public Object findFilterId(AnnotatedClass ac) {
        // Let's default to current behavior if annotation is found:
        Object id = super.findFilterId(ac);
        // but use simple class name if not
        if (id == null) {
            id = ac.getAnnotated().getSimpleName()+"Filter";
        }
        return id;
    }
}
