package com.vendora.common.mapping;

import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class QueryParamBinder {

    private QueryParamBinder() {}

    public static <T> T bind(T target, MultiValueMap<String, String> queryParams) {
        Class<?> clazz = target.getClass();

        queryParams.forEach((rawKey, values) -> {
            String fieldName = snakeToCamel(rawKey);

            try {
                Field field = clazz.getDeclaredField(fieldName);
                field.setAccessible(true);

                Object convertedValue = convert(field, values);
                field.set(target, convertedValue);

            } catch (NoSuchFieldException ignored) {
                // queryParam no existe en el DTO → se ignora
            } catch (Exception e) {
                throw new RuntimeException("Error binding query param: " + rawKey, e);
            }
        });

        return target;
    }

    private static Object convert(Field field, List<String> values) {
        Class<?> type = field.getType();

        if (List.class.isAssignableFrom(type)) {
            return convertList(field, values);
        }

        return convertSimple(type, values.get(0));
    }

    private static Object convertList(Field field, List<String> values) {
        ParameterizedType genericType =
          (ParameterizedType) field.getGenericType();

        Class<?> elementType =
          (Class<?>) genericType.getActualTypeArguments()[0];

        return values.stream()
          .map(value -> convertSimple(elementType, value))
          .toList();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private static Object convertSimple(Class<?> type, String value) {
        if (type.equals(String.class)) return value;
        if (type.equals(Integer.class)) return Integer.valueOf(value);
        if (type.equals(Long.class)) return Long.valueOf(value);
        if (type.isEnum()) return Enum.valueOf((Class<Enum>) type, value);

        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    private static String snakeToCamel(String value) {
        StringBuilder result = new StringBuilder();
        boolean upperNext = false;

        for (char c : value.toCharArray()) {
            if (c == '_') {
                upperNext = true;
            } else {
                result.append(upperNext ? Character.toUpperCase(c) : c);
                upperNext = false;
            }
        }
        return result.toString();
    }
}
