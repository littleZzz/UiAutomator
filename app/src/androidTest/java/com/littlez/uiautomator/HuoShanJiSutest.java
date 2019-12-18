package com.littlez.uiautomator;

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Rect;

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
 * <p>火山极速版
 * Date 2019/12/3
 */
public class HuoShanJiSutest extends TestCase {


    /*app 名字*/
    private String appName = "火山极速版";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
//        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(/*instrumentation*/);
//        Context context = instrumentation.getContext();  // 获取上下文

//        LogUtil.e("我开始运行了");
        int count = 0;

        try {

            while (true) {

//                LogUtil.e("我运行了" + (count++));

                Thread.sleep(1000);

                //首页
                UiObject uiFirstHome = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/qb"));
                UiObject uiHome01 = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/qe"));
                UiObject uiHome02 = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/uq"));
                //心
                UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/o3"));

                UiObject uiHomeAward = new UiObject(new UiSelector().description("谢谢参与").index(2));


                if (uiHome01.exists()) {//是首页

                    if (uiHomeAward.exists()) {//还有一个抽奖页面
                        Rect bounds = uiHomeAward.getBounds();
                        uiDevice.click(bounds.right - 50, bounds.bottom + 50);
                    } else {
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        /*随机数 进行判断 点击心或者滑动到下一个视频*/
                        if (number <= 10) {//上滑
                            uiDevice.swipe(534, 802, 400, 1200, 2);
                        } else if (number <= 95) {//下滑
                            uiDevice.swipe(400, 1200, 534, 802, 2);
                            Thread.sleep(8000);//播放 时长
                        } else {//3点击心
                            if (uiHeart.exists()) uiHeart.click();
                        }
                    }

                } else if (uiFirstHome.exists()) {//这一个有第一个首页的说法
                    uiFirstHome.click();
                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiRootT = new UiObject(new UiSelector().resourceId("com.kingroot.kinguser:id/title").text("UiAutomator"));
                    UiObject uiRootAllow = new UiObject(new UiSelector().resourceId("com.kingroot.kinguser:id/button_right"));
                    UiObject uiCloseBtn = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/positive"));

                    if (uiRootT.exists() && uiRootAllow.exists()) {//root 权限获取
                        uiRootAllow.click();
                    } else if (uiCloseBtn.exists()) {//青少年保护弹框 TODO 青少年保护 还需要自己观看一下
                        uiCloseBtn.click();
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