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
public class ShuaBaotest extends TestCase {

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
//        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(/*instrumentation*/);
//         获取上下文
//        Context context = instrumentation.getContext();

//        LogUtil.e("我开始运行了");
        int count = 0;

        try {

            while (true) {

//                LogUtil.e("我运行了" + (count++));

                Thread.sleep(1500);

//                SPUtils.get()

                UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.jm.video:id/image_view"));
                UiObject uiShare = new UiObject(new UiSelector().resourceId("com.jm.video:id/share"));

                UiObject uiGold = new UiObject(new UiSelector().resourceId("com.jm.video:id/constraintLayout_gold"));
                UiObject uiBalance = new UiObject(new UiSelector().resourceId("com.jm.video:id/constraintLayout_balance"));

                if (uiHeart.exists() && uiShare.exists()) {//是首页
                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 10) {//上滑
                        uiDevice.swipe(534, 802, 400, 1200, 2);
                    } else if (number <= 85) {//下滑
                        uiDevice.swipe(400, 1200, 534, 802, 2);
                        Thread.sleep(8000);//播放 时长
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
                    } else {
                        uiDevice.pressHome();
                        Thread.sleep(500);
                        uiDevice.pressRecentApps();
                        Thread.sleep(500);
                        UiObject appLaunch = new UiObject(new UiSelector().text("刷宝短视频"));
                        if (appLaunch.exists()) {//没有彻底挂掉

                            appLaunch.click();
                            Thread.sleep(500);
                            uiDevice.swipe(400, 1200, 534, 802, 2);
                            Thread.sleep(1000);

                        } else {//彻底挂掉了  重启
                            uiDevice.pressHome();
                            Thread.sleep(500);
                            //启动应用
                            UiObject uiVideo = new UiObject(new UiSelector().text("刷宝短视频"));
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