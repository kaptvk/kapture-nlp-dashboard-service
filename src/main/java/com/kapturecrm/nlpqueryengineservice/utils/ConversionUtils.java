package com.kapturecrm.nlpqueryengineservice.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.kapturecrm.nlpqueryengineservice.constant.GlobalConstant;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class ConversionUtils {
    public static <K, V, E> Map<K, V> toMap(Collection<E> collection, Predicate<E> filter,
                                            Function<E, K> keyFunction, Function<E, V> valueFunction,
                                            Supplier<Map<K, V>> mapType) {
        try {
            Stream<E> stream = collection.stream();
            if (filter != null) {
                stream = stream.filter(filter);
            }

            return stream
                    .collect(Collectors
                            .toMap(keyFunction, valueFunction, (oldVal, newVal) -> newVal, mapType));
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    public static <K, V, E> Map<K, V> toMap(Collection<E> collection, Predicate<E> filter,
                                            Function<E, K> keyFunction, Function<E, V> valueFunction) {
        return toMap(collection, filter, keyFunction, valueFunction, HashMap::new);
    }

    public static <K, E> Map<K, E> toMap(Collection<E> collection, Predicate<E> filter,
                                         Function<E, K> keyFunction) {
        return toMap(collection, filter, keyFunction, e -> e);
    }

    public static Map<String, String> toMap(String jsonString) {
        try {
            return new ObjectMapper().readValue(jsonString, new TypeReference<Map<String, String>>() {
            });
        } catch (Exception e) {
            log.error("Exception in toMap() : ", e);
            return new HashMap<>();
        }
    }

    public static <E, T> Set<E> toSet(Collection<T> collection, Predicate<T> filter, Function<T, E> keyFunction, Supplier<Set<E>> setType) {
        try {
            Stream<T> stream = collection.stream();
            if (filter != null) {
                stream = stream.filter(filter);
            }
            return stream.map(keyFunction).collect(Collectors.toCollection(setType));
        } catch (Exception e) {
            return new HashSet<>();
        }
    }

    public static <E, T> Set<E> toSet(Collection<T> collection, Predicate<T> filter, Function<T, E> keyFunction) {
        return toSet(collection, filter, keyFunction, HashSet::new);
    }

    public static <K, V> Map<K, List<V>> toMapOfList(Collection<V> collection, Predicate<V> filter,
                                                     Function<V, K> keyFunction, Function<V, V> valueFunction, Supplier<Map<K, List<V>>> mapType,
                                                     Supplier<List<V>> collectionType) {
        try {
            Stream<V> stream = collection.stream();
            if (filter != null) {
                stream = stream.filter(filter);
            }

            return stream.collect(Collectors.groupingBy(keyFunction, mapType,
                    Collectors.mapping(valueFunction, Collectors.toCollection(collectionType))));
        } catch (Exception e) {
            return mapType.get();
        }
    }

    public static <K, V> Map<K, List<V>> toMapOfList(Collection<V> collection, Predicate<V> filter,
                                                     Function<V, K> keyFunction, Function<V, V> valueFunction) {
        Supplier<Map<K, List<V>>> mapType = HashMap::new;
        return toMapOfList(collection, filter, keyFunction, valueFunction, mapType, ArrayList::new);
    }

    public static <K, V> Map<K, List<V>> toMapOfList(Collection<V> collection, Predicate<V> filter,
                                                     Function<V, K> keyFunction) {
        return toMapOfList(collection, filter, keyFunction, element -> element);
    }


    public static <T> T toTypeReference(String jsonStr, TypeReference<T> typeReference) {
        try {
            return new ObjectMapper().readValue(jsonStr, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Error in toTypeReference(): ", e);
            return null;
        }
    }

    public static <T> String toJsonString(T obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .setDateFormat(new SimpleDateFormat(GlobalConstant.TIMESTAMP_FORMAT));
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Error in toJsonString(): {}", obj.getClass().getName());
            return "";
        }
    }

    public static JSONObject toJsonObject(String jsonStr) {
        try {
            return StringUtils.isBlank(jsonStr) ? new JSONObject() : JSONObject.fromObject(jsonStr);
        } catch (Exception e) {
            return new JSONObject();
        }
    }

    public static <E, T> T toType(E obj, Function<E, T> converter, T defaultVal) {
        try {
            return converter.apply(obj);
        } catch (Exception e) {
            return defaultVal;
        }
    }
    public static JSONObject setResponse(String status, String message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status);
        jsonObject.put("message", message);

        return jsonObject;
    }

    public static JSONObject setResponse(String status, String message, String objName, Object object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status);
        jsonObject.put("message", message);

        if (object != null && StringUtils.isNotBlank(objName)) {
            jsonObject.put(objName, ConversionUtils.toJson(object));
        }
        return jsonObject;
    }

    public static String toJson(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Date formatter
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ignore) {

        }
        return "";
    }

    public static JSONObject setResponseIncludingDefaultValues(String status, String message, String objName, Object object) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status", status);
        jsonObject.put("message", message);

        if (object != null && StringUtils.isNotBlank(objName)) {
            jsonObject.put(objName, toJsonWithDefault(object));
        }
        return jsonObject;
    }

    public static String toJsonWithDefault(Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            // Date formatter
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

            mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException ignore) {

        }
        return "";
    }

}
