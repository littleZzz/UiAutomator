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
 * <p>快手极速的
 * Date 2019/12/3
 */
public class A12KuaiShouJiStest extends TestCase {

    /*app 名字*/
    private String appName = "快手极速版";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
//            A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
//            A00UtilTest.errorCount = 0;//重置错误次数

            while (appRun) {

                UiObject uiRedFloat = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/redFloat"));//红包倒计时器id
                UiObject uiLiveTxt = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/nebula_hot_live_count_down_cancel"));//点击进入直播间文字 id
                UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/like_button"));//heart
//                UiObject uiHome = new UiObject(new UiSelector().resourceId(""));

                if (uiRedFloat.exists() || uiLiveTxt.exists()) {//是看视频接麦你
                    int number = A00UtilTest.getRandom(100);
                    if (number <= 3) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 95) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Thread.sleep((3 + A00UtilTest.getRandom(15)) * 1000);//播放 时长
                    } else {//点击心
                        if (uiHeart.exists()) uiHeart.click();
                    }
                } /*else if (uiViewpager.exists()) {//是检测页面
                    if (uiHome.exists()) uiHome.click();
                }*/ else {//处理异常情况
                    UiObject uiYoungDia = new UiObject(new UiSelector().resourceId("com.kuaishou.nebula:id/positive"));//青少年弹框

                    if (uiYoungDia.exists()) {//
                        uiYoungDia.click();
                    }/* else if (uiNet.exists()) {//
                        uiNet.click();
                        Thread.sleep(2000);
                    } */ else {//最终的强制搞一波
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