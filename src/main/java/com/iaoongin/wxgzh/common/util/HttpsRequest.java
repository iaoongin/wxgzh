package com.iaoongin.wxgzh.common.util;


import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 发送HTTPS请求
 */
public class HttpsRequest {

    /**
     * HTTPS 请求
     *
     * @param requestUrl    请求地址
     * @param requestMethod 请求方式 POST或GET
     * @param message       参数
     * @return
     * @throws Exception
     */
    public static JSONObject httpsRequest(String requestUrl, String requestMethod, String message) throws Exception {
        JSONObject json = null;
        TrustManager[] tm = {new MyX509TrustManager()};
        SSLContext ssl = SSLContext.getInstance("SSL", "SunJSSE");
        ssl.init(null, tm, new SecureRandom());

        SSLSocketFactory ssf = ssl.getSocketFactory();

        URL urlGet = new URL(requestUrl);

        HttpsURLConnection http = (HttpsURLConnection) urlGet.openConnection();

        http.setSSLSocketFactory(ssf);
        http.setDoOutput(true);
        http.setDoInput(true);
        http.setUseCaches(false);
        http.setRequestMethod(requestMethod);

        System.setProperty("sun.net.client.defaultConnectTimeout", "30000");
        System.setProperty("sun.net.client.defaultReadTimeout", "30000");

        if (message != null) {
            OutputStream outputStream = http.getOutputStream();
            outputStream.write(message.getBytes("UTF-8"));
            outputStream.close();
        }

        InputStream is = http.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader reader = new BufferedReader(isr);
        String str = null;
        StringBuffer sb = new StringBuffer();

        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }

        //释放资源
        isr.close();
        reader.close();
        is.close();
        is = null;
        http.disconnect();
        json = JSONObject.parseObject(sb.toString());
        return json;
    }

    /**
     * @param requestUrl
     * @param requestMethod
     * @param message
     * @return
     * @throws IOException
     */
    public static JSONObject httpRequest(String requestUrl, String requestMethod, String message) throws IOException {
        JSONObject json = null;
        URL urlGet = new URL(requestUrl);
        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();

        http.setDoOutput(true);
        http.setDoInput(true);
        http.setUseCaches(false);
        http.setRequestMethod(requestMethod);

        if (message != null) {
            OutputStream outputStream = http.getOutputStream();
            outputStream.write(message.getBytes("UTF-8"));
            outputStream.close();
        }

        InputStream is = http.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader reader = new BufferedReader(isr);
        String str = null;
        StringBuffer sb = new StringBuffer();

        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }

        //释放资源
        isr.close();
        reader.close();
        is.close();
        is = null;
        http.disconnect();
        json = JSONObject.parseObject(sb.toString());
        return json;
    }

    public static String setMap(Map<String, Object> map) {
        String message = "";
        if (map != null) {
            for (Entry<String, Object> entry : map.entrySet()) {
                message += entry.getKey() + "=" + entry.getValue() + "&";
            }
        }
        return message;
    }

    public static String httpRequestStr(String requestUrl, String requestMethod, String message) throws IOException {
        StringBuffer sb = new StringBuffer();
        URL urlGet = new URL(requestUrl);
        HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();

        http.setDoOutput(true);
        http.setDoInput(true);
        http.setUseCaches(false);
        http.setRequestMethod(requestMethod);

        if (message != null) {
            OutputStream outputStream = http.getOutputStream();
            outputStream.write(message.getBytes("UTF-8"));
            outputStream.close();
        }

        InputStream is = http.getInputStream();
        InputStreamReader isr = new InputStreamReader(is, "UTF-8");
        BufferedReader reader = new BufferedReader(isr);
        String str = null;
        while ((str = reader.readLine()) != null) {
            sb.append(str);
        }
        //释放资源
        isr.close();
        reader.close();
        is.close();
        is = null;
        http.disconnect();
        return sb.toString();
    }
}

class MyX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {

    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}
