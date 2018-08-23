package com.iaoongin.wxgzh.web.controller;

import com.iaoongin.wxgzh.common.util.AccessTokenCache;
import com.iaoongin.wxgzh.common.util.CommonFileUtil;
import com.iaoongin.wxgzh.common.util.WxUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.Arrays;
import java.util.List;

/**
 * @author XHX
 * @date 2018/8/21
 */
@Controller
@Slf4j
public class JSSDKController {

    @Autowired
    private ServletContext sc;

    @GetMapping("/uploadLocation")
    @ResponseBody
    public String uploadLocation(@RequestParam("lon") String lon, @RequestParam("la") String la) {

        log.info("lon: " + lon);
        log.info("la: " + la);
        return "坐标上传成功!";

    }

    @PostMapping("/uploadFile")
    @ResponseBody
    public String uploadFile(@RequestParam(value = "mediaId[]") String[] mediaId, String suffix) throws Exception {

        if (mediaId == null || mediaId.length == 0) {
            return "空";
        }

        List<String> list = Arrays.asList(mediaId);
        AccessTokenCache accessTokenCache = AccessTokenCache.getInstance();
        String accessToken = (String) accessTokenCache.getValue("accessToken");
        log.info("accessToken：" + accessToken);
        WxUtil.getUploadFile(accessToken, list, suffix);

        return "文件上传成功";
    }

    @PostMapping("/uploadBase64Img")
    @ResponseBody
    public String uploadBase64Img(@RequestParam(value = "base64[]") String[] base64) throws Exception {

        if (base64 == null || base64.length == 0) {
            return "空";
        }

        CommonFileUtil.uploadBase64Img(base64);

        return "ok";
    }

}
