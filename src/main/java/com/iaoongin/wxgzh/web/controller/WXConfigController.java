package com.iaoongin.wxgzh.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.iaoongin.wxgzh.common.util.AccessTokenCache;
import com.iaoongin.wxgzh.common.util.SignUtil;
import com.iaoongin.wxgzh.common.util.WXProperties;
import com.iaoongin.wxgzh.common.util.WxUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author XHX
 * @date 2018/8/21
 */
@Controller
@Slf4j
public class WXConfigController extends BaseController {

    /**
     * 0.后台服务器被动身份验证（微信调用本接口）
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping("/config")
    @ResponseBody
    public String config(@RequestParam String signature, @RequestParam String timestamp, @RequestParam String nonce, @RequestParam String echostr) {

        log.info("config from 微信...");
        log.info(request.getRemoteHost() + ":" + request.getRemotePort());
        return SignUtil.checkSigature(signature, timestamp, nonce) ? echostr : "微信签名验证失败";
    }

    @GetMapping("/jssdkConfig")
    @ResponseBody
    public Map jssdkConfig(String url) {
        log.info("开始获取jsapiTicket...");

        if (!StringUtils.isBlank(url)) {

        }

        String jsapiTicket = "";
        try {
            // 获取缓存的accessToken和jsapiTicket
            AccessTokenCache accessTokenCache = AccessTokenCache.getInstance();
            jsapiTicket = accessTokenCache.getValue("jsapiTicket").toString();

            if (!StringUtils.isBlank(jsapiTicket)) {
                return SignUtil.sign(jsapiTicket, URLDecoder.decode(url, "UTF-8"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<Object, Object> map = new HashMap<>(2);
        map.put("errcode", "1");
        map.put("errmsg", "url为空");
        return map;
    }

    @GetMapping("/refreshConfig")
    @ResponseBody
    public String refreshJssdkConfig() {

        AccessTokenCache.getInstance().clearAll();

        return "refresh ok";
    }

}
