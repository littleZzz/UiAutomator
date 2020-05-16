package com.littlez.uiautomator;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import junit.framework.TestCase;

/**
 * created by xiaozhi
 * <p>酷铃声 的测试用例
 * * Date 2019/12/3
 */
public class A10TianTianSmallTVtest extends TestCase {

    /*app 名字*/
    private String appName = "天天短视频";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
            A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
            A00UtilTest.errorCount = 0;//重置错误次数

            UiObject uiToHomePage = new UiObject(new UiSelector().resourceId("com.tiantian.video:id/home_hint").text("首页"));
            UiObject uiToMissionPage = new UiObject(new UiSelector().resourceId("com.tiantian.video:id/money_hint").text("赚钱"));
            UiObject uiToMePage = new UiObject(new UiSelector().resourceId("com.tiantian.video:id/mine_hint").text("我的"));
            UiObject uiHomePage = new UiObject(new UiSelector().resourceId("com.tiantian.video:id/home_progress"));//首页进度框
            UiObject uiSmallTVPage = new UiObject(new UiSelector().resourceId("com.tiantian.video:id/rcL_vide_recyclerView"));
            UiObject uiMissionPage = new UiObject(new UiSelector().resourceId("com.tiantian.video:id/sign"));//签到文字 id
            UiObject uiMePage = new UiObject(new UiSelector().resourceId("com.tiantian.video:id/nickname"));//名称id
            UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.tiantian.video:id/good_count_layout"));//

            UiObject uiAdvClose = new UiObject(new UiSelector().className("android.webkit.WebView").text("腾讯社交联盟广告"));//广告结束后的 重新播放文案

            while (appRun) {

                if (uiHomePage.exists()) {//首页
                    int random = A00UtilTest.getRandom(100);
                    if (random <= 3) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (random <= 92) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Thread.sleep((15 + A00UtilTest.getRandom(15)) * 1000);//播放 时长
                    } else if (random <= 96) {//跳转赚钱
                      if(uiToMissionPage.exists())  uiToMissionPage.click();
                        Thread.sleep(3000);
                    } else if (random <= 98) {//跳转我
                        if(uiToMePage.exists())  uiToMePage.click();
                        Thread.sleep(3000);
                    } else {//3点击心
                        if (uiHeart.exists()) uiHeart.click();
                    }
                } else if (uiMissionPage.exists()) {//赚钱页面
                    uiMissionPage.click();
                    Thread.sleep(5000);
                    uiDevice.pressBack();
                    Thread.sleep(5000);
                    if (uiToHomePage.exists()) uiToHomePage.click();
                } else if (uiMePage.exists()) {//我的页面
                    Thread.sleep(5000);
                    if (uiToHomePage.exists()) uiToHomePage.click();
                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiDia = new UiObject(new UiSelector().resourceId("com.tiantian.video:id/iv_signIn_close"));//签到 关闭

                    if (uiDia.exists()) {//签到 关闭
                        uiDia.click();
                    } else {//最终的强制搞一波
                        A00UtilTest.baseMethod(uiDevice, 1, appName, new A00UtilTest.MyCallBack() {
                            @Override
                            public void callback(boolean isStop) {
                                appRun = false;//出问题了停止运行
                            }
                        });
                    }
                }
                Thread.sleep(1000);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}