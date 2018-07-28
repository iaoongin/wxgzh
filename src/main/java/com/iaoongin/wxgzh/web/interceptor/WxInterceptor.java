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
import java.net.URLEncoder;

/**
 * 拦截是否有openId
 *
 * @author XHX
 * @date 2018/7/28
 */
public class WxInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(WxInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String openId = request.getParameter("openId");

        // 是否传入openid
        if (StringUtils.isBlank(openId)) {

            HttpSession session = request.getSession(true);
            // 未传入openid
            openId = (String) session.getAttribute("openId");
            // session是否有openid
            if (StringUtils.isBlank(openId)) {
                // session 没有
                // 是否code换openId
                String code = request.getParameter("code");
                if (StringUtils.isBlank(code)) {
                    // 没有code
                    // 用户验证
                    String userAuthoriseUrl = WXProperties.getInstance().getProperty("userAuthoriseUrl");
                    String redirectUrl = URLEncoder.encode(WXProperties.getInstance().getProperty("serverUrl") + request.getRequestURI(), "UTF-8");
                    logger.info("redirectUrl :" + redirectUrl);
                    String state = System.currentTimeMillis() + "";
                    userAuthoriseUrl = userAuthoriseUrl.replace("REDIRECT_URI", redirectUrl)
                            .replace("APPID", WXProperties.getInstance().getProperty("AppID"))
                            .replace("SCOPE", "snsapi_userinfo")
                            .replace("STATE", state);
                    response.sendRedirect(userAuthoriseUrl);
                    return false;
                } else {
                    // 有code
                    // 换取openId
                    logger.info("有code :" + code);
                    JSONObject jsonObject = WxUtil.getAccess_token(code);
                    String access_token = jsonObject.getString("access_token");
                    openId = jsonObject.getString("openid");
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
