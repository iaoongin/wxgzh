package com.iaoongin.wxgzh.common.util;

import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * @author XHX
 * @date 2018/7/18
 */
public class WxUtil {

    private static final Logger logger = LoggerFactory.getLogger(WxUtil.class);

    /**
     * 获取token
     *
     * @return
     */
    public static String getAccessToken() {

        String url = WXProperties.getInstance().getProperty("access_tokenUrl");
        String APPID = WXProperties.getInstance().getProperty("AppID");
        String APPSECRET = WXProperties.getInstance().getProperty("AppSecret");
        url = url.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        JSONObject json;
        String access_token = "";
        try {
            json = HttpsRequest.httpsRequest(url, "GET", null);
            System.out.println(json);
            if (json != null && json.containsKey("access_token")) {
                access_token = json.getString("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }

    /**
     * 获取CodeAccess_token
     *
     * @param code
     * @return
     * @throws Exception
     */
    public static JSONObject getAccess_token(String code) throws Exception {
        String url = WXProperties.getInstance().getProperty("codeAccess_tokenUrl");
        String APPID = WXProperties.getInstance().getProperty("AppID");
        String APPSECRET = WXProperties.getInstance().getProperty("AppSecret");
        url = url.replace("APPID", APPID).replace("APPSECRET", APPSECRET).replace("CODE", code);
        return HttpsRequest.httpsRequest(url, "GET", null);
    }

    /**
     * 获取微信用户信息
     *
     * @param OPENID
     * @param ACCESS_TOKEN
     * @return
     * @throws Exception
     */
    public static JSONObject getUserInfo(String OPENID, String ACCESS_TOKEN) throws Exception {
        String url = WXProperties.getInstance().getProperty("user_infoUrl");
        url = url.replace("OPENID", OPENID).replace("ACCESS_TOKEN", ACCESS_TOKEN);
        return HttpsRequest.httpsRequest(url, "GET", null);
    }

    /**
     * 获取 js api Ticket
     *
     * @param ACCESS_TOKEN
     * @return
     */
    public static String getJsapiTicket(String ACCESS_TOKEN) {
        String url = WXProperties.getInstance().getProperty("ticketURL");
        url = url.replace("ACCESS_TOKEN", ACCESS_TOKEN);
        JSONObject json;
        String jsapiTicket = "";
        try {
            json = HttpsRequest.httpsRequest(url, "GET", null);
            System.out.println(json);
            if (json != null && json.containsKey("ticket")) {
                jsapiTicket = json.getString("ticket");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsapiTicket;
    }

    /**
     * 获取微信服务器的图片
     *
     * @param ACCESS_TOKEN
     * @param MEDIA_ID
     * @param suffix
     * @return
     * @throws IOException
     */
    public static boolean getUploadFile(String ACCESS_TOKEN, String MEDIA_ID, String suffix) throws IOException {
        String fileUrl = WXProperties.getInstance().getProperty("fileUrl");
        fileUrl = fileUrl.replace("ACCESS_TOKEN", ACCESS_TOKEN).replace("MEDIA_ID", MEDIA_ID);
        CommonFileUtil.download(fileUrl, suffix);
        return true;
    }

    /**
     * 获取微信服务器的图片
     *
     * @param ACCESS_TOKEN
     * @param MEDIA_ID
     * @param suffix
     * @return
     * @throws IOException
     */
    public static boolean getUploadFile(String ACCESS_TOKEN, List<String> MEDIA_ID, String suffix) throws IOException {
        for (String s : MEDIA_ID) {
            if (!getUploadFile(ACCESS_TOKEN, s, suffix)) {
                return false;
            }
        }
        return true;
    }

}
