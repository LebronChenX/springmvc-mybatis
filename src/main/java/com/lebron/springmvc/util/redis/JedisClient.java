package com.lebron.springmvc.util.redis;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface JedisClient {

    /**
     * 清空所有cache
     * 
     * @return
     */
    String flushAll();

    /**
     * 得到key列表
     * 
     * @param pattern
     *            如(key1*)
     * @return
     */
    Set<String> getKeys(String pattern);

    /**
     * 添加缓存,有失效时间
     * 
     * @param key
     * @param value
     * @param seconds
     * @return
     */
    String set(String key, Object value, int seconds);

    /**
     * 添加缓存
     * 
     * @param key
     * @param value
     * @return
     */
    String set(String key, Object value);

    /**
     * 读缓存
     * 
     * @param key
     * @return
     */
    Object get(String key);

    /**
     * 删除key
     * 
     * @param key
     * @return
     */
    void del(String... keys);

    /**
     * 将一个或多个值value插入到列表key的表头。 如果key不存在，一个空列表会被创建并执行LPUSH操作。 当key存在但不是列表类型时，返回一个错误。
     * 
     * @param <T>
     * @param key
     * @param strings
     * @return
     */
    <T> Long lpush(String key, T... objects);

    /**
     * 将一个或多个值value插入到列表key的表尾。 如果key不存在，一个空列表会被创建并执行RPUSH操作。 当key存在但不是列表类型时，返回一个错误。
     * 
     * @param <T>
     * @param key
     * @param strings
     * @return
     */
    <T> Long rpush(String key, T... objects);

    /**
     * 从list里面读取数据
     * 
     * @param key
     * @param start
     * @param end
     * @return
     */
    List<?> lrange(String key, int start, int end);

    /**
     * 取得list的长度
     * 
     * @param key
     * @return
     */
    Long llen(String key);

    /**
     * 从list中删除obj
     * 
     * @param key
     * @param obj
     * @return
     */
    Long lrem(String key, Object obj);

    /**
     * 重置list中的某个值
     * 
     * @param key
     * @param index
     * @param obj
     * @return
     */
    String lset(String key, long index, Object obj);

    /**
     * 取到对应下标的值
     * 
     * @param key
     * @param index
     * @return
     */
    Object lindex(String key, long index);

    /**
     * map方式存值
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    Long hset(String key, String field, Object value);

    /**
     * map方式存值, value为字符串时推荐使用, object序列号比较耗时
     * 
     * @param key
     * @param field
     * @param value
     * @return
     */
    Long hsetStr(String key, String field, String value);

    /**
     * 从map中取值
     * 
     * @param key
     * @param field
     * @return
     */
    Object hget(String key, String field);

    /**
     * 从map中取String类型的值
     * 
     * @param key
     * @param field
     * @return
     */
    String hgetStrValue(String key, String field);

    /**
     * 从cache中取到map,value为object
     * 
     * @param key
     * @return
     */
    Map<String, Object> hgetAll(String key);

    /**
     * 从cache中取到map,value为字符串
     * 
     * @param key
     * @return
     */
    Map<String, String> hgetAllStrValue(String key);

    /**
     * 从map中删除多个key
     * 
     * @param key
     * @param fields
     * @return
     */
    Long hdel(String key, String... fields);

    /**
     * 检查key是否存在
     * 
     * @return
     */
    Boolean exists(String key);

    /**
     * 给指定key设置过期时间
     * 
     * @param key
     * @param seconds
     * @return
     */
    Long expire(String key, int seconds);

}
