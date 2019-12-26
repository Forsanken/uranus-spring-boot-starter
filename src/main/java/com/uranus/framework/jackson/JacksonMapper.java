package com.uranus.framework.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JacksonMapper {

    @Autowired
    private ObjectMapper objectMapper;

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setMapperPropertyNamingStrategy(PropertyNamingStrategy propertyNamingStrategy) {
        objectMapper.setPropertyNamingStrategy(propertyNamingStrategy);
    }

    /**
     * javaBean、列表数组转换为json字符串
     *
     * @param obj 数据对象
     * @return String
     * @throws Exception 转换异常
     */
    public String objToJson(Object obj) throws Exception {
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
    public <T> T jsonToPojo(String jsonString, Class<T> clazz) throws Exception {
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
    public Map jsonToMap(String jsonString) throws Exception {
        return objectMapper.readValue(jsonString, Map.class);
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
    public <T> Map<String, T> jsonToMap(String jsonString, TypeReference typeReference, Class<T> clazz) throws Exception {
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
    public <T> T jsonToList(String jsonArrayStr, TypeReference<T> typeReference) throws Exception {
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
    public <T> T mapToPojo(Map map, Class<T> clazz) {
        return objectMapper.convertValue(map, clazz);
    }

    /**
     * map 转json
     *
     * @param map 源
     * @return string
     * @throws Exception 转换异常
     */
    public String mapToJson(Map map) throws Exception {
        return objectMapper.writeValueAsString(map);
    }

    /**
     * bean 类型转换
     * @param obj 源
     * @param clazz 类
     * @param <T> 转换类型
     * @return T
     */
    public <T> T objToPojo(Object obj, Class<T> clazz) {
        return objectMapper.convertValue(obj, clazz);
    }
}