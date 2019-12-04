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


                UiObject uiHeart = uiDevice.findObject(new UiSelector().resourceId("com.jm.video:id/image_view"));
                UiObject uiShare = uiDevice.findObject(new UiSelector().resourceId("com.jm.video:id/share"));

                UiObject uiGold = uiDevice.findObject(new UiSelector().resourceId("com.jm.video:id/constraintLayout_gold"));
                UiObject uiBalance = uiDevice.findObject(new UiSelector().resourceId("com.jm.video:id/constraintLayout_balance"));

                if (uiHeart.exists() && uiShare.exists()) {//是首页
                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 15) {//上滑
                        uiDevice.swipe(534, 802, 400, 1200, 2);
                        Thread.sleep(2000);
                    } else if (number <= 70) {//下滑
                        uiDevice.swipe(400, 1200, 534, 802, 2);
                        Thread.sleep(10000);//播放 时长
                    } else if (number <= 85) {//点击我的
                        UiObject uiMe =
                                uiDevice.findObject(new UiSelector().resourceId("com.jm.video:id/tv_tab_title").text("我"));
                        uiMe.click();
                        Thread.sleep(3000);
                    } else {//30的几率点击心
                        if (uiHeart.exists()) uiHeart.click();
                        Thread.sleep(3000);
                    }
                } else if (uiGold.exists() && uiBalance.exists()) {//是我的界面
                    UiObject uiHome =
                            uiDevice.findObject(new UiSelector().resourceId("com.jm.video:id/tv_tab_title").text("首页"));
                    uiHome.click();
                    Thread.sleep(2000);
                } /*else {
                    uiDevice.pressBack();
                }*/

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}