package com.iaoongin.wxgzh.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 微信公众号配置
 * @author XHX
 * @date 2018/7/19
 */
public class WXProperties extends Properties {

    private static final long serialVersionUID = 1L;
    // 这是对外提供的一个实例
    private static WXProperties instance = null;

    /**
     * 外部调用这个方法来获取唯一的一个实例
     */

    public synchronized static WXProperties getInstance() {
        if (instance != null) {
            return instance;
        } else {
            instance = new WXProperties();
            return instance;
        }
    }

    // 单例模式最核心的是构造方法私有化
    private WXProperties() {
        // 从db.properties文件中读取所有的配置信息
        InputStream is = this.getClass().getResourceAsStream("/config.properties");
        // 通过类的反射实例找到classpath路径下的资源文件,文件是db.properties,并建立一个流
        try {
            // 将流里面的字节码加载到MyPro对象中
            this.load(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}