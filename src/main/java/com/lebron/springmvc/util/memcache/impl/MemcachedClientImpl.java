package com.lebron.springmvc.util.memcache.impl;

import java.io.IOException;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.lebron.springmvc.util.memcache.MyMemcachedClient;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.CASResponse;
import net.spy.memcached.CASValue;
import net.spy.memcached.DefaultConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.MemcachedNode;
import net.spy.memcached.NodeLocator;

@Component
public class MemcachedClientImpl implements MyMemcachedClient {

    private static final Logger logger = LoggerFactory.getLogger(MemcachedClientImpl.class);

    @Value("${memcacheIp}")
    private String hosts;
    @Value("${memcache.timeout}")
    private long timeout;
    private MemcachedClient mc = null;
    private static MemcachedClientImpl instance = null;

    private MemcachedClientImpl() throws IOException {

    }

    public synchronized MemcachedClient getMemcacedClient() throws IOException {
        // logger.debug("hosts is:" + this.hosts);
        if (this.mc == null) {
            this.mc = new MemcachedClient(new DefaultConnectionFactory() {

                @Override
                public long getOperationTimeout() {
                    return timeout;
                }

            }, AddrUtil.getAddresses(this.hosts));
        }
        return this.mc;
    }

    public void setHosts(String hostString) {
        this.hosts = hostString;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public static synchronized MemcachedClientImpl getInstance() throws IOException {
        if (instance == null) {
            instance = new MemcachedClientImpl();
        }
        return instance;
    }

    public void destroy() {
        this.mc.shutdown();
    }

    public void set(String key, Object value, int exp) throws Exception {
        if (StringUtils.isBlank(key)) {
            throw new Exception("key is null.");
        }
        this.getMemcacedClient().set(key, exp, value);
    }

    public Object get(String key) throws Exception {
        if (StringUtils.isBlank(key)) {
            throw new Exception("key is null.");
        }
        Object object = this.getMemcacedClient().get(key);

        return object;
    }

    public void remove(String k, String key, int exp) throws Exception {
        try {
            while (true) {
                CASValue casValue = getMemcacedClient().gets(k);
                if (casValue != null) {
                    Map map = (Map) casValue.getValue();
                    map.remove(key);
                    CASResponse casResponse = getMemcacedClient().cas(k, casValue.getCas(), exp, map);
                    if (CASResponse.OK.equals(casResponse)) {
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    public void put(String k, String key, Object value, int exp) throws Exception {
        try {
            while (true) {
                CASValue casValue = getMemcacedClient().gets(k);
                if (casValue != null) {
                    Map map = (Map) casValue.getValue();
                    map.put(key, value);
                    CASResponse casResponse = getMemcacedClient().cas(k, casValue.getCas(), exp, map);
                    if (CASResponse.OK.equals(casResponse)) {
                        break;
                    }
                } else {
                    Map map = new HashMap();
                    map.put(key, value);
                    getMemcacedClient().set(k, exp, map);
                    break;
                }
            }
        } catch (Exception e) {
            logger.error(e.toString(), e);
        }
    }

    public Map<String, Object> getMulti(Collection<String> keys) throws Exception {
        Map<String, Object> objMap = this.getMemcacedClient().getBulk(keys);
        return objMap;
    }

    @Override
    public String getServerInfo(String key) {
        try {
            NodeLocator nl = mc.getNodeLocator();
            MemcachedNode md = nl.getPrimary(key);
            SocketAddress sa = md.getSocketAddress();
            return sa.toString();
        } catch (Exception e) {
            return "";
        }
    }

    @Override
    public Set<String> getServerInfos() {
        Set<String> ss = new HashSet<String>();
        String[] hs = hosts.split(" ");
        for (String h : hs) {
            ss.add("/" + h.trim());
        }
        return ss;
    }

    @Override
    public boolean allServerIsAvailable() {
        try {
            Set<String> hs = getServerInfos();
            Set<SocketAddress> as = this.getMemcacedClient().getVersions().keySet();
            for (String h : hs) {
                if (!isExistServer(h, as)) {
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            // log.error(e.getMessage(), e);
            return false;
        }
    }

    private boolean isExistServer(String server, Set<SocketAddress> as) {
        for (SocketAddress a : as) {
            if (a.toString().equals(server)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void delete(String key) throws Exception {
        if (key != null) {
            getMemcacedClient().delete(key);
        }
    }

    public CASValue<Object> gets(String key) throws Exception {
        if (StringUtils.isBlank(key)) {
            throw new Exception("key is null.");
        }
        CASValue<Object> casValue = this.getMemcacedClient().gets(key);
        return casValue;
    }

    public CASResponse cas(String key, long casId, Object value, int exp) throws Exception {
        if (StringUtils.isBlank(key)) {
            throw new Exception("key is null.");
        }
        CASResponse casResponse = this.getMemcacedClient().cas(key, casId, exp, value);
        return casResponse;
    }

}
