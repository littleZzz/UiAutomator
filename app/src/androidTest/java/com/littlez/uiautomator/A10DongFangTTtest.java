package com.littlez.uiautomator;

import android.app.Instrumentation;

import junit.framework.TestCase;

import java.util.Random;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.littlez.uiautomator.util.LogUtil;

/**
 * created by xiaozhi
 * <p>东方头条  测试用例
 * Date 2019/12/3
 */
public class A10DongFangTTtest extends TestCase {

    /*app 名字*/
    private String appName = "东方头条";
    private boolean isFirstRun = true;//是否第一次启动

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

//        A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
//        A00UtilTest.errorCount = 0;//重置次数
        try {
            while (true) {
                //主页
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/ady"));
                if (uiHome.exists()) {//是主页  滑动选择条目
                    UiObject uiHomeTitle = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/afj"));
                    if (uiHomeTitle.exists()) {//选中的是首页
//                        if (isFirstRun) {//第一次启动要刷新一下数据
//                            UiObject uinews = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/a5t"));
//                            if (uinews.exists()) {
//                                uinews.click();
//                                isFirstRun = false;
//                                Thread.sleep(2000);
//                                continue;
//                            }
//                        }
                        //主页滑动选择条目
//                        Random r = new Random();
//                        int number = r.nextInt(100) + 1;
//                        if (number <= 2) {//上滑
//                            A00UtilTest.swipUp(uiDevice, 12);
//                        } else if (number <= 95) {//下滑
//                            A00UtilTest.swipDown(uiDevice, 12);
//                        } //做一些其他额外的附加任务
                        //首页领取  做一个时段任务奖励的领取
                        UiObject uiGet = new UiObject(new UiSelector()
                                .resourceId("com.songheng.eastnews:id/aj9").text("领取"));
                        if (uiGet.exists()) {
                            uiGet.click();
                            continue;
                        }
                        Thread.sleep(500);//没有时段领取 选择一条进行跳转
                        UiObject uiTvTitle = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/atv"));
                        if (uiTvTitle.exists()) uiTvTitle.click();//有视频 先看视频
                        else {//就看新闻
                            UiObject uiNews =
                                    new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/au7").instance(0));
                            uiNews.click();
                        }
                        Thread.sleep(1500);//要听一下  给一些加载时间
                    }
                } else {//去检测是不是我想要的界面  是就进行处理
                    UiObject uiListView = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/ax3"));//列表
                    UiObject uiNewList = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/a37"));//新闻列表
                    LogUtil.e("1");
                    if (uiNewList.exists()) {//是新闻
                        LogUtil.e("2");
                        boolean isRun = true;
                        while (isRun) {
                            LogUtil.e("3");
                            UiObject uiHotNews = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/aim"));//热门新闻
                            UiObject uiMore = new UiObject(
                                    new UiSelector().resourceIdMatches("com.songheng.eastnews:id/a4y"));//这个查找慢
                            LogUtil.e("4");
                            if (uiMore.exists()) {//查看更多
                                LogUtil.e("5");
                                uiMore.click();
                            } else if (uiHotNews.exists()) {
                                LogUtil.e("6");
                                isRun = false;
                                uiDevice.pressBack();
                            } else {
                                LogUtil.e("7");
                                uiDevice.swipe(400, 1200, 534, 802, 10);
                                Thread.sleep(1000);
                            }
                        }
                    } else if (uiListView.exists()) {//是视频
                        Random r = new Random();
                        int number = r.nextInt(90) + 1;
                        Thread.sleep((45 + number) * 1000);
                        uiDevice.pressBack();
                    } else {
                        //处理异常情况 首页领取奖励后的dialog
                        UiObject uiClose = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/au8"));//时段奖励领取
                        UiObject uiClose02 = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/uf"));//dialog
                        UiObject uiClose03 = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/amn"));//dialog

                        if (uiClose.exists()) {
                            uiClose.click();
                        } else if (uiClose02.exists()) {
                            uiClose02.click();
                        } else if (uiClose03.exists()) {
                            uiClose03.click();
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