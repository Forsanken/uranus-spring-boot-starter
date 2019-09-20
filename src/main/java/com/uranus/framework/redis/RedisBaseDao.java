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

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.TimeoutUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
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
@Component
@SuppressWarnings("unchecked")
public class RedisBaseDao<T> {

    @Resource
    private RedisTemplate<String, T> redisTemplate;

    /**
     * 默认过期时长，单位：秒
     */
    private static final long DEFAULT_EXPIRE = 60 * 60 * 24 * 2;

    /**
     * 不设置过期时长
     */
    private static final long NOT_EXPIRE = -1;


    /**
     * 判断key是否存在
     * @param key key
     * @return boolean
     */
    public boolean existsKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return false;
        }
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
        if (StringUtils.isEmpty(oldKey) || StringUtils.isEmpty(newKey)) {
            return false;
        }
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
    public void expireKey(String key, long time, TimeUnit timeUnit) {
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
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public T getObj(String key) {
        return key == null ? null : this.redisTemplate.opsForValue().get(key);
    }


    /**
     * 普通缓存放入
     * 默认有效期时间2天
     *
     * @param key   键
     * @param value 值
     */
    public void putObj(String key, T value) {
        this.putObj(key, value, DEFAULT_EXPIRE, TimeUnit.SECONDS);
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置为默认日期2天
     * @param unit 时间单位
     */
    public void putObj(String key, T value, long time, TimeUnit unit) {
        if (time > 0) {
            this.redisTemplate.opsForValue().set(key, value, time, unit);
        } else {
            this.redisTemplate.opsForValue().set(key, value, DEFAULT_EXPIRE, TimeUnit.SECONDS);
        }
    }


    // ============================Set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return Set<T>
     */
    public Set<T> getSet(String key) {
        return this.redisTemplate.opsForSet().members(key);
    }


    /**
     * 判断set集合中是否存在value
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean existsSetValue(String key, T value) {
        return this.redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 将数据放入set缓存
     * 有效期时间默认2天
     *
     * @param key    键
     * @param values 值 列表
     * @return 成功个数
     */
    public Long putSet(String key, Collection<T> values) {
        return this.putSet(key, DEFAULT_EXPIRE, TimeUnit.SECONDS, values);
    }

    /**
     * 将数据放入SET缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 列表
     * @return 成功个数
     */
    public Long putSet(String key, long time, TimeUnit unit, Collection<T> values) {
        if (CollectionUtils.isEmpty(values)) {
            return 0L;
        }
        return this.putSet(key, time, unit, (T[])values.toArray());
    }

    /**
     * 将数据放入set缓存
     * 有效期时间默认2天
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public Long putSet(String key, T... values) {
        return this.putSet(key, DEFAULT_EXPIRE, TimeUnit.SECONDS, values);
    }

    /**
     * 将数据放入SET缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    @SafeVarargs
    public final Long putSet(String key, long time, TimeUnit unit, T... values) {
        if (values == null || values.length == 0) {
            return 0L;
        }
        Long count = this.redisTemplate.opsForSet().add(key, values);

        if (time > 0) {
            this.expireKey(key, time, unit);
        } else {
            this.expireKey(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
        }

        return count;
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return Long 长度
     */
    public Long getSetSize(String key) {
        return this.redisTemplate.opsForSet().size(key);
    }

    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */
    public Long removeSetValue(String key, T... values) {
        return this.redisTemplate.opsForSet().remove(key, values);
    }

    // ===============================list=================================
    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束 0 到 -1代表所有值
     * @return List<T>
     */
    public List<T> getList(String key, long start, long end) {
        return this.redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return Long
     */
    public Long getListSize(String key) {
        return this.redisTemplate.opsForList().size(key);
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return T
     */
    public T getListIndex(String key, long index) {
        return this.redisTemplate.opsForList().index(key, index);
    }

    /**
     * 将多个对象放入List
     * 有效期默认2天
     *
     * @param key   键
     * @param values 值
     * @return 成功数
     */
    public Long putList(String key, T... values) {
        return this.putList(key, DEFAULT_EXPIRE, TimeUnit.SECONDS, values);
    }

    /**
     * 将多个对象放入List
     *
     * @param key   键
     * @param values 值
     * @param time  时间
     * @param unit 时间单位
     * @return 成功数
     */
    public Long putList(String key, long time, TimeUnit unit, T... values) {
        if (null == values) {
            return 0L;
        }
        return this.putList(key, time, unit, Arrays.asList(values));
    }

    /**
     * 将集合放入缓存list
     *
     * @param key   键
     * @param values 值
     * @return 成功数
     */
    public Long putList(String key, Collection<T> values) {
        return this.putList(key, DEFAULT_EXPIRE, TimeUnit.SECONDS, values);
    }

    /**
     * 将集合放入缓存List
     *
     * @param key   键
     * @param values 值
     * @param time  时间
     * @param unit 时间单位
     * @return 成功数
     */
    public Long putList(String key, long time, TimeUnit unit, Collection<T> values) {
        if (CollectionUtils.isEmpty(values)) {
            return 0L;
        }

        Long count = this.redisTemplate.opsForList().rightPushAll(key, values);
        if (time > 0) {
            this.expireKey(key, time, unit);
        } else {
            this.expireKey(key, DEFAULT_EXPIRE, TimeUnit.SECONDS);
        }

        return count;
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     */
    public void updateListIndex(String key, long index, T value) {
        this.redisTemplate.opsForList().set(key, index, value);
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个 0表示删除全部
     * @param value 值
     * @return 移除的个数
     */
    public long removeListValue(String key, long count, T value) {
        return this.redisTemplate.opsForList().remove(key, count, value);
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
        RedisSerializer<String> ks = (RedisSerializer<String>) this.redisTemplate.getKeySerializer();
        RedisSerializer<T> vs = (RedisSerializer<T>) this.redisTemplate.getValueSerializer();

        this.redisTemplate.executePipelined((RedisCallback<T>) conn -> {
            map.forEach((k,v) -> conn.setEx(ks.serialize(k), TimeoutUtils.toSeconds(time, unit), vs.serialize(v)));
            return null;
        });
    }

    /**
     * 批量读取数据
     *
     * @param keys 需要读取数据的key
     * @return {@link List<T>}
     */
    public List<T> batchGet(List<String> keys) {
        RedisSerializer<String> ks = (RedisSerializer<String>) this.redisTemplate.getKeySerializer();

        List<Object> list = this.redisTemplate.executePipelined((RedisCallback<T>) redisConnection -> {
            keys.forEach(key -> redisConnection.get(ks.serialize(key)));
            return null;
        });

        return list.stream().filter(Objects::nonNull).map(o -> (T) o).collect(Collectors.toList());
    }
}