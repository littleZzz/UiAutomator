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

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);
        int count = 0;
        try {
            while (count <= 3) {
                UiObject uiHomeLive = new UiObject(new UiSelector().resourceId("com.android.launcher3:id/live"));
                if (uiHomeLive.exists()) {
                    count++;
                } else {
                    uiDevice.pressHome();
                }
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}