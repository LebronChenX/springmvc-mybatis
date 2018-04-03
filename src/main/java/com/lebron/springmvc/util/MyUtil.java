package com.lebron.springmvc.util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyUtil {

    public static final ObjectMapper MAPPER = new ObjectMapper();
    public static final ObjectMapper MAPPER_NON_NULL = new ObjectMapper().setSerializationInclusion(Include.NON_NULL);

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return new ObjectMapper().getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static String formatLatLong(String string) {
        Double dou = Double.valueOf(string);
        return String.format("%.5f", dou);
    }

}
