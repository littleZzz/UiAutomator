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
 * <p>开机启动的  测试用例
 * Date 2019/12/3
 */
public class A01BootLuanchertest extends TestCase {

    /*app 名字*/
    private String appName = "小智阅读";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
        try {
            while (appRun) {
                UiObject uiLockScreen = new UiObject(new UiSelector().resourceId("com.android.systemui:id/keyguard_notification_scroll"));
                UiObject uiMainPage = new UiObject(new UiSelector().resourceId("com.android.launcher3:id/live"));//手机的主页
                UiObject uiAppMainPage = new UiObject(new UiSelector().resourceId("com.littlez.uiautomator:id/recycleView"));

                if (uiLockScreen.exists()) {//在手机锁屏页面
                    //上滑 打开app
                    A00UtilTest.swipDown(uiDevice);
                } else if (uiMainPage.exists()) {//在手机的主页面
                    uiDevice.pressHome();
                    //启动应用
                    UiObject uiVideo = new UiObject(new UiSelector().text(appName));
                    if (uiVideo.exists()) {
                        uiVideo.click();
                        Thread.sleep(10000);
                    }
                } else if (uiAppMainPage.exists()) {//在app主页面
                    UiObject uiCheckAll = new UiObject(new UiSelector().resourceId("com.littlez.uiautomator:id/cbCheckAll"));
                    if (uiCheckAll.exists() && !uiCheckAll.isChecked()) uiCheckAll.click();
                    Thread.sleep(1000);
                    UiObject uiMakeUpTime = new UiObject(new UiSelector().resourceId("com.littlez.uiautomator:id/btnMakeUpTime"));
                    if (uiMakeUpTime.exists()) uiMakeUpTime.exists();
                    Thread.sleep(1000);
                    UiObject uiStartService = new UiObject(new UiSelector().resourceId("com.littlez.uiautomator:id/btnStartServe"));
                    if (uiStartService.exists()) {
                        uiStartService.exists();
                        appRun=false;
                        break;
                    }
                    Thread.sleep(1000);

                } else {
                    A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
                    uiDevice.pressHome();
                }
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}