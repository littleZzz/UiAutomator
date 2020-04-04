package com.littlez.uiautomator;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.littlez.uiautomator.util.LogUtil;

import junit.framework.TestCase;

import java.io.File;
import java.util.Random;

/**
 * 非常重要的 截图动态ui的
 */
public class A000ScreenTest extends TestCase {


    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();

        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {

            try {
                uiDevice.dumpWindowHierarchy("window_dump.uix") ;
                uiDevice.dumpWindowHierarchy(new File("/sdcard/screen.uix"));
                uiDevice.takeScreenshot(new File("/sdcard/screen.png"));

            } catch (Exception e) {
                LogUtil.e(e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(e.toString());
        }
    }


}
