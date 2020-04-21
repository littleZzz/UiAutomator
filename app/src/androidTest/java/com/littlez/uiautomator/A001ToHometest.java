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
 * <p>回到首页  并进行内存清理的测试用例
 * Date 2019/12/3
 */
public class A001ToHometest extends TestCase {

    /*app 名字*/
    private String appName = "豌豆荚";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
        try {
            while (appRun) {
                UiObject uiHomeLive = new UiObject(new UiSelector().resourceId("com.android.launcher3:id/live"));
                UiObject uiMe = new UiObject(new UiSelector().resourceId("android:id/text1").text("我的"));
                UiObject uiGC = new UiObject(new UiSelector().resourceId("com.wandoujia.phoenix2:id/ke").text("垃圾清理"));
                UiObject uiGCOver = new UiObject(new UiSelector().resourceId("com.wandoujia.phoenix2:id/vo").text("已清理"));

                if (uiHomeLive.exists()) {//在手机主页
                    //启动豌豆荚
                    UiObject uiVideo = new UiObject(new UiSelector().text(appName));
                    if (uiVideo.exists()) {
                        uiVideo.click();
                        Thread.sleep(10000);
                    }
                } else if (uiMe.exists() && !uiMe.isSelected()) {//在豌豆荚
                    uiMe.click();
                } else if (uiGC.exists()) {//垃圾清理
                    uiGC.click();
                    UiObject uiOneTimesGC =
                            new UiObject(new UiSelector().resourceId("com.wandoujia.phoenix2:id/v6").textContains("一键清理"));
                    A00UtilTest.backUntilObjOrTime(uiDevice, uiOneTimesGC, 50);
                } else if (uiGCOver.exists()) {//已清理界面
                    uiDevice.pressHome();
                    break;
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