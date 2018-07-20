package com.iaoongin.wxgzh.common.util;

import com.alibaba.fastjson.JSONObject;

/**
 * @author XHX
 * @date 2018/7/18
 */
public class WxUtil {

    //获取token
    public static String getAccessToken(){

        String url=WXProperties.getInstance().getProperty("access_tokenUrl");
        String APPID=WXProperties.getInstance().getProperty("AppID");
        String SECRET=WXProperties.getInstance().getProperty("AppSecret");
        url=url.replace("APPID", APPID).replace("APPSECRET", SECRET);
        JSONObject json;
        String access_token="";
        try {
            json = HttpsRequest.httpsRequest(url, "GET", null);
            System.out.println(json);
            if(json!=null && json.containsKey("access_token")){
                access_token=json.getString("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }

    //获取CodeAccess_token
    public static JSONObject getAccess_token(String code) throws Exception{
        String url=WXProperties.getInstance().getProperty("codeAccess_tokenUrl");
        String APPID=WXProperties.getInstance().getProperty("AppID");
        String SECRET=WXProperties.getInstance().getProperty("AppSecret");
        url=url.replace("APPID", APPID).replace("SECRET", SECRET).replace("CODE", code);
        return HttpsRequest.httpsRequest(url, "GET", null);
    }

    //获取微信用户信息
    public static JSONObject getUserInfo(String OPENID,String ACCESS_TOKEN) throws Exception{
        String url=WXProperties.getInstance().getProperty("user_infoUrl");
        url=url.replace("OPENID", OPENID).replace("ACCESS_TOKEN", ACCESS_TOKEN);
        return HttpsRequest.httpsRequest(url, "GET", null);
    }
}
