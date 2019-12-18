package com.littlez.uiautomator;

import android.app.Instrumentation;
import android.content.Context;
import android.text.TextUtils;

import com.littlez.uiautomator.util.LogUtil;

import junit.framework.TestCase;


import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;

/**
 * created by xiaozhi
 * <p>这个是  进行全部任务的测试用例
 * Date 2019/12/18
 */
public class AtotalTest extends TestCase {


    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        // 获取上下文
        Context context = instrumentation.getContext();

        LogUtil.e("总计测试用例  运行了");
        int count = 0;


        try {

            
            while (true) {

                LogUtil.e("我运行了" + (count++));
                Thread.sleep(1000);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
