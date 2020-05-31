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
 * <p>火牛视频的  测试用例
 * Date 2019/12/3
 */
public class A63HuoNiutest extends TestCase {

    /*app 名字*/
    private String appName = "火牛视频";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
//            A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
//            A00UtilTest.errorCount = 0;//重置错误次数

            UiObject uiHome = new UiObject(new UiSelector().resourceId("com.waqu.android.firebull:id/tv_tab").text("首页"));//底部栏
            UiObject uiMe = new UiObject(new UiSelector().resourceId("com.waqu.android.firebull:id/tv_tab").text("我的"));//底部栏
            UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.waqu.android.firebull:id/heart_button"));
            UiObject uiClickGet = new UiObject(new UiSelector().resourceId("com.waqu.android.firebull:id/tv_video_list_temp_num").text("点击领取"));//广告 点击临朐

            while (appRun) {

                if (uiHome.exists() && uiHome.isSelected()) {//是首页
                    if (uiClickGet.exists())uiClickGet.click();//领取广告奖励
                    int number = A00UtilTest.getRandom(100);
                    if (number <= 3) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 95) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Thread.sleep((10 + A00UtilTest.getRandom(18)) * 1000);//播放 时长
                    } else if (number <= 98) {//点击heart
                        if (uiHeart.exists()) uiHeart.click();
                    }else if (number <= 100) {//点击我的
                        if (uiMe.exists()) uiMe.click();
                    }
                } else if (uiMe.exists() && uiMe.isSelected()) {//是我的界面
                    Thread.sleep(2000);
                    if (uiHome.exists()) uiHome.click();
                }  else {//处理异常情况

                    UiObject uidialog01 = new UiObject(new UiSelector().resourceId("com.waqu.android.firebull:id/iv_close"));//新人弹框
                    if (uidialog01.exists()) {//新人弹框
                        uidialog01.click();
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