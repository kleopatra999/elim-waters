package com.church.elim.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;

import java.io.IOException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/28/13
 * Time: 10:44 PM
 */
public class CustomJsonFilter<E> {
    private String fields;
    private E object;

    private FilterProvider filterProvider;

    public CustomJsonFilter(String fields, E object) {
        this.fields = fields;
        this.object = object;
        this.filterProvider = getFilterProvider(fields, object);
    }

    public void setFilterProvider(FilterProvider filterProvider) {
        this.filterProvider = filterProvider;
    }

    public String filterObject(E object) throws IOException {
        StringTokenizer st = new StringTokenizer(fields, ",");
        Set<String> filterProperties = new HashSet<String>();
        while (st.hasMoreTokens()) {
            filterProperties.add(st.nextToken());
        }
        FilterProvider filters = new SimpleFilterProvider().addFilter(object.getClass().getSimpleName() + "Filter",
                SimpleBeanPropertyFilter.filterOutAllExcept(filterProperties));
        ObjectMapper mapper = new ObjectMapper();

        mapper.setFilters(filters);
        String json = mapper.writeValueAsString(object);
        return json;
    }

    public List<String> filterList(List<E> elements) throws IOException {
        List<String> result = new ArrayList<String>();
        for (E element : elements) {
            result.add(filterObject(element));
        }
        return (result.size() > 0) ? result : null;
    }

    private FilterProvider getFilterProvider(String fields, E object) {
        StringTokenizer st = new StringTokenizer(fields, ",");
        Set<String> filterProperties = new HashSet<String>();
        while (st.hasMoreTokens()) {
            filterProperties.add(st.nextToken());
        }
        FilterProvider filters = new SimpleFilterProvider().addFilter(object.getClass().getSimpleName() + "Filter",
                SimpleBeanPropertyFilter.filterOutAllExcept(filterProperties));
        return filters;
    }

    public static <E> String filterObject(String fields, E object) throws IOException {
        if(object == null) return null;
        return new CustomJsonFilter<E>(fields, object).filterObject(object);
    }

    public static <E> List<String> filterList(String fields, List<E> objects) throws IOException {
        if(objects == null || objects.size() == 0) return null;
        return new CustomJsonFilter<E>(fields, objects.get(0)).filterList(objects);
    }
}
