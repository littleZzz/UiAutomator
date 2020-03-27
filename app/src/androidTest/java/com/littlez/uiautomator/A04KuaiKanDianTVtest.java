package com.littlez.uiautomator;

import android.app.Instrumentation;

import junit.framework.TestCase;

import java.util.Random;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

/**
 * created by xiaozhi
 * <p>快看点  测试用例
 * Date 2019/12/3
 */
public class A04KuaiKanDianTVtest extends TestCase {


    /*app 名字*/
    private String appName = "快看点";
    private int swipCount = 0;//记录一下滑动的次数每隔10次滑动  点击一下timer


    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置

        while (true) {

            try {

                //主页
                UiObject uiMain = new UiObject(new UiSelector()
                        .resourceId("com.yuncheapp.android.pearl:id/home_page_tab_bar"));

                if (uiMain.exists()) {//是主页

                    UiObject uiHome = new UiObject(new UiSelector()
                            .resourceId("com.yuncheapp.android.pearl:id/tab_tv").text("首页"));
                    UiObject uiTV = new UiObject(new UiSelector()
                            .resourceId("com.yuncheapp.android.pearl:id/tab_tv").text("小视频"));
                    UiObject uiMission = new UiObject(new UiSelector()
                            .resourceId("com.yuncheapp.android.pearl:id/tab_tv").text("任务"));

                    if (uiTV.exists() && uiTV.isSelected()) {//选中的小视频

                        //心
                        UiObject uiHeart = new UiObject(new UiSelector()
                                .resourceId("com.yuncheapp.android.pearl:id/like_icon"));
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        /*随机数 进行判断 点击心或者滑动到下一个视频*/
                        if (number <= 10) {//上滑
                            A00UtilTest.swipUp(uiDevice);
                        } else if (number <= 90) {//下滑
                            A00UtilTest.swipDown(uiDevice);
                            Thread.sleep(15000);//播放 时长
                            swipCount++;
                            if (swipCount % 10 == 0) {
                                UiObject uiTimer = new UiObject(
                                        new UiSelector().resourceId("com.yuncheapp.android.pearl:id/timer_anchor"));
                                uiTimer.clickTopLeft();
                            }
                        } else if (number <= 95) {//点击任务
                            uiMission.click();
                        } else {//3点击心
                            if (uiHeart.exists()) uiHeart.click();
                        }

                    } else {//其他
                        uiTV.click();
                    }

                } else {//处理异常情况
                    UiObject uiDialogClose = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/close_img"));
                    UiObject uiWebView = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/webview"));
                    UiObject close = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/close_img"));

                    //TODO 还有一个砸蛋那个关闭没有弄
                    if (uiWebView.exists()) {
                        uiDevice.pressBack();
                    } else if (uiDialogClose.exists()) {//
                        uiDialogClose.click();
                    }else if (close.exists()) {//
                        close.click();
                    } else {//最终的强制搞一波

                        A00UtilTest.baseMethod(uiDevice, 1, appName);
                    }
                }
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}