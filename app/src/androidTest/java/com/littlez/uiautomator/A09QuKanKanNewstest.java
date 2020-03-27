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
 * <p>趣看看  测试用例
 * Date 2019/12/3
 */
public class A09QuKanKanNewstest extends TestCase {


    /*app 名字*/
    private String appName = "趣看看";


    private int errorCount = 0;//记录异常强制启动次数  超过10次就关闭应用
    private int newsCount = 0;//记录一下看新闻的次数  每隔几次点击一下

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);


        baseMethod(uiDevice, 0);//启动时  先关闭其他的

        while (true) {
            try {
//                LogUtil.e("我运行了" + (count++));

                //主页
                UiObject uiMain = new UiObject(new UiSelector()
                        .resourceId("com.popnews2345:id/ll_tab_group"));
                //红包
                UiObject uiRedPack = new UiObject(new UiSelector().resourceId("com.startnews.plugin:id/iv_red_pack"));


                if (uiRedPack.exists()) {//查看新闻界面

                    boolean isRun = true;
                    newsCount++;

                    if (newsCount % 4 == 0) {//每隔三次点击一下红包
                        uiRedPack.click();
                        continue;
                    }

                    //查看新闻
                    while (isRun) {

                        //新闻
                        UiObject uiLike = new UiObject(
                                new UiSelector().resourceId("com.startnews.plugin:id/rel_like"));

                        if (uiLike.exists()) {
                            isRun = false;
                            uiDevice.pressBack();
                        } else {
                            uiDevice.swipe(400, 1200, 534, 802, 10);
                        }

                    }

                } else if (uiMain.exists()) {//是主页

                    UiObject uiHome = new UiObject(new UiSelector()
                            .resourceId("com.popnews2345:id/tv_title").text("头条"));
                    UiObject uiMission = new UiObject(new UiSelector()
                            .resourceId("com.popnews2345:id/tv_title").text("任务"));
                    UiObject uiMe = new UiObject(new UiSelector()
                            .resourceId("com.popnews2345:id/tv_title").text("我的"));

                    if (uiHome.isSelected()) {//选中的头条
                        //item  通过图片id  确认点击哪一个
                        UiObject uiItem = new UiObject(new UiSelector()
                                .resourceId("com.startnews.plugin:id/img_item1").instance(0));

                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        if (number <= 2) {//上滑
                            uiDevice.swipe(534, 802, 400, 1200, 10);
                        } else if (number <= 95) {//下滑
                            uiDevice.swipe(400, 1200, 534, 602, 10);
                        } else if (number <= 98) {
                            uiMission.click();//跳转到查看任务
                            Thread.sleep(500);
                            continue;
                        } else {
                            uiMe.click();//我的
                            Thread.sleep(1000);
                            continue;
                        }
                        uiItem.click();
                        Thread.sleep(1000);//要听一下  给一些加载时间
                    } else {//其他
                        uiHome.click();
                    }

                } else {//处理异常情况
                    //靠点赞按钮判断是不是在播放视频那个整个界面
                    UiObject uiDialog = new UiObject(new UiSelector().resourceId("com.popnews2345:id/iv_close"));
                    UiObject uiDialog02 = new UiObject(
                            new UiSelector().resourceId("com.startnews.plugin:id/news2345_iv_close"));

                    if (uiDialog.exists()) {//
                        uiDialog.click();
                    } else if (uiDialog02.exists()) {//
                        uiDialog02.click();
                    } else {//最终的强制搞一波

                        baseMethod(uiDevice, 1);
                    }
                }

                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
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