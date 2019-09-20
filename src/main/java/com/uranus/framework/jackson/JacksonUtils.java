package com.uranus.framework.jackson;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class JacksonUtils {

    private final static ObjectMapper objectMapper = new ObjectMapper();
    static {
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT, true);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeNull();
            }
        });
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new ParameterNamesModule());
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        final SimpleModule simpleModule = new SimpleModule();
        simpleModule.addDeserializer(LocalTime.class, new SimpleLocalTimeDeserializer(DateTimeFormatter.ofPattern("HH:mm:ss")));
        simpleModule.addDeserializer(String.class, new SimpleStringDeserializer());
        simpleModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        simpleModule.addDeserializer(LocalDate.class, new SimpleLocalDateDeserializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        objectMapper.registerModule(simpleModule);
    }

    private JacksonUtils() {

    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }

    /**
     * javaBean、列表数组转换为json字符串
     *
     * @param obj 数据对象
     * @return String
     * @throws Exception 转换异常
     */
    public static String objToJson(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    /**
     * json 转JavaBean
     *
     * @param jsonString 字符串
     * @param clazz 类
     * @param <T> 转换类型
     * @return T
     * @throws Exception 转换异常
     */
    public static <T> T jsonToPojo(String jsonString, Class<T> clazz) throws Exception {
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        return objectMapper.readValue(jsonString, clazz);
    }

    /**
     * json字符串转换为map
     *
     * @param jsonString 字符串
     * @return Map
     * @throws Exception 转换异常
     */
    public static Map jsonToMap(String jsonString) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(jsonString, Map.class);
    }

    /**
     * json字符串转换为map
     *
     * @param jsonString 字符串
     * @param typeReference 类
     * @param clazz 转换类型
     * @return T
     * @throws Exception 转换异常
     */
    public static <T> Map<String, T> jsonToMap(String jsonString, TypeReference typeReference, Class<T> clazz) throws Exception {
        Map<String, Map<String, Object>> map = objectMapper.readValue(jsonString, typeReference);
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<String, Map<String, Object>> entry : map.entrySet()) {
            result.put(entry.getKey(), mapToPojo(entry.getValue(), clazz));
        }
        return result;
    }

    /**
     * 与javaBean json数组字符串转换为列表
     *
     * @param jsonArrayStr 字符串
     * @param typeReference 类
     * @return List
     * @throws Exception 转换异常
     */
    public static <T> T jsonToList(String jsonArrayStr, TypeReference<T> typeReference) throws Exception {
        return objectMapper.readValue(jsonArrayStr, typeReference);
    }

    /**
     * map  转JavaBean
     *
     * @param map 源
     * @param clazz 类
     * @param <T> 转换类型
     * @return T
     */
    public static <T> T mapToPojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * map 转json
     *
     * @param map 源
     * @return string
     * @throws Exception 转换异常
     */
    public static String mapToJson(Map map) throws Exception {
        return objectMapper.writeValueAsString(map);
    }

    /**
     * bean 类型转换
     * @param obj 源
     * @param clazz 类
     * @param <T> 转换类型
     * @return T
     */
    public static <T> T objToPojo(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }
}