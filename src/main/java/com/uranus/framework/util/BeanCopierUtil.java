package com.uranus.framework.util;

import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 〈一句话功能简述〉<br> 
 * 〈BeanCopier 缓存〉
 *
 * @author chy
 * @since 1.0.0
 */
public class BeanCopierUtil {

    private static final Map<String, BeanCopier> BEAN_COPIERS = new ConcurrentHashMap<>();

    public static void copy(Object srcObj, Object desObj) {
        String key = getKey(srcObj.getClass(), desObj.getClass());
        BeanCopier copier;
        if (!BEAN_COPIERS.containsKey(key)) {
            copier = BeanCopier.create(srcObj.getClass(), desObj.getClass(), false);
            BEAN_COPIERS.put(key, copier);
        } else {
            copier = BEAN_COPIERS.get(key);
        }
        copier.copy(srcObj, desObj, null);
    }

    private static String getKey(Class<?> srcClazz, Class<?> desClazz) {
        return srcClazz.getName() + desClazz.getName();
    }
}