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
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);
//        Context context = instrumentation.getContext();  // 获取上下文

//        LogUtil.e("我开始运行了");
        int count = 0;
        int errorCount = 0;//记录异常强制启动次数  超过10次就关闭应用
        try {

            while (true) {

//                LogUtil.e("我运行了" + (count++));

                Thread.sleep(1000);

                //首页
                UiObject uiFirstHome = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/qb"));
                UiObject uiHome01 = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/qe").description("返回"));
                UiObject uiHome02 = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/uq"));
                //心
                UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/o3"));

                UiObject uiHomeAward = new UiObject(new UiSelector().description("谢谢参与"));


                if (uiHome01.exists()) {//是首页（用是否有这个来判断）

                    if (uiHomeAward.exists()) {//还有一个抽奖页面
                        Rect bounds = uiHomeAward.getBounds();
                        uiDevice.click(bounds.right - 50, bounds.bottom + 50);
                        Thread.sleep(5000);//播放 时长
                        uiHome01.click();
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
                    UiObject uiCloseBtn =
                            new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/qe").text("我知道了"));
                    UiObject uiLeftAllow = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/a45"));
                    UiObject uiMyEarn = new UiObject(new UiSelector().description("我的收益").className("android.view.View"));


                    if (uiRootT.exists() && uiRootAllow.exists()) {//root 权限获取
                        uiRootAllow.click();
                    } else if (uiCloseBtn.exists()) {//青少年保护弹框
                        uiCloseBtn.click();
                    } else if (uiLeftAllow.exists() && uiMyEarn.exists()) {//我的收益
                        uiLeftAllow.click();
                    } else {//最终的强制搞一波

                        if (errorCount > 6) {//这个强制方法走了10次  出现什么异常问题了 直接关闭应用  重新启动
                            uiDevice.pressHome();
                            Thread.sleep(500);
                            uiDevice.pressRecentApps();
                            Thread.sleep(500);
                            UiObject appClearAll =
                                    new UiObject(new UiSelector().resourceId("com.android.systemui:id/clearAnimView"));
                            if (appClearAll.exists()) {
                                appClearAll.click();
                                errorCount = 0;//重置失败次数
                                Thread.sleep(500);
                            }
                        }

                        uiDevice.pressHome();
                        Thread.sleep(500);
                        uiDevice.pressRecentApps();
                        Thread.sleep(500);
                        UiObject appLaunch = new UiObject(new UiSelector().descriptionContains(appName)
                                .className("android.widget.FrameLayout"));
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
                        errorCount++;//增加异常启动次数
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}