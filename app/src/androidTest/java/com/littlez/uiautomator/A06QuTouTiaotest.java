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
            A00UtilTest.errorCount = 0;//重置
            while (true) {
                //主页
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/m6"));
                if (uiHome.exists()) {//是主页  滑动选择条目
                    UiObject uiHomeChild =
                            uiHome.getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
                    if (uiHomeChild.isSelected()) {//选中的是首页
                        //主页滑动选择条目
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        if (number <= 2) {//上滑
                            A00UtilTest.swipUp(uiDevice, 10);
                        } else if (number <= 100) {//下滑
                            A00UtilTest.swipDown(uiDevice, 10);
                        }
                        UiObject uiGet =
                                new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/bsx").text("领取"));//首页领取
                        if (uiGet.exists()) {
                            uiGet.click();
                            continue;
                        }
                        Thread.sleep(500);
                        UiObject uiAuther = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/aly"));
                        uiAuther.click();//跳转到查看任务
                        Thread.sleep(600);//要听一下  给一些加载时间
                    }
                } else {//去检测是不是我想要的界面  是就进行处理
                    UiObject uicllect = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/bmx"));//收藏
                    UiObject uiTvOut = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/tp"));//视频最外层
                    if (uicllect.exists() && uiTvOut.exists()) {//是视频
                        boolean isRun = true;
                        while (isRun) {//循环观看视频
                            UiObject uiAward = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/aw_"));//阅读奖励
                            if (uiAward.exists()) {
                                uiAward.click();
                                break;//要跳过这次循环
                            }
                            UiObject uiRePlay = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/btv"));
                            if (uiRePlay.exists()) {
                                isRun = false;
                                uiDevice.pressBack();
                            }
                            Thread.sleep(2000);
                        }
                    } else if (uicllect.exists()) {//是新闻

                        UiObject uiAward = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/aw_"));//阅读奖励
                        if (uiAward.exists()) {
                            uiAward.click();
                            continue;//要跳过这次循环
                        }
                        boolean isRun = true;
                        int count = 0;//记录滑动次数  超过一定次数就直接返回
                        while (isRun) {

                            uiDevice.swipe(400, 1200, 534, 602, 10);
                            count++;
                            if (count >= 35) {
                                isRun = false;
                                uiDevice.pressBack();
                            }
                            Thread.sleep(1500);
                        }
                    } else {
                        //处理异常情况
                        UiObject uiClose = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/a5n"));
                        //阅读奖励点击后的dialog
                        UiObject uiClose02 = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/v8"));
                        UiObject uiClose03 = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/yc"));
                        UiObject uiClose04 = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/a92"));
                        UiObject uiClose05 = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/aw_"));//阅读奖励

                        if (uiClose.exists()) {
                            uiClose.click();
                        } else if (uiClose02.exists()) {
                            uiClose02.click();
                        } else if (uiClose03.exists()) {
                            uiClose03.click();
                        } else if (uiClose04.exists()) {
                            uiClose04.click();
                        } else if (uiClose05.exists()) {
                            uiClose05.click();
                        } else {//最终的强制搞一波
                            A00UtilTest.baseMethod(uiDevice, 1, appName);
                        }
                    }
                }
                Thread.sleep(500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}