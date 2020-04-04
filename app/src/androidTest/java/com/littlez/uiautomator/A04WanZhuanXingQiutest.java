package com.littlez.uiautomator;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import junit.framework.TestCase;

import java.nio.IntBuffer;
import java.util.Random;

/**
 * created by xiaozhi
 * <p>玩转星球 测试用例
 * Date 2019/12/3
 */
public class A04WanZhuanXingQiutest extends TestCase {

    /*app 名字*/
    private String appName = "玩赚星球";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
            A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
            A00UtilTest.errorCount = 0;//重置
            while (true) {
                //首页
                UiObject uiNewsTask = new UiObject(new UiSelector().resourceId("com.planet.light2345:id/tv_task_button"));
                UiObject uiTvBack =
                        new UiObject(new UiSelector().resourceId("com.startnews.plugin:id/news2345_iv_back_small_video_detail"));
                if (uiNewsTask.exists()) {//是首页选中

                    uiNewsTask.click();
                    Thread.sleep(2000);
                    UiObject uiTv = new UiObject(new UiSelector().resourceId("com.startnews.plugin:id/channel_name").text("小视频"));
                    if (uiTv.exists()) uiTv.click();
                    Thread.sleep(2000);
                    UiObject uiTitle = new UiObject(new UiSelector().resourceId("com.startnews.plugin:id/small_video_title_tv"));
                    if (uiTitle.exists()) uiTitle.click();
                    Thread.sleep(2000);

                } else if (uiTvBack.exists()) {
                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 2) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 100) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Thread.sleep(15000);//播放 时长
                    }

                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiHomeTab = new UiObject(new UiSelector().resourceId("com.planet.light2345:id/bottom_tab_layout"));
                    if (uiHomeTab.exists()) {
                        UiObject child = uiHomeTab.getChild(new UiSelector().className("android.view.ViewGroup").instance(0));
                        if (child.exists()) {
                            child.click();
                            continue;
                        }
                    }
                    UiObject uiDialogClose = new UiObject(new UiSelector().resourceId("com.planet.light2345:id/iv_close"));
                    UiObject uiDialogClose02 = new UiObject(new UiSelector().resourceId("com.planet.light2345:id/iv_delete"));

                    if (uiDialogClose.exists()) {//弹框
                        uiDialogClose.click();
                    } else if (uiDialogClose02.exists()) {//弹框
                        uiDialogClose02.click();
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