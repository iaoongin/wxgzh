package com.iaoongin.wxgzh.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @author XHX
 * @date 2018/8/21
 */
@Slf4j
public class AccessTokenCache extends BaseGuavaCache<String, Object> {

    private static AccessTokenCache accessTokenCache;

    /**
     * 在这里初始化必要参数（比如过期时间，定期刷新时间，缓存最大条数等）
     */
    private AccessTokenCache() {
        //初始化过期时间
        this.setExpireDuration(7200);
        this.setExpireTimeUnit(TimeUnit.SECONDS);
        //不刷新缓存
        this.setRefreshDuration(7200);
        this.setRefreshTimeUnit(TimeUnit.SECONDS);
        this.setMaxSize(1000);

    }

    /**
     * 单例模式
     */
    public static AccessTokenCache getInstance() {
        if (accessTokenCache == null) {
            synchronized (AccessTokenCache.class) {
                if (accessTokenCache == null) {
                    accessTokenCache = new AccessTokenCache();
                }
            }
        }
        return accessTokenCache;
    }

    @Override
    public void loadValueWhenStarted() {
    }

    @Override
    protected Object getValueWhenExpired(String key) throws Exception {
        log.info("过期开始刷新..." + key);

        switch (key) {
            case "accessToken":
                return WxUtil.getAccessToken();
            case "jsapiTicket":
                return WxUtil.getJsapiTicket((String) this.getValue("accessToken"));
            default:
                return "";
        }

    }

    public static void main(String[] args) throws Exception {
        AccessTokenCache accessTokenCache = AccessTokenCache.getInstance();
        for (int i = 5; i > 0; i--) {
            Thread.sleep(200);
            if (i / 3 == 0) {
                accessTokenCache.clear("accessToken");
            }
            System.out.println("accessToken: " + accessTokenCache.getValue("accessToken"));
            System.out.println("jsapiTicket: " + accessTokenCache.getValue("jsapiTicket"));
        }
    }
}
