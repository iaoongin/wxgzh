package com.iaoongin.wxgzh.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @author XHX
 * @date 2018/8/22
 */
@Slf4j
public class CommonFileUtil {

    public static final String path = "C:\\Users\\XHX\\Desktop\\";

    public static final String[] imgSuffixS = {"png", "jpg", "jpeg", "pneg"};

    public static final List<String> imgSuffix = Arrays.asList(imgSuffixS);

    public static void download(List<String> fileUrls, String suffix) {
        fileUrls.forEach((fileUrl) -> {
            try {
                download(fileUrl, suffix);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    //图片转化成base64字符串
    public static String GetImageStr() {//将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        String imgFile = "D:\\tupian\\a.jpg";//待处理的图片
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);//返回Base64编码过的字节数组字符串
    }

    /**
     * base64字符串转化成图片
     *
     * @param base64
     * @return
     * @throws IOException
     */
    public static boolean uploadBase64Img(String base64) throws IOException {

        if (base64 == null) {
            return false;
        }

        String[] split = base64.split(",");

        log.info(split[0]);
        String suffix = split[0].split("/")[1].split(";")[0];

        if (!imgSuffix.contains(suffix)) {
            suffix = imgSuffix.get(0);
        }

        log.info("后缀为: " + suffix);
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(split[1]);
            for (int i = 0; i < b.length; ++i) {
                //调整异常数据
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            String imgFilePath = path + genRandomName() + "." + suffix;
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean uploadBase64Img(String[] base64s) throws IOException {
        for (String base64 : base64s) {
            if (!uploadBase64Img(base64)) {
                return false;
            }
        }
        return true;
    }

    public static void download(String fileUrl, String suffix) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            URL url = new URL(fileUrl);

            log.info("开启下载连接...");
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(3 * 1000);
            is = connection.getInputStream();
            if (StringUtils.isBlank(suffix)) {
                suffix = HttpURLConnection.guessContentTypeFromStream(new BufferedInputStream(is));
            }
            log.info("保存路径：" + path + genRandomName() + "." + suffix);
            fos = new FileOutputStream(path + genRandomName() + "." + suffix);
            byte[] buf = new byte[1024];
            int len;
            while ((len = is.read(buf)) > 0) {
                fos.write(buf, 0, len);
            }
            log.info("下载完成");
        } finally {
            close(fos);
            close(is);
        }

    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String genRandomName() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static void main(String[] args) throws IOException {
        uploadBase64Img("data:image/png;base64,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");
    }

}
