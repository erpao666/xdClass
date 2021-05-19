package net.xdclass.online_xdclass.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class BaseCache {

    private Cache<String,Object> tenMinuteCache = CacheBuilder.newBuilder()

            .initialCapacity(10)    // 设置缓存初始⼤⼩，应该合理设置，后续会扩容
            .maximumSize(100)   // 最⼤值
            .concurrencyLevel(5)    // 并发数设置
            .expireAfterWrite(600, TimeUnit.SECONDS)    // 缓存过期时间，写⼊后10分钟过期
            .recordStats()  // 统计缓存命中率
            .build();

    private Cache<String,Object> oneHourCache = CacheBuilder.newBuilder()

            .initialCapacity(10)
            .maximumSize(100)
            .concurrencyLevel(5)
            .expireAfterWrite(3600, TimeUnit.SECONDS)
            .recordStats()
            .build();

    public Cache<String, Object> getTenMinuteCache() {
        return tenMinuteCache;
    }

    public void setTenMinuteCache(Cache<String, Object> tenMinuteCache) {
        this.tenMinuteCache = tenMinuteCache;
    }

    public Cache<String, Object> getOneHourCache() {
        return oneHourCache;
    }

    public void setOneHourCache(Cache<String, Object> oneHourCache) {
        this.oneHourCache = oneHourCache;
    }
}
