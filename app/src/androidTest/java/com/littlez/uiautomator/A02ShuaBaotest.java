package com.littlez.uiautomator;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;

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

                UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.jm.video:id/image_view"));
                UiObject uiName = new UiObject(new UiSelector().resourceId("com.jm.video:id/name"));
                UiObject uiGold = new UiObject(new UiSelector().resourceId("com.jm.video:id/constraintLayout_gold"));
                UiObject uiBalance = new UiObject(new UiSelector().resourceId("com.jm.video:id/constraintLayout_balance"));
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.jm.video:id/tv_tab_title").text("首页"));
                UiObject uiTask = new UiObject(new UiSelector().resourceId("com.jm.video:id/tv_tab_title").text("任务"));
                UiObject uiSign = new UiObject(new UiSelector().className("android.widget.Button").description("立即签到").index(10));
                UiObject uiSeeTVToSign = new UiObject(new UiSelector().className("android.view.View").description("看视频签到"));
                
                //首页 任务 我的都有 但是首页用的其他方式首先判断  所以可以用他来判断在其他不想要的界面 好回到首页
                UiObject uiViewpager = new UiObject(new UiSelector().resourceId("com.jm.video:id/mmViewPager"));

                if (uiHeart.exists() || uiName.exists()) {//是首页
                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 3) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 95) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Thread.sleep(12000);//播放 时长
                    } else if (number <= 98) {//点击任务 去做签到
                        uiTask.click();//900  530
                        Thread.sleep(5000);
                    } else {//3点击心
                        if (uiHeart.exists()) uiHeart.click();
                    }
                } else if (uiSign.exists()) {//是任务界面且有立即签到字样  执行签到任务
                    uiSign.click();
                    Thread.sleep(5000);
                    A00UtilTest.backUntilObjOrTime(uiDevice, uiSeeTVToSign, 30);
                    UiObject uiAdcClose = new UiObject(new UiSelector().resourceId("com.jm.video:id/tt_video_ad_close_layout"));
                    A00UtilTest.backUntilObjOrTime(uiDevice, uiAdcClose, 60);
                    if (uiHome.exists()) uiHome.click();
                } else if (uiViewpager.exists()) {//是我的或者任务界面
                    if (uiHome.exists()) uiHome.click();
                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiPrivacy = new UiObject(new UiSelector().resourceId("com.jm.video:id/btn_privacy_action"));
                    UiObject uiNet = new UiObject(new UiSelector().resourceId("com.jm.video:id/empty_button"));//无网络
                    UiObject uiTimeAward = new UiObject(new UiSelector().resourceId("com.jm.video:id/tv_go"));//时段奖励
                    UiObject uinotifiCancle = new UiObject(new UiSelector().resourceId("com.jm.video:id/cancel"));//通知权限关闭
                    UiObject uiInviteDiaClose = new UiObject(new UiSelector().resourceId("com.jm.video:id/imgClose"));//去邀请弹框

                    if (uiPrivacy.exists()) {//用户协议
                        uiPrivacy.click();
                    } else if (uiNet.exists()) {//用户协议
                        uiNet.click();
                        Thread.sleep(2000);
                    } else if (uiTimeAward.exists()) {//时段奖励
                        uiTimeAward.click();
                    } else if (uinotifiCancle.exists()) {//通知权限关闭
                        uinotifiCancle.click();
                        Thread.sleep(3000);
                        uiTask.click();//前往任务进行签到
                        Thread.sleep(5000);
                    } else if (uiInviteDiaClose.exists()) {//去邀请弹框
                        uiInviteDiaClose.click();
                        Thread.sleep(5000);
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