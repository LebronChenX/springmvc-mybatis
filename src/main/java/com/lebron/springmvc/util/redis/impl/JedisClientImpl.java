package com.lebron.springmvc.util.redis.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lebron.springmvc.util.SerializeUtil;
import com.lebron.springmvc.util.redis.JedisClient;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Service
public class JedisClientImpl implements JedisClient {

    protected final Log log = LogFactory.getLog(getClass());
    private JedisPool jedisPool = null;
    private static JedisClientImpl instance = null;

    @Value("${redispoolmaxActive}")
    private int redispoolmaxActive;
    @Value("${redispoolmaxIdle}")
    private int redispoolmaxIdle;
    @Value("${redispoolmaxWait}")
    private long redispoolmaxWait;
    @Value("${redispooltestOnBorrow}")
    private boolean redispooltestOnBorrow;
    @Value("${redisip}")
    private String redisip;
    @Value("${reidspass}")
    private String reidspass;
    @Value("${redistimeout}")
    private int redistimeout;

    private JedisClientImpl() {

    }

    public synchronized JedisPool getJedisPool() {
        if (this.jedisPool == null) {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxActive(this.redispoolmaxActive);
            jedisPoolConfig.setMaxIdle(this.redispoolmaxIdle);
            jedisPoolConfig.setMaxWait(this.redispoolmaxWait);
            jedisPoolConfig.setTestOnBorrow(this.redispooltestOnBorrow);
            String[] ipport = this.redisip.split(":");
            this.jedisPool = new JedisPool(jedisPoolConfig,ipport[0],Integer.parseInt(ipport[1]),this.redistimeout);
        }
        return this.jedisPool;
    }

    public static JedisClientImpl getInstance() {
        if (instance == null) {
            instance = new JedisClientImpl();
        }
        return instance;
    }

    public void destroy() {

    }

    @Override
    public String flushAll() {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return jedis.flushAll();
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Flush all cache error. errorStr:%s", e.toString()));
            e.printStackTrace();
        } finally {
            jedisPool.returnResource(jedis);
        }
        return "ERROR";
    }

    @Override
    public Set<String> getKeys(String pattern) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return jedis.keys(pattern);
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(e.toString());
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return null;
    }

    public String set(String key, Object value, int seconds) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return jedis.setex(key.getBytes(), seconds, SerializeUtil.serialize(value));
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Add cache error. key:%s; errorStr:%s", key, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return "ERROR";
    }

    public String set(String key, Object value) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return jedis.set(key.getBytes(), SerializeUtil.serialize(value));
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("add cache error. key:%s; errorStr:%s", key, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return "ERROR";
    }

    public Object get(String key) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return SerializeUtil.unserialize(jedis.get(key.getBytes()));
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Get cache from redis error. key:%s; errorStr:%s", key, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return null;
    }

    @Override
    public void del(String... keys) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            for (String key : keys) {
                jedis.del(key.getBytes());
            }
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(e.toString());
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
    }

    @Override
    public <T> Long lpush(String key, T... objects) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            Long result = -1L;
            for (int i = 0; i < objects.length; i++) {
                result = jedis.lpush(key.getBytes(), SerializeUtil.serialize(objects[i]));
            }
            return result;
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Add list cache error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return -1L;
    }

    @Override
    public <T> Long rpush(String key, T... objects) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            Long result = -1L;
            for (int i = 0; i < objects.length; i++) {
                result = jedis.rpush(key.getBytes(), SerializeUtil.serialize(objects[i]));
            }
            return result;
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Add list cache error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return -1L;
    }

    @Override
    public List<?> lrange(String key, int start, int end) {
        List<Object> objects = null;
        Jedis jedis = null;
        try {
            objects = new ArrayList<Object>();
            jedis = this.getJedisPool().getResource();
            List<byte[]> bs = jedis.lrange(key.getBytes(), start, end);
            for (byte[] bs2 : bs) {
                objects.add(SerializeUtil.unserialize(bs2));
            }
            return objects;
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Get limit list cache error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return objects;
    }

    @Override
    public Long llen(String key) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return jedis.llen(key.getBytes());
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Get list size error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return -1L;
    }

    @Override
    public Long hset(String key, String field, Object value) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return jedis.hset(key.getBytes(), field.getBytes(), SerializeUtil.serialize(value));
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Set map cache error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return -1L;
    }

    @Override
    public Long hsetStr(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Set map cache error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return -1L;
    }

    @Override
    public Object hget(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return SerializeUtil.unserialize(jedis.hget(key.getBytes(), field.getBytes()));
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Get map value error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return null;
    }

    @Override
    public String hgetStrValue(String key, String field) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            byte[] result = jedis.hget(key.getBytes(), field.getBytes());
            if (result == null) {
                return null;
            }
            return new String(result);
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Get map string value error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return null;
    }

    @Override
    public Map<String, Object> hgetAll(String key) {
        Map<String, Object> results = null;
        Jedis jedis = null;
        try {
            results = new HashMap<String, Object>();
            jedis = this.getJedisPool().getResource();
            Map<byte[], byte[]> tempMap = jedis.hgetAll(key.getBytes());
            for (byte[] field : tempMap.keySet()) {
                results.put(new String(field), SerializeUtil.unserialize(tempMap.get(field)));
            }
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Get map aLL Object error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return results;
    }

    @Override
    public Map<String, String> hgetAllStrValue(String key) {
        Map<String, String> results = null;
        Jedis jedis = null;
        try {
            results = new HashMap<String, String>();
            jedis = this.getJedisPool().getResource();
            Map<byte[], byte[]> tempMap = jedis.hgetAll(key.getBytes());
            for (byte[] field : tempMap.keySet()) {
                results.put(new String(field), new String(tempMap.get(field)));
            }
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Get map aLL String value error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return results;
    }

    @Override
    public Boolean exists(String key) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return jedis.exists(key.getBytes());
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("check key exists error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return false;
    }

    @Override
    public Long lrem(String key, Object obj) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return jedis.lrem(key.getBytes(), 1, SerializeUtil.serialize(obj));
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("Remove obj error from list. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return -1L;
    }

    @Override
    public String lset(String key, long index, Object obj) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return jedis.lset(key.getBytes(), index, SerializeUtil.serialize(obj));
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("set obj error to list. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return "ERROR";
    }

    @Override
    public Object lindex(String key, long index) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return SerializeUtil.unserialize(jedis.lindex(key.getBytes(), index));
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(e.toString());
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return "ERROR";
    }

    @Override
    public Long hdel(String key, String... fields) {
        Jedis jedis = null;
        try {
            int length = fields.length;
            byte[][] bs = new byte[length][];
            for (int i = 0; i < bs.length; i++) {
                bs[i] = fields[i].getBytes();
            }
            jedis = this.getJedisPool().getResource();
            return jedis.hdel(key.getBytes(), bs);
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("remove key from map error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return -1L;
    }

    @Override
    public Long expire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = this.getJedisPool().getResource();
            return jedis.expire(key.getBytes(), seconds);
        } catch (Exception e) {
            this.getJedisPool().returnBrokenResource(jedis);
            log.error(String.format("remove key from map error. key:%s; errorStr:%s", e, e.toString()));
            e.printStackTrace();
        } finally {
            this.getJedisPool().returnResource(jedis);
        }
        return -1L;
    }

    public int getRedispoolmaxActive() {
        return redispoolmaxActive;
    }

    public void setRedispoolmaxActive(int redispoolmaxActive) {
        this.redispoolmaxActive = redispoolmaxActive;
    }

    public int getRedispoolmaxIdle() {
        return redispoolmaxIdle;
    }

    public void setRedispoolmaxIdle(int redispoolmaxIdle) {
        this.redispoolmaxIdle = redispoolmaxIdle;
    }

    public long getRedispoolmaxWait() {
        return redispoolmaxWait;
    }

    public void setRedispoolmaxWait(long redispoolmaxWait) {
        this.redispoolmaxWait = redispoolmaxWait;
    }

    public boolean isRedispooltestOnBorrow() {
        return redispooltestOnBorrow;
    }

    public void setRedispooltestOnBorrow(boolean redispooltestOnBorrow) {
        this.redispooltestOnBorrow = redispooltestOnBorrow;
    }

    public String getRedisip() {
        return redisip;
    }

    public void setRedisip(String redisip) {
        this.redisip = redisip;
    }

    public String getReidspass() {
        return reidspass;
    }

    public void setReidspass(String reidspass) {
        this.reidspass = reidspass;
    }

    public int getRedistimeout() {
        return redistimeout;
    }

    public void setRedistimeout(int redistimeout) {
        this.redistimeout = redistimeout;
    }

}
