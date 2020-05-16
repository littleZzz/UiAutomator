package com.littlez.uiautomator;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import junit.framework.TestCase;

import java.util.Random;

/**
 * created by xiaozhi
 * <p>趣铃声
 * Date 2019/12/3
 */
public class A09QuLingShengtest extends TestCase {

    /*app 名字*/
    private String appName = "趣铃声";
    private boolean appRun = true;//appRun
    private boolean isFirstRun = true;//是否初次运行

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
            A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
            A00UtilTest.errorCount = 0;//重置错误次数

            UiObject uiRingPage = new UiObject(new UiSelector().resourceId("com.zheyun.bumblebee:id/tv_tab_name").text("铃声"));
            UiObject uiMusicPage = new UiObject(new UiSelector().resourceId("com.zheyun.bumblebee:id/tv_tab_name").text("音乐"));
            UiObject uiSmallTVPage = new UiObject(new UiSelector().resourceId("com.zheyun.bumblebee:id/tv_tab_name").text("小视频"));
            UiObject uiMissionPage = new UiObject(new UiSelector().resourceId("com.zheyun.bumblebee:id/tv_tab_name").text("任务"));
            UiObject uiMePage = new UiObject(new UiSelector().resourceId("com.zheyun.bumblebee:id/tv_tab_name").text("我的"));
            UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.zheyun.bumblebee:id/tv_like"));//铃声和小视频id 是一样的

            UiObject uiSeeTVGetMoney = new UiObject(new UiSelector().resourceId("com.zheyun.bumblebee:id/tv_confirm"));//看视频翻倍

            UiObject uiAdvPage = new UiObject(new UiSelector().className("com.qukan.media.player.renderview.TextureRenderView"));//广告页面
            UiObject uiAdvClose = new UiObject(new UiSelector().className("android.widget.TextView").text("点击重播"));//广告结束后的 重新播放文案

            while (appRun) {

                if (isFirstRun && uiSmallTVPage.exists()) {//从看小视频开始
                    uiSmallTVPage.click();
                    isFirstRun = false;
                }
                if ((uiRingPage.exists() && uiRingPage.isSelected()) ||
                        (uiSmallTVPage.exists() && uiSmallTVPage.isSelected())) {//是铃声 或者小视频页面
                    int random = A00UtilTest.getRandom(100);
                    if (random <= 3) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (random <= 94) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Thread.sleep((15 + A00UtilTest.getRandom(20)) * 1000);//播放 时长
                    } else if (random <= 96) {//跳转任务
                        uiMissionPage.click();
                        Thread.sleep(3000);
                    } else if (random <= 98) {//跳转我
                        uiMePage.click();
                        Thread.sleep(3000);
                    } else {//3点击心
                        if (uiHeart.exists()) uiHeart.click();
                    }
                } else if ((uiMissionPage.exists() && uiMissionPage.isSelected()) ||
                        (uiMePage.exists() && uiMePage.isSelected())) {//任务界面  或者我的界面
                    int random = A00UtilTest.getRandom(100);
                    if (random <= 50) {//去铃声
                        uiRingPage.click();
                        Thread.sleep(3000);
                    } else {//去视频
                        uiSmallTVPage.click();
                        Thread.sleep(3000);
                    }
                } else if (uiSeeTVGetMoney.exists()) {//看视频翻倍按钮
                    uiSeeTVGetMoney.click();//去点击观看广告
                    A00UtilTest.backUntilObjOrTimeToBack(uiDevice, uiAdvClose, 60);
                } else if (uiAdvPage.exists()) {//广告界面
                    if (uiAdvClose.exists()) uiDevice.pressBack();
                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiDia = new UiObject(new UiSelector().resourceId("com.zheyun.bumblebee:id/base_card_dialog_close"));//提现 和设置铃声dia
                    UiObject uiDia02 = new UiObject(new UiSelector().resourceId("com.zheyun.bumblebee:id/iv_close"));//看视频翻倍 dia 关闭
                    UiObject uiHonorDia = new UiObject(new UiSelector().resourceId("com.zheyun.bumblebee:id/base_card_dialog_close"));//荣誉勋章暂不领取

                    if (uiDia.exists()) {//提现 和设置铃声dia  看视频设置铃声dia
                        uiDia.click();
                    } else if (uiDia02.exists()) {//看视频翻倍 dia 关闭
                        uiDia02.click();
                    } else if (uiHonorDia.exists()) {//荣誉勋章暂不领取
                        uiHonorDia.click();
                    } else {//最终的强制搞一波
                        A00UtilTest.baseMethod(uiDevice, 1, appName, new A00UtilTest.MyCallBack() {
                            @Override
                            public void callback(boolean isStop) {
                                appRun = false;//出问题了停止运行
                            }
                        });
                    }
                }
                Thread.sleep(500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}