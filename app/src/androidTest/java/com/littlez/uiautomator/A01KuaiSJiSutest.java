package com.littlez.uiautomator;

import android.app.Instrumentation;
import android.content.Context;
import android.text.TextUtils;

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
 * <p>快手极速版
 * Date 2019/12/3
 */
public class A01KuaiSJiSutest extends TestCase {

    /*app 名字*/
    private String appName = "快手极速版";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
            LogUtil.e("1");
//            A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
//            A00UtilTest.errorCount = 0;//设置errorcount为0
            LogUtil.e("2");


            while (true) {
                LogUtil.e("5");

                //首页
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/left_btn"));

                if (uiHome.exists()) {//是首页
                    LogUtil.e("6");

                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 10) {//上滑
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 94) {//下滑
                        A00UtilTest.swipDown(uiDevice);
                        Thread.sleep(5000);//播放 时长
                    } else if (number <= 97) {//3点击心
                        //心
                        UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/like_icon"));
                        if (uiHeart.exists()) uiHeart.click();
                    } else {
                        //关注
                        UiObject uiFollow = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/follow_layout"));
                        if (uiFollow.exists()) uiFollow.click();
                    }

                    /*处理异常情况*/
                    UiObject uiCloseBtn = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/positive"));
                    UiObject uiSignIn = new UiObject(new UiSelector().className("android.view.View").description("立即签到"));
                    UiObject uiWebView = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/webView"));
                    UiObject uiCloseBtn02 = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/close"));

                    if (uiCloseBtn.exists()) {//青少年保护弹框
                        uiCloseBtn.click();
                    } else if (uiCloseBtn02.exists()) {//邀请
                        uiCloseBtn02.click();
                    } else if (uiSignIn.exists()) {//签到
                        uiSignIn.click();
                    } else if (uiWebView.exists()) {
                        uiDevice.pressBack();
                        Thread.sleep(2000);
                    }

                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiReplay = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/replay_ad_video"));
                    LogUtil.e("7");

                    if (uiReplay.exists()) {
                        A00UtilTest.swipDown(uiDevice);
                    } else {//最终的强制搞一波
                        A00UtilTest.baseMethod(uiDevice, 1, appName);
                    }
                }

                Thread.sleep(500);
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e.toString());
        }
    }


}