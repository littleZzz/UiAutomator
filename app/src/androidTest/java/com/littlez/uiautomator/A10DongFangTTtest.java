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
 * <p>东方头条  测试用例
 * Date 2019/12/3
 */
public class A10DongFangTTtest extends TestCase {

    /*app 名字*/
    private String appName = "东方头条";

    private int errorCount = 0;//记录异常强制启动次数  超过10次就关闭应用
    private int newsCount = 0;//记录一下看新闻的次数  每隔几次点击一下

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        // 获取上下文
//        Context context = instrumentation.getContext();

//        LogUtil.e("我开始运行了");
        int count = 0;

        try {

            baseMethod(uiDevice, 0);//启动时  先关闭其他的

            while (true) {

//                LogUtil.e("我运行了" + (count++));

                //主页
                UiObject uiMain = new UiObject(new UiSelector()
                        .resourceId("com.songheng.eastnews:id/adz"));
                //收藏
                UiObject uiCollect = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/xd"));

//                其特点就是  和今日头条基本是一模一样的风格
//                分别如下 com.songheng.eastnews:id/aep 是视屏  com.songheng.eastnews:id/a5i 新闻

                if (uiCollect.exists()) {//查看新闻界面

                    boolean isRun = true;
                    //查看新闻
                    while (isRun && uiCollect.exists()) {

                        UiObject UiLike = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/axc"));

                        if (UiLike.exists()) {//视频
                            Random r = new Random();
                            int number = r.nextInt(30) + 1;
                            Thread.sleep((25 + number) * 1000);
                            uiDevice.pressBack();
                            isRun = false;
                            break;
                        } else {//新闻

                            UiObject uiHotNews = new UiObject(
                                    new UiSelector().resourceId("com.songheng.eastnews:id/aio"));

                            if (uiHotNews.exists()) {
                                isRun = false;
                                uiDevice.pressBack();
                            } else {
                                uiDevice.swipe(400, 1200, 534, 802, 10);
                            }

                        }
                    }

                } else if (uiMain.exists()) {//是主页

                    UiObject uiPhoto = new UiObject(new UiSelector()
                            .resourceId("com.songheng.eastnews:id/a5u"));

                    if (uiPhoto.exists()) {//选中新闻栏

                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        if (number <= 2) {//上滑
                            uiDevice.swipe(534, 802, 400, 1200, 10);
                        } else if (number <= 95) {//下滑
                            uiDevice.swipe(400, 1200, 534, 402, 10);
                        } else if (number <= 98) {
                            UiObject uiMission = new UiObject(new UiSelector()
                                    .resourceId("com.songheng.eastnews:id/aal").text("任务"));
                            uiMission.click();//跳转到查看任务
                            Thread.sleep(500);
                            continue;
                        } else {
                            UiObject uiMe = new UiObject(new UiSelector()
                                    .resourceId("com.songheng.eastnews:id/aak").text("我的"));
                            uiMe.click();//我的
                            Thread.sleep(1000);
                            continue;
                        }

                        //点击item  其实这里有三个类型还不止  新闻  视屏  短视频   等等
                        UiObject uiNewItem = new UiObject(new UiSelector()
                                .resourceId("com.songheng.eastnews:id/a5i").instance(0));
                        UiObject uiTvItem = new UiObject(new UiSelector()
                                .resourceId("com.songheng.eastnews:id/aep").instance(0));
                        if (uiNewItem.exists() && uiTvItem.exists()) {//两个都存在
                            Random rr = new Random();
                            int num = rr.nextInt(6) + 1;
                            if (num <= 2) uiTvItem.click();
                            else uiNewItem.clickTopLeft();
                        } else {
                            if (uiNewItem.exists()) uiNewItem.clickTopLeft();
                            if (uiTvItem.exists()) uiTvItem.clickTopLeft();
                        }
                        Thread.sleep(1000);//要停一下  给一些加载时间
                    } else {//其他
                        UiObject uiHome = new UiObject(new UiSelector()
                                .resourceId("com.songheng.eastnews:id/a5v"));
                        uiHome.click();
                    }

                } else {//处理异常情况
                    //靠点赞按钮判断是不是在播放视频那个整个界面
                    UiObject uiDialog = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/ea"));
                    UiObject uiDialog02 = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/avl"));
                    UiObject uiDialog03 = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/uc"));
                    UiObject uiNoFun = new UiObject(new UiSelector().resourceId("com.songheng.eastnews:id/a12"));

                    if (uiDialog.exists()) {//
                        uiDialog.click();
                    } else if (uiDialog02.exists()) {//
                        uiDialog02.click();
                    } else if (uiDialog03.exists()) {//
                        uiDialog03.click();
                    } else if (uiNoFun.exists()) {//
                        uiDevice.pressBack();
                    } else {//最终的强制搞一波

                        baseMethod(uiDevice, 1);
                    }
                }

                Thread.sleep(500);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 基本的运行方法封装
     */
    public void baseMethod(UiDevice uiDevice, int flag) {
        try {
            switch (flag) {
                case 0://CLEAR_APP
                    uiDevice.pressRecentApps();
                    Thread.sleep(500);
                    UiObject clearAll = new UiObject(new UiSelector().resourceId("com.android.systemui:id/clearAnimView"));
                    if (clearAll.exists()) {
                        clearAll.click();
                        Thread.sleep(500);
                    }
                    break;
                case 1://Error_Base
                    if (errorCount > 6) {//这个强制方法走了10次  出现什么异常问题了 直接关闭应用  重新启动
                        uiDevice.pressHome();
                        Thread.sleep(500);
                        uiDevice.pressRecentApps();
                        Thread.sleep(500);
                        UiObject appClearAll =
                                new UiObject(new UiSelector().resourceId("com.android.systemui:id/clearAnimView"));
                        if (appClearAll.exists()) {
                            appClearAll.click();
                            errorCount = 0;//重置失败次数
                            Thread.sleep(500);
                        }
                    }
                    uiDevice.pressHome();
                    Thread.sleep(500);
                    uiDevice.pressRecentApps();
                    Thread.sleep(500);
                    UiObject appLaunch = new UiObject(new UiSelector().descriptionContains(appName)
                            .className("android.widget.FrameLayout"));
                    if (appLaunch.exists()) {//没有彻底挂掉
                        appLaunch.click();
                        Thread.sleep(2000);
                    } else {//彻底挂掉了  重启
                        uiDevice.pressHome();
                        Thread.sleep(500);
                        //启动应用
                        UiObject uiVideo = new UiObject(new UiSelector().text(appName));
                        if (uiVideo.exists()) {
                            uiVideo.click();
                            Thread.sleep(2000);
                        }
                    }
                    errorCount++;//增加异常启动次数
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}