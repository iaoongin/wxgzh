package com.iaoongin.wxgzh.common.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

/**
 * @author XHX
 * @date 2018/8/22
 */
public class CacheTest {

    public static void main(String[] args) throws InterruptedException {
        LoadingCache<String, Object> caches = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(200, TimeUnit.MILLISECONDS)
                .refreshAfterWrite(300, TimeUnit.MILLISECONDS)
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
                        return key + "OONG";
                    }
                });

        caches.put("key-zorro", "123");

        for (int i = 0; i < 5; i++) {
            Thread.sleep(100);
            System.out.println(caches.getIfPresent("key-zorro"));
        }
    }

}
