package com.littlez.uiautomator;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.littlez.uiautomator.util.LogUtil;

import junit.framework.TestCase;

import java.util.Random;

/**
 * created by xiaozhi
 * <p>
 * Date 2019/12/3
 */
public class A04YinLangtest extends TestCase {

    /*app 名字*/
    private String appName = "音浪短视频";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
            A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
            A00UtilTest.errorCount = 0;//重置错误次数

            while (appRun) {
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.video.yl:id/action_bar_root"));//提现栏
                UiObject uiGoWithdraw = new UiObject(new UiSelector().resourceId("com.video.yl:id/go_withdraw"));//我的 金钱界面
                UiObject uiRewardButton = new UiObject(new UiSelector().resourceId("com.video.yl:id/tv_rewardButton"));//看广告获取更多金币


                if (uiHome.exists()) {//是首页
                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 3) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 97) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Random rr = new Random();
                        Thread.sleep((30 + rr.nextInt(15) + 1) * 1000);//播放 时长
                    } else if (number <= 100) {//点击我的
                        UiObject uiMe = new UiObject(new UiSelector().resourceId("com.video.yl:id/tab_text").text("我"));
                        if (uiMe.exists()) uiMe.click();
                    }
                } else if (uiGoWithdraw.exists()) {//是我的界面
                    if (uiHome.exists()) uiHome.click();
                } else if (uiRewardButton.exists()) {//看广告获取更多
                    uiRewardButton.click();
                    UiObject uiAdvClose = new UiObject(new UiSelector().resourceId("com.video.yl:id/tt_video_ad_close_layout"));
                    UiObject uiAdvClose02 = new UiObject(new UiSelector().resourceId("com.video.yl:id/reward_ad_close"));
                    A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, uiAdvClose02, 50);
                } else {//处理异常情况
                    UiObject uiUpdateCancel = new UiObject(new UiSelector().resourceId("com.video.yl:id/btn_left"));//跟新取消
                    UiObject uiUpdateDown = new UiObject(new UiSelector().resourceId("com.video.yl:id/btn_right"));//跟新下载
                    UiObject uiadv = new UiObject(new UiSelector().resourceId("com.video.yl:id/tt_video_ad_close_layout"));
                    UiObject uiadv02 = new UiObject(new UiSelector().resourceId("com.video.yl:id/reward_ad_close"));
                    if (uiadv.exists()) {//用户协议
                        uiadv.click();
                    } else if (uiadv02.exists()) {//用户协议
                        uiadv02.click();
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