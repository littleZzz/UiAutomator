package com.littlez.uiautomator;

import android.app.Instrumentation;
import android.content.Context;

import com.littlez.uiautomator.util.LogUtil;

import junit.framework.TestCase;

import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

/**
 * created by xiaozhi
 * <p>
 * Date 2019/12/3
 */
public class Uitest extends TestCase {

    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);
        // 获取上下文
        Context context = instrumentation.getContext();

        LogUtil.e("我开始运行了");
        int count = 0;
        try {
            while (true) {
                LogUtil.e("我运行了" + (count++));
                Thread.sleep(500);
                // 点击设备返回按钮
                uiDevice.pressBack();

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}