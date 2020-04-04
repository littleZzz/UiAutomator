package com.littlez.uiautomator;

import android.app.Instrumentation;
import android.graphics.Rect;
import android.util.Log;

import com.littlez.uiautomator.util.LogUtil;

import junit.framework.TestCase;

import java.lang.annotation.ElementType;
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
public class A05KuaiKanDianNewstest extends TestCase {

    /*app 名字*/
    private String appName = "快看点";

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
                    UiObject uiMission = new UiObject(new UiSelector()
                            .resourceId("com.yuncheapp.android.pearl:id/tab_tv").text("任务"));

                    if (uiHome.exists() && uiHome.isSelected()) {//选中的首页
                        //item
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        if (number <= 2) {//上滑
                            A00UtilTest.swipUp(uiDevice, 15);
                        } else if (number <= 97) {//下滑
                            A00UtilTest.swipDown(uiDevice, 15);
                        } else {
                            uiMission.click();//跳转到查看任务
                            continue;
                        }
                        UiObject uiItem = new UiObject(new UiSelector()
                                .resourceId("com.yuncheapp.android.pearl:id/title").instance(0));
                        UiObject uiTV = new UiObject(new UiSelector()
                                .resourceId("com.yuncheapp.android.pearl:id/cover"));
                        if (uiTV.exists()) uiTV.click();//需要优先点击视屏观看
                        else uiItem.click();
                        Thread.sleep(1000);//要听一下  给一些加载时间
                    } else {//其他
                        uiHome.click();
                    }
                } else {//去检测是不是我想要的界面  是就进行处理

                    UiObject uiCollect = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/collect"));
                    UiObject uiWeixin02 =
                            new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/wechat_wrapper"));//视频
                    UiObject uiNet = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/description"));//网络不畅

                    if (uiNet.exists()) {//处理网络异常
                        uiNet.click();
                        Thread.sleep(3000);
                    } else if (uiCollect.exists() && uiWeixin02.exists()) {//是视频
                        boolean isRun = true;
                        while (isRun) {//循环观看视频
                            UiObject uiRePlay = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/replay"));
                            if (uiRePlay.exists()) {
                                isRun = false;
                                uiDevice.pressBack();
                            }
                            Thread.sleep(2000);
                        }
                    } else if (uiCollect.exists()) {//新闻
                        boolean isRun = true;
                        while (isRun) {
                            UiObject uiRecy = new UiObject(
                                    new UiSelector().resourceId("com.yuncheapp.android.pearl:id/recycler_view"));
                            if (uiRecy.exists()) {
                                isRun = false;
                                uiDevice.pressBack();
                            } else {
                                uiDevice.swipe(400, 1200, 534, 802, 10);
                                Thread.sleep(3000);
                            }
                        }

                    } else {//处理异常情况
                        //靠点赞按钮判断是不是在播放视频那个整个界面
                        UiObject uiTV = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/like_icon"));
                        UiObject close = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/close_img"));
                        UiObject close02 = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/coin_get"));
                        UiObject close03 = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/iv_close"));
                        UiObject close04 = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/dialog_close"));
                        UiObject close05 =
                                new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/dialog_negative_button"));
                        UiObject close06 = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/btn_positive"));

                        if (uiTV.exists()) {//
                            uiDevice.pressBack();
                        } else if (close.exists()) {
                            close.click();
                        } else if (close02.exists()) {
                            close02.click();
                        } else if (close03.exists()) {
                            close03.click();
                        } else if (close04.exists()) {//砸金蛋
                            close04.click();
                        } else if (close05.exists()) {//版本跟新 忽略版本
                            close05.click();
                        } else if (close06.exists()) {//版本跟新 忽略版本
                            close06.click();
                        } else {//最终的强制搞一波
                            A00UtilTest.baseMethod(uiDevice, 1, appName);
                        }
                    }
                }
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}