package com.littlez.uiautomator;

import android.app.Instrumentation;


import junit.framework.TestCase;

import java.util.Random;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

/**
 * created by xiaozhi
 * <p>彩蛋视频 测试用例
 * Date 2019/12/3
 */
public class A03CaiDantest extends TestCase {

    /*app 名字*/
    private String appName = "彩蛋视频";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
            A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
            A00UtilTest.errorCount=0;//重置
            while (true) {

                //首页
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.jifen.dandan:id/view_home_top_shadow"));
                //心
                UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.jifen.dandan:id/iv_like_icon"));

                if (uiHome.exists()) {//是首页

                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 10) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 95) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Thread.sleep(15000);//播放 时长
                    } else {//3点击心
                        if (uiHeart.exists()) uiHeart.click();
                    }

                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiDialogClose = new UiObject(new UiSelector().resourceId("com.jifen.dandan:id/iv_close"));
                    UiObject uiDialogClose02 = new UiObject(new UiSelector().resourceId("com.jifen.dandan:id/close_bottom_button"));
                    UiObject uiCloseBtn = new UiObject(new UiSelector().resourceId(""));
                    UiObject uiWebView = new UiObject(new UiSelector().resourceId("com.jifen.dandan:id/q_web_view"));

                    if (uiDialogClose.exists()) {//弹框（邀请好友）
                        uiDialogClose.click();
                    } else if (uiDialogClose02.exists()) {
                        uiDialogClose02.click();
                    } else if (uiCloseBtn.exists()) {//青少年保护弹框
                        uiCloseBtn.click();
                    } else if (uiWebView.exists()) {//个人中心 webView 控件
                        uiDevice.pressBack();
                    } else {//最终的强制搞一波
                        A00UtilTest.baseMethod(uiDevice, 1, appName);
                    }
                }
                Thread.sleep(500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}