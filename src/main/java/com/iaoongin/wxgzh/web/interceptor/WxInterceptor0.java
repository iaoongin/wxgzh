package com.iaoongin.wxgzh.web.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.iaoongin.wxgzh.common.util.WXProperties;
import com.iaoongin.wxgzh.common.util.WxUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * 拦截是否有openId
 *
 * @author XHX
 * @date 2018/7/28
 */
public class WxInterceptor0 extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WxInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String openId = request.getParameter("openId");

        logger.info("进入openId拦截器");

        logger.info("====================================================");
        logger.info("||requestSessionId:" + request.getRequestedSessionId());
        logger.info("||from: " + request.getRemoteHost() + ":" + request.getRemotePort());
        logger.info("====================================================");

        // 是否传入openid
        if (StringUtils.isBlank(openId)) {
            logger.info("未传入openid");
            HttpSession session = request.getSession(true);
            // 未传入openid
            openId = (String) session.getAttribute("openId");
            // session是否有openid
            if (StringUtils.isBlank(openId)) {
                logger.info("session没有openid");
                // session 没有
                // 是否code换openId
                String code = request.getParameter("code");
                if (StringUtils.isBlank(code)) {
                    // 没有code
                    // 用户验证
                    String userAuthoriseUrl = WXProperties.getInstance().getProperty("userAuthoriseUrl");
                    String redirectUrl = URLEncoder.encode(WXProperties.getInstance().getProperty("serverUrl") + request.getRequestURI(), "UTF-8");
                    String state = System.currentTimeMillis() + "";
                    userAuthoriseUrl = userAuthoriseUrl.replace("REDIRECT_URI", redirectUrl)
                            .replace("APPID", WXProperties.getInstance().getProperty("AppID"))
                            .replace("SCOPE", "snsapi_userinfo")
                            .replace("STATE", state);
                    logger.info("重定向到：" + URLDecoder.decode(redirectUrl, "UTF-8"));
                    response.sendRedirect(userAuthoriseUrl);
                    return false;
                } else {
                    // 有code
                    // 换取openId
                    logger.info("使用code换取openId，code:" + code);
                    JSONObject jsonObject = WxUtil.getAccess_token(code);
                    // code 换取失败
                    String access_token = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");

                    if (StringUtils.isBlank(openId)) {

                        PrintWriter pw = new PrintWriter(response.getOutputStream());
                        logger.info("授权失败");
                        pw.write("javascript:alert('授权失败，请关闭后重试');");
                    }

                    // 存入session
                    session.setAttribute("openId", openId);
                    session.setAttribute("access_token", access_token);
                }
            }
            logger.info("openId :" + openId);
        }
        return true;
    }
}
