package me.lorenzo0111.teleport.cache;

import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExpiringCache<K,V> {
    private final Map<K,V> cache;

    public ExpiringCache(TimeUnit unit, int time) {
        this.cache = ExpiringMap.builder()
                .expiration(time, unit)
                .expirationPolicy(ExpirationPolicy.ACCESSED)
                .build();
    }

    public Map<K, V> getCache() {
        return cache;
    }
}
