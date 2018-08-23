package com.iaoongin.wxgzh.web.controller;

import com.iaoongin.wxgzh.common.util.SignUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author XHX
 * @date 2018/7/18
 */
@RestController
@RequestMapping("/weixin")
public class WeChatController extends BaseController {

    /**
     * 注册
     *
     * @param telPhone
     * @param password
     * @return
     */
    @GetMapping("/auth/register")
    public Object register(String telPhone, String password, String capCode) {
        logger.info("进行注册业务");

        String openId = (String) session.getAttribute("openId");
        logger.info("sessionid:" + session.getId());
        logger.info("telPhone:" + telPhone);
        logger.info("password:" + password);
        logger.info("capCode:" + capCode);
        logger.info("openId:" + openId);

        return "注册信息：openId：" + openId + "  电话为:" + telPhone;
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
