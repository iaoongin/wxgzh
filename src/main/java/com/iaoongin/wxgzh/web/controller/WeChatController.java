package com.iaoongin.wxgzh.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.iaoongin.wxgzh.common.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLEncoder;
import java.util.Arrays;

/**
 * @author XHX
 * @date 2018/7/18
 */
@RestController
@RequestMapping("/weixin")
public class WeChatController extends BaseController {

    /**
     * 0.后台服务器被动身份验证（微信调用本接口）
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping
    public String weixin(@RequestParam String signature, @RequestParam String timestamp, @RequestParam String nonce, @RequestParam String echostr) {
        return SignUtil.checkSigature(signature, timestamp, nonce) ? echostr : "微信签名验证失败";
    }


    /**
     * 注册
     *
     * @param telPhone
     * @param password
     * @return
     */
    @GetMapping("/auth/register")
    public Object register(String telPhone, String password) {

        String openId = (String) session.getAttribute("openId");
        logger.info("进行注册业务");
        return "注册成功：openId：" + openId + "  电话为:" + telPhone;
    }

    /**
     * 注册
     *
     * @return
     */
    @GetMapping("/register")
    public Object register() {
        logger.info("进行非认证操作");
        return "非认证操作界面";
    }

}
