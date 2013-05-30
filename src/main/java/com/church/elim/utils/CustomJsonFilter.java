package com.church.elim.utils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import java.io.IOException;
import java.util.*;

import static com.church.elim.utils.ReflectionUtils.getActualTypeOfGenericParameter;

/**
 * Created with IntelliJ IDEA.
 * User: adi
 * Date: 5/28/13
 * Time: 10:44 PM
 */
public class CustomJsonFilter {
    private String fields;

    private FilterProvider filterProvider;
    private ObjectMapper mapper;
    private final Class[] filterClasses;

    public CustomJsonFilter(String fields, Class... filterClasses) {
        this.fields = fields;
        this.filterClasses = filterClasses;
        this.filterProvider = getFilterProvider(fields);
        this.mapper = new ObjectMapper();
        mapper.setFilters(this.filterProvider);
    }

    public void setFilterProvider(FilterProvider filterProvider) {
        this.filterProvider = filterProvider;
    }

    public String filterObject(Object object) throws IOException {
        StringTokenizer st = new StringTokenizer(fields, ",");
        Set<String> filterProperties = new HashSet<String>();
        while (st.hasMoreTokens()) {
            filterProperties.add(st.nextToken());
        }
        String json = mapper.writeValueAsString(object);
        return json;
    }

    public List<String> filterList(List<Object> elements) throws IOException {
        List<String> result = new ArrayList<String>();
        for (Object element : elements) {
            result.add(filterObject(element));
        }
        return (result.size() > 0) ? result : null;
    }

    private FilterProvider getFilterProvider(String fields) {
        StringTokenizer st = new StringTokenizer(fields, ",");
        Set<String> filterProperties = new HashSet<String>();
        while (st.hasMoreTokens()) {
            filterProperties.add(st.nextToken());
        }
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter(this.filterClasses[0].getSimpleName() + "Filter",
                        SimpleBeanPropertyFilter.filterOutAllExcept(filterProperties));
        return filters;
    }

    public static String filterObject(String fields, Object object) throws IOException {
        if (object == null) return null;
        return new CustomJsonFilter(fields, object.getClass()).filterObject(object);
    }

    public static List<String> filterList(String fields, List objects) throws IOException {
        if (objects == null || objects.size() == 0) return null;
        return new CustomJsonFilter(fields, objects.get(0).getClass()).filterList(objects);
    }
}
