package com.littlez.uiautomator;

import android.app.Instrumentation;
import android.content.Context;

import com.littlez.uiautomator.util.LogUtil;

import junit.framework.TestCase;

import org.junit.Test;

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

                Random r = new Random();
                int number = r.nextInt(100) + 1;
                /*随机数 进行判断 点击心或者滑动到下一个视频*/
                if (number <= 20) {
                    // 点击设备返回按钮
                    uiDevice.swipe(534, 802, 400, 1500, 1);
                    Thread.sleep(20000);
                } else if (number <= 70) {//50的几率下滑
                    // 点击设备返回按钮
                    uiDevice.swipe(400, 1500, 534, 802, 1);
                    Thread.sleep(35000);
                } else {//30的几率点击心
                    UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.jm.video:id/image_view"));
                    uiHeart.click();
                    Thread.sleep(5000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


}