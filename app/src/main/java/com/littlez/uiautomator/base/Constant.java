package com.littlez.uiautomator.base;

import com.littlez.uiautomator.bean.VideosBean;

import java.util.ArrayList;

/**
 * 存放常亮的地方
 */
public class Constant {

    public static boolean isCloseService = false;//是否关闭服务 默认是false 不关闭
    public static boolean isrun = true;//循环的运行
    public static int startFlag = 0;//运行到第几条的flag PS：是一直自增的 所以用的时候进行%运算

    public static ArrayList<VideosBean> videosBeans = new ArrayList<>();//跑的数据
}
