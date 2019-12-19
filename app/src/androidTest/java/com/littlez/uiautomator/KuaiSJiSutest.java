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
public class KuaiSJiSutest extends TestCase {


    /*app 名字*/
    private String appName = "快手极速版";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);
        // 获取上下文
//        Context context = instrumentation.getContext();

//        LogUtil.e("我开始运行了");
        int count = 0;

        try {

            while (true) {

//                LogUtil.e("我运行了" + (count++));
                Thread.sleep(1000);

                //首页
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/left_btn"));
                //心
                UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/like_icon"));
                //关注
                UiObject uiFollow = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/follow_layout"));

                if (uiHome.exists()) {//是首页
                    /*处理异常情况*/
                    UiObject uiCloseBtn = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/positive"));
                    UiObject uiSignIn = new UiObject(new UiSelector().description("立即签到").className("android.view.View"));
                    UiObject uiGoldIncome = new UiObject(new UiSelector().description("金币收益").className("android.view.View"));
                    UiObject uiMoneyIncome = new UiObject(new UiSelector().description("现金收益").className("android.view.View"));

                    if (uiCloseBtn.exists()) {//青少年保护弹框
                        uiCloseBtn.click();
                    } else if (uiSignIn.exists()) {//签到
                        uiSignIn.click();
                    } else if (uiMoneyIncome.exists() && uiGoldIncome.exists()) {//首页界面
                        uiDevice.pressBack();
                        Thread.sleep(2000);
                    } else {
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        /*随机数 进行判断 点击心或者滑动到下一个视频*/
                        if (number <= 10) {//上滑
                            uiDevice.swipe(534, 802, 400, 1200, 2);
                        } else if (number <= 94) {//下滑
                            uiDevice.swipe(400, 1200, 534, 802, 2);
                            Thread.sleep(8000);//播放 时长
                        } else if (number <= 97) {//3点击心
                            if (uiHeart.exists()) uiHeart.click();
                        } else {
                            if (uiFollow.exists()) uiFollow.click();
                        }
                    }
                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiRootT = new UiObject(new UiSelector().resourceId("com.kingroot.kinguser:id/title").text("UiAutomator"));
                    UiObject uiRootAllow = new UiObject(new UiSelector().resourceId("com.kingroot.kinguser:id/button_right"));

                    if (uiRootT.exists() && uiRootAllow.exists()) {//root 权限获取
                        uiRootAllow.click();
                    } else {//最终的强制搞一波
                        uiDevice.pressHome();
                        Thread.sleep(500);
                        uiDevice.pressRecentApps();
                        Thread.sleep(500);
                        UiObject appLaunch = new UiObject(new UiSelector().text(appName));
                        if (appLaunch.exists()) {//没有彻底挂掉
                            appLaunch.click();
                            Thread.sleep(500);
                        } else {//彻底挂掉了  重启
                            uiDevice.pressHome();
                            Thread.sleep(500);
                            //启动应用
                            UiObject uiVideo = new UiObject(new UiSelector().text(appName));
                            if (uiVideo.exists()) {
                                uiVideo.click();
                                Thread.sleep(2000);
                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}