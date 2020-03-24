package com.littlez.uiautomator;

import android.app.Instrumentation;

import junit.framework.TestCase;

import java.util.Random;

import androidx.test.espresso.action.Swipe;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

/**
 * created by xiaozhi
 * <p>趣头条 测试用例
 * Date 2019/12/3
 */
public class A06QuTouTiaotest extends TestCase {

    /*app 名字*/
    private String appName = "趣头条";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {

            A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
            A00UtilTest.errorCount = 0;//重置/

            while (true) {

                //主页
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/mh"));
                //心
                UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/qu"));

                if (uiHome.exists()) {//是主页

                    /*阅读奖励*/
                    UiObject uiReadingAward = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/aw_"));
                    UiObject uiTV = new UiObject(new UiSelector().text("小视频").className("android.widget.TextView"));
                    UiObject uiMission = new UiObject(new UiSelector().text("任务").className("android.widget.TextView"));
                    UiObject uiMe = new UiObject(new UiSelector().text("我的").className("android.widget.TextView"));

                    if (!uiTV.isSelected()) {//不在播放视频界面
                        uiTV.click();
                        Thread.sleep(500);
                    } else if (uiReadingAward.exists()) {
                        uiReadingAward.click();
                    } else {
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        /*随机数 进行判断 点击心或者滑动到下一个视频*/
                        if (number <= 5) {//上滑
                            A00UtilTest.swipUp(uiDevice);
                        } else if (number <= 88) {//下滑
                            A00UtilTest.swipDown(uiDevice);
                            Thread.sleep(8000);//播放 时长
                        } else if (number <= 92) {
                            uiMission.click();
                            Thread.sleep(500);
                        } else if (number <= 95) {
                            uiMe.click();
                            Thread.sleep(500);
                        } else {//3点击心
                            if (uiHeart.exists()) uiHeart.click();
                        }
                    }

                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiClose = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/a5n"));

                    if (uiClose.exists()) {
                        uiClose.click();
                    } else {//最终的强制搞一波
                        A00UtilTest.baseMethod(uiDevice, 1, appName);
                    }
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}