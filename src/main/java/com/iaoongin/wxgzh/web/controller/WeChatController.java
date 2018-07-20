package com.iaoongin.wxgzh.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.iaoongin.wxgzh.common.util.HttpsRequest;
import com.iaoongin.wxgzh.common.util.SHA1;
import com.iaoongin.wxgzh.common.util.WXProperties;
import com.iaoongin.wxgzh.common.util.WxUtil;
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
     * 1.后台服务器被动身份验证（微信调用本接口）
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @param echostr
     * @return
     */
    @GetMapping
    public String weixin(@RequestParam String signature, @RequestParam String timestamp, @RequestParam String nonce, @RequestParam String echostr) {

        //加密/校验流程如下：
        String[] arr = {WXProperties.getInstance().getProperty("token"), timestamp, nonce};
        //1.将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        String str = "";
        //2.将三个参数字符串拼接成一个字符串进行sha1加密
        for (String temp : arr) {
            str += temp;
        }
        //3.开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
        if (signature.equals(SHA1.encode(str))) {
            logger.info("signature:" + signature);
            logger.info("timestamp:" + timestamp);
            logger.info("nonce:" + nonce);
            logger.info("接入成功！");
            return echostr;
        }
        logger.error("接入失败！");
        return null;
    }

    /**
     * 获取openId
     *
     * @param openId      手动传入openId
     * @param code        用于交换Access_token
     * @param scope       用户授权方式
     * @param redirectUrl 跳转地址
     * @return
     */
    public String getOpenId(String openId, String code, String scope, String redirectUrl) {

        // 是否传入openid
        if (StringUtils.isBlank(openId)) {
            // 未传入openid
            openId = (String) session.getAttribute("openId");
            // session是否有openid
            if (StringUtils.isBlank(openId)) {
                // session 没有
                // 是否code换openId
                if (StringUtils.isBlank(code)) {
                    // 没有code
                    // 用户验证
                    String userAuthoriseUrl = WXProperties.getInstance().getProperty("userAuthoriseUrl");
                    try {
                        redirectUrl = URLEncoder.encode(redirectUrl, "UTF-8");
                        String state = System.currentTimeMillis() + "";
                        userAuthoriseUrl = userAuthoriseUrl.replace("REDIRECT_URI", redirectUrl)
                                .replace("APPID", WXProperties.getInstance().getProperty("AppID"))
                                .replace("SCOPE", scope)
                                .replace("STATE", state);
                        response.sendRedirect(userAuthoriseUrl);
                        return null;
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }

                } else {
                    // 有code
                    // 换取openId
                    // codeAccessToken
                    String codeAccess_tokenUrl = WXProperties.getInstance().getProperty("codeAccess_tokenUrl");
                    codeAccess_tokenUrl = codeAccess_tokenUrl.replace("APPID", WXProperties.getInstance().getProperty("AppID"))
                            .replace("APPSECRET", WXProperties.getInstance().getProperty("AppSecret"))
                            .replace("CODE", code);
                    JSONObject response = null;
                    try {
                        response = HttpsRequest.httpsRequest(codeAccess_tokenUrl, "GET", null);
                        String access_token = response.getString("access_token");
                        openId = response.getString("openid");

                        // 存入session
                        session.setAttribute("openId", openId);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }

                }
            }
        }

        return openId;
    }

    /**
     * 注册
     *
     * @param code
     * @param telPhone
     * @param password
     * @return
     */
    @GetMapping("/register")
    public Object register(String openId, String code, String telPhone, String password) {

        // 回调地址
        String redirectUrl = WXProperties.getInstance().getProperty("serverUrl") + "/register?telPhone=" + telPhone + "&password=" + password;
        // 获取openId
        openId = getOpenId(openId, code, "snsapi_base", redirectUrl);

        if (StringUtils.isBlank(openId)) {
            return "";
        }

        logger.info("openId:" + openId);
        logger.info("telPhone:" + telPhone);
        logger.info("password:" + password);

        logger.info("注册业务");
        return "注册成功：电话为：" + telPhone + "openId:" + openId;
    }

    @GetMapping(value = "/main/pay")
    public String pay(Model model) {
        String openid = request.getParameter("openid");
        if (StringUtils.isBlank(openid)) {
            openid = (String) session.getAttribute("openid");
        }
        if (StringUtils.isBlank(openid)) {
            String code = request.getParameter("code");
            if (StringUtils.isNotBlank(code)) {
                JSONObject jsonObject;
                try {
                    jsonObject = WxUtil.getAccess_token(code);
                    System.out.println(jsonObject.toString());
                    if (jsonObject.containsKey("openid")) {
                        openid = (String) jsonObject.get("openid");
                        if (StringUtils.isNotBlank(openid)) {
                            session.setAttribute("openid", openid);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        model.addAttribute("openid", openid);
        return "pay";
    }
}
