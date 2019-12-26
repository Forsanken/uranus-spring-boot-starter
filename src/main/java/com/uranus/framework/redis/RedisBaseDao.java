/**
 * FileName: RedisService
 * Author:   chy
 * Date:     2018/12/17 10:42
 * Description: Redis存储服务
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.uranus.framework.redis;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 〈一句话功能简述〉<br>
 * 〈Redis存储服务〉
 *
 * @author chy
 * @date 2018/12/17
 * @since 1.0.0
 */
@Getter
@Slf4j
@Component
@SuppressWarnings("unchecked")
public class RedisBaseDao<T> {

    @Autowired
    @Qualifier("redisTemplate")
    private RedisTemplate<String, T> redisTemplate;

    private ValueOperations<String, T> valueOper;

    private SetOperations<String, T> setOper;

    private ListOperations<String, T> listOper;

    private HashOperations<String, Object, T> hashOper;

    private HashOperations<String, Object, Collection<T>> hashListOper;

    /**
     * 默认过期时长，单位：秒
     */
    private static final long DEFAULT_EXPIRE = 60 * 60 * 24 * 7;

    /**
     * 批量操作分组长度
     */
    private static final int GROUP_SIZE = 1000;

    public RedisTemplate<String, T> getRedisTemplate() {
        return redisTemplate;
    }

    public ValueOperations<String, T> getValueOper() {
        if (null == this.valueOper) {
            this.valueOper = this.redisTemplate.opsForValue();
        }
        return this.valueOper;
    }

    public SetOperations<String, T> getSetOper() {
        if (null == this.setOper) {
            this.setOper = this.redisTemplate.opsForSet();
        }
        return this.setOper;
    }

    public ListOperations<String, T> getListOper() {
        if (null == this.listOper) {
            this.listOper = this.redisTemplate.opsForList();
        }
        return listOper;
    }

    public HashOperations<String, Object, T> getHashOper() {
        if (null == this.hashOper) {
            this.hashOper = this.redisTemplate.opsForHash();
        }
        return hashOper;
    }

    public HashOperations<String, Object, Collection<T>> getHashListOper() {
        if (null == this.hashListOper) {
            this.hashListOper = this.redisTemplate.opsForHash();
        }
        return hashListOper;
    }

    public static long getDefaultExpire() {
        return DEFAULT_EXPIRE;
    }

    public static int getGroupSize() {
        return GROUP_SIZE;
    }

    /**
     * 判断key是否存在
     * @param key key
     * @return boolean
     */
    public boolean existsKey(String key) {
        return this.redisTemplate.hasKey(key);
    }

    /**
     * 重名名key，如果newKey已经存在，则newKey的原值被覆盖
     *
     * @param oldKey 旧key
     * @param newKey 新key
     */
    public void renameKey(String oldKey, String newKey) {
        this.redisTemplate.rename(oldKey, newKey);
    }

    /**
     * newKey不存在时才重命名
     *
     * @param oldKey 旧key
     * @param newKey 新key
     * @return 修改成功返回true
     */
    public boolean renameKeyNotExist(String oldKey, String newKey) {
        return this.redisTemplate.renameIfAbsent(oldKey, newKey);
    }

    /**
     * 删除以key开头的所有对象
     * 模糊匹配删除所有对象
     * 数据较大时比较慢，慎用
     *
     * @param key 模糊查询关键字
     */
    public void deleteFuzzyMatch(String key) {
        Set<String> keys = this.redisTemplate.keys(key + "*");
        this.redisTemplate.delete(keys);
    }

    /**
     * 删除多个key
     *
     * @param keys keys
     */
    public void deleteKey(String... keys) {
        Set<String> kSet = Stream.of(keys).collect(Collectors.toSet());
        this.redisTemplate.delete(kSet);
    }

    /**
     * 删除Key的集合
     *
     * @param keys keys
     */
    public void deleteKey(Collection<String> keys) {
        Set<String> kSet = new HashSet<>(keys);
        this.redisTemplate.delete(kSet);
    }

    /**
     * 设置key的生命周期
     *
     * @param key key
     * @param time 过期时间
     * @param timeUnit 时间单位
     */
    public void expireKey(String key, Long time, TimeUnit timeUnit) {
        if (time == null || time < 0) {
            time = DEFAULT_EXPIRE;
        }
        if (timeUnit == null) {
            timeUnit = TimeUnit.SECONDS;
        }
        this.redisTemplate.expire(key, time, timeUnit);
    }

    /**
     * 指定key在指定的日期过期
     *
     * @param key key
     * @param dateTime 过期时间
     */
    public void expireKeyAt(String key, LocalDateTime dateTime) {
        ZonedDateTime zdt = dateTime.atZone(ZoneId.systemDefault());
        Date date = Date.from(zdt.toInstant());
        this.redisTemplate.expireAt(key, date);
    }

    /**
     * 查询key的生命周期
     *
     * @param key key
     * @param timeUnit 时间单位
     * @return long
     */
    public long getKeyExpire(String key, TimeUnit timeUnit) {
        return this.redisTemplate.getExpire(key, timeUnit);
    }

    /**
     * 将key设置为永久有效
     *
     * @param key key
     */
    public void persistKey(String key) {
        this.redisTemplate.persist(key);
    }

    // ============================Object=============================


    /**
     * 普通缓存放入
     * 默认有效期时间7天
     *
     * @param key   键
     * @param value 值
     */
    public void putObj(String key, T value) {
        this.getValueOper().set(key, value, DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }


    // ============================Map=============================

    /**
     * Map缓存放入
     * 默认有效期时间7天
     *
     * @param key   键
     * @param value 值
     */
    public void putMap(String key, Map<String, T> value) {
        this.getHashOper().putAll(key, value);
        this.expireKey(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * Map缓存放入某个值
     * 默认有效期时间7天
     *
     * @param key    键
     * @param mapKey map键
     * @param value  值
     */
    public void putMapValue(String key, String mapKey, T value) {
        this.getHashOper().put(key, mapKey, value);
        this.expireKey(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * Map分组缓存放入
     * 默认有效期时间7天
     *
     * @param key   键
     * @param value 值
     */
    public void putMapGroupList(String key, Map<String, List<T>> value) {
        if (CollectionUtils.isEmpty(value)) {
            return;
        }
        Map<String, Collection<T>> map = new HashMap<>();
        value.forEach((k, v) -> map.put(k, new ArrayList<>(v)));
        this.putMapGroup(key, map);
    }

    /**
     * Map分组缓存放入
     * 默认有效期时间7天
     *
     * @param key   键
     * @param value 值
     */
    public void putMapGroup(String key, Map<String, Collection<T>> value) {
        if (CollectionUtils.isEmpty(value)) {
            return;
        }
        this.getHashListOper().putAll(key, value);
        this.expireKey(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * Map分组缓存放入某个值
     * 默认有效期时间7天
     *
     * @param key   键
     * @param mapKey map对象中的KEY值
     * @param value 值
     */
    public void putMapValue(String key, Object mapKey, Collection<T> value) {
        if (CollectionUtils.isEmpty(value)) {
            return;
        }
        this.getHashListOper().put(key, mapKey, value);
        this.expireKey(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 读取Map
     * @param key   键
     */
    public Map<String, T> getMap(String key) {
        Map<Object, T> map = this.getHashOper().entries(key);
        Map<String, T> rtMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(map)) {
            map.forEach((k, v) -> rtMap.put(String.valueOf(k), v));
        }
        return rtMap;
    }

    /**
     * 读取Map分组
     * @param key   键
     */
    public Map<String, List<T>> getMapGroup(String key) {
        Map<Object, Collection<T>> map = this.getHashListOper().entries(key);
        Map<String, List<T>> rtMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(map)) {
            map.forEach((k, v) -> rtMap.put(String.valueOf(k), new ArrayList<>(v)));
        }
        return rtMap;
    }

    // ============================Set=============================

    /**
     * 将数据放入set缓存
     * 有效期时间默认7天
     *
     * @param key    键
     * @param values 值 列表
     * @return 成功个数
     */
    public Long putSet(String key, Set<T> values) {
        if (CollectionUtils.isEmpty(values)) {
            return 0L;
        }
        return this.putSet(key, (T[])values.toArray());
    }

    /**
     * 将数据放入SET缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @SafeVarargs
    public final Long putSet(String key, T... values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        Long count = this.getSetOper().add(key, values);
        this.expireKey(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
        return count;
    }

    // ===============================list=================================

    /**
     * 将多个对象放入List
     * 有效期默认7天
     *
     * @param key   键
     * @param values 值
     * @return 成功数
     */
    public Long putList(String key, T... values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        return this.putList(key, Arrays.asList(values));
    }

    /**
     * 将集合放入缓存List
     *
     * @param key   键
     * @param values 值
     * @return 成功数
     */
    public Long putList(String key, Collection<T> values) {
        if (CollectionUtils.isEmpty(values)) {
            return 0L;
        }
        Long count = this.getListOper().rightPushAll(key, values);
        this.expireKey(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
        return count;
    }

    // ===============================batch=================================
    /**
     * 批量插入数据
     *
     * @param map 插入数据 key : value
     */
    public void batchInsert(Map<String, T> map) {
        this.batchInsert(map, DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 批量插入数据
     *
     * @param map 插入数据 key : value
     * @param time 有效期
     * @param unit 有效期单位
     */
    public void batchInsert(Map<String, T> map, long time, TimeUnit unit) {
        if (CollectionUtils.isEmpty(map)) {
            return;
        }
        RedisSerializer<String> ks = (RedisSerializer<String>) this.redisTemplate.getKeySerializer();
        RedisSerializer<T> vs = (RedisSerializer<T>) this.redisTemplate.getValueSerializer();

        int i = 0;
        final Map<String, T> finalMap = new HashMap<>();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            finalMap.put(entry.getKey(), entry.getValue());
            i++;
            if (finalMap.size() >= GROUP_SIZE || i >= map.size()) {
                this.redisTemplate.executePipelined((RedisCallback<T>) conn -> {
                    finalMap.forEach((k,v) -> conn.setEx(ks.serialize(k), TimeoutUtils.toSeconds(time, unit), vs.serialize(v)));
                    return null;
                });
                finalMap.clear();
            }
        }
    }

    /**
     * 批量读取数据
     *
     * @param keys 需要读取数据的key
     * @return {@link List<T>}
     */
    public List<T> batchGet(List<String> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return new ArrayList<>();
        }
        RedisSerializer<String> ks = (RedisSerializer<String>) this.redisTemplate.getKeySerializer();
        List<Object> list = new ArrayList<>();

        for (int i = 0; i < Math.floor(keys.size() / GROUP_SIZE) + 1; i++) {
            int endIndex = (i + 1) * GROUP_SIZE > keys.size() ? keys.size() : (i + 1) * GROUP_SIZE;
            final List<String> finalSubKeys = keys.subList(i * GROUP_SIZE, endIndex);

            List<Object> subList = this.redisTemplate.executePipelined((RedisCallback<T>) redisConnection -> {
                finalSubKeys.forEach(key -> redisConnection.get(ks.serialize(key)));
                return null;
            });
            list.addAll(subList);
        }

        return list.stream().filter(Objects::nonNull).map(o -> (T) o).collect(Collectors.toList());
    }
}