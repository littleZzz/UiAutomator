package com.littlez.uiautomator;

import android.app.Instrumentation;
import android.content.Context;

import com.littlez.uiautomator.util.LogUtil;

import junit.framework.TestCase;


import java.util.Random;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

/**
 * created by xiaozhi
 * <p>
 * Date 2019/12/3
 */
public class A02ShuaBaotest extends TestCase {

    /*app 名字*/
    private String appName = "刷宝短视频";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {

            A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
            A00UtilTest.errorCount = 0;//重置错误次数

            while (true) {

                UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.jm.video:id/image_view"));
                UiObject uiGold = new UiObject(new UiSelector().resourceId("com.jm.video:id/constraintLayout_gold"));
                UiObject uiBalance = new UiObject(new UiSelector().resourceId("com.jm.video:id/constraintLayout_balance"));

                if (uiHeart.exists()) {//是首页
                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 10) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 85) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Thread.sleep(15000);//播放 时长
                    } else if (number <= 90) {//点击我的
                        UiObject uiMe =
                                new UiObject(new UiSelector().resourceId("com.jm.video:id/tv_tab_title").text("我"));
                        uiMe.click();
                    } else {//3点击心
                        if (uiHeart.exists()) uiHeart.click();
                    }
                } else if (uiGold.exists() && uiBalance.exists()) {//是我的界面
                    UiObject uiHome =
                            new UiObject(new UiSelector().resourceId("com.jm.video:id/tv_tab_title").text("首页"));
                    uiHome.click();
                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiPrivacy = new UiObject(new UiSelector().resourceId("com.jm.video:id/btn_privacy_action"));
                    if (uiPrivacy.exists()) {//用户协议
                        uiPrivacy.click();
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