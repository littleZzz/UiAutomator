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
 * <p>米读极速版  测试用例
 * Date 2019/12/3
 */
public class A22MiDuJiSutest extends TestCase {

    /*app 名字*/
    private String appName = "米读极速版";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置次数
        long startTime = System.currentTimeMillis();//开始时间
        try {
            while (true) {
                //主页
                UiObject uiHome = new UiObject(new UiSelector().className("android.widget.RadioButton").text("书城"));
                UiObject uiBooks = new UiObject(new UiSelector().className("android.widget.RadioButton").text("书架"));
                UiObject uiWelfare = new UiObject(new UiSelector().className("android.widget.RadioButton").text("福利"));

                if (uiHome.exists() && uiHome.isChecked()) {//是主页 书城
                    if (uiBooks.exists()) uiBooks.click();

                } else if (uiBooks.exists() && uiBooks.isChecked()) {//是书架界面
                    UiObject uiRead = new UiObject(new UiSelector().resourceId("com.lechuan.mdwz:id/ajq"));
                    uiRead.click();
                    Thread.sleep(2000);

                } else if (new UiObject(new UiSelector().resourceId("com.lechuan.mdwz:id/a_q")).exists()) {//是阅读界面
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - startTime >= 30 * 60 * 1000) {//回到主页 获取奖励
                        uiDevice.pressBack();
                        Thread.sleep(2000);
                        if (uiWelfare.exists()) {
                            uiWelfare.click();
                            startTime = currentTimeMillis;
                        }
                    } else {
                        uiDevice.swipe(960, 1600, 300, 1610, 10);
                        Random r = new Random();
                        int number = r.nextInt(5) + 1;
                        Thread.sleep((15 + number) * 1000);
                    }

                } else if (uiWelfare.exists() && uiWelfare.isChecked()) {//是福利页面
                    welare(uiDevice);//福利还没有弄

                } else {//处理异常情况
                    UiObject uiClose = new UiObject(new UiSelector().resourceId("com.lechuan.mdwz:id/t"));

                    if (uiClose.exists()) {
                        uiClose.click();
                    } else {//最终的强制搞一波
                        A00UtilTest.baseMethod(uiDevice, 1, appName);
                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //福利页面处理
    private void welare(UiDevice uiDevice) throws UiObjectNotFoundException, InterruptedException {
        int count = 0;
        while (count <= 5) {
            UiObject uiGet = new UiObject(new UiSelector().className("android.view.View").text("领取"));
            if (uiGet.exists()) uiGet.click();
            else {
                uiDevice.swipe(620, 1300, 700, 830, 15);
                count++;
            }
            Thread.sleep(2000);
        }
        UiObject uiBooks = new UiObject(new UiSelector().className("android.widget.RadioButton").text("书架"));
        uiBooks.click();
    }

}