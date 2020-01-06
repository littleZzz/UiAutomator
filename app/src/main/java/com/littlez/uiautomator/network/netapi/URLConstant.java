package com.littlez.uiautomator.network.netapi;

/**
 * Created by xioazhi on 2018/3/27.
 * 项目环境配置的文件
 */

public class URLConstant {


    public static boolean isDebug = true;
    //存放全部的URL（可分为开发、测试、正式）
    public static String BASE = isDebug ? "https://sm.zhangchuwang.cn/" : "https://mall.zhangchuwang.cn/";


    public static String BASE_URL = BASE + "api/";
    public static String BASE_IMG_URL = "https://img.zhangchuwang.cn/";


}
