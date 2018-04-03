package com.lebron.springmvc.util.memcache;

import java.io.IOException;
import java.util.Set;

import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.MemcachedClient;

public interface MyMemcachedClient {

    public MemcachedClient getMemcacedClient() throws IOException;

    public void setHosts(String hostString);

    public void destroy();

    public void set(String key, Object value, int exp) throws Exception;

    public Object get(String key) throws Exception;

    public void remove(String k, String key, int exp) throws Exception;

    public void put(String k, String key, Object value, int exp) throws Exception;

    public String getServerInfo(String key);

    public Set<String> getServerInfos();

    public boolean allServerIsAvailable();

    public void delete(String key) throws Exception;

    public CASValue<Object> gets(String key) throws Exception;

    public CASResponse cas(String key, long casId, Object value, int exp) throws Exception;
}
