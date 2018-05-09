package com.yugoo.gis.user.service.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nihao on 18/5/9.
 */
public class CacheManagerUtils {
    private static final Map<String,CacheManager> pool = new ConcurrentHashMap<>();
    public static void add(String key, CacheManager cacheManager){
        pool.put(key, cacheManager);
    }
    public static void invalid(String key){
        CacheManager cacheManager = pool.get(key);
        if(cacheManager != null){
            cacheManager.invalidateAll();
        }
    }
    public static void invalid(){
        for(String key : pool.keySet()){
            CacheManager cacheManager = pool.get(key);
            if(cacheManager != null){
                cacheManager.invalidateAll();
            }
        }
    }
}
