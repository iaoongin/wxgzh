package com.iaoongin.wxgzh.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author XHX
 * @date 2018/8/3
 */
@Controller
public class UrlController extends BaseController {

    @GetMapping("/register.html")
    public String registerHtml() {
        return "register";
    }

    @GetMapping("/jssdk.html")
    public String jssdk() {
        return "jssdk";
    }

}
