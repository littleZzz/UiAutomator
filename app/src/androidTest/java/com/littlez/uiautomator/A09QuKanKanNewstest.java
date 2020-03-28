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
 * <p>趣看看  测试用例
 * Date 2019/12/3
 */
public class A09QuKanKanNewstest extends TestCase {

    /*app 名字*/
    private String appName = "趣看看";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置次数
        try {
            while (true) {
                //主页
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.popnews2345:id/ll_tab_group"));
                if (uiHome.exists()) {//是主页  滑动选择条目
                    UiObject uiHomeTitle = new UiObject(new UiSelector().resourceId("com.popnews2345:id/main_title_rl"));
                    if (uiHomeTitle.exists()) {//选中的是首页
                        //主页滑动选择条目
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        if (number <= 2) {//上滑
                            A00UtilTest.swipUp(uiDevice, 15);
                        } else if (number <= 95) {//下滑
                            A00UtilTest.swipDown(uiDevice, 15);
                        } //做一些其他额外的附加任务
                        //首页领取  做一个时段任务奖励的领取
                        UiObject uiGet = new UiObject(new UiSelector()
                                .resourceId("com.popnews2345:id/main_reward_time_tv").text("领取"));
                        if (uiGet.exists()) {
                            uiGet.click();
                            continue;
                        }
                        Thread.sleep(500);//没有时段领取 选择一条进行跳转
                        UiObject uiTvTitle = new UiObject(new UiSelector().resourceId("com.startnews.plugin:id/tv_title"));
                        if (uiTvTitle.exists()) uiTvTitle.click();//有视频 先看视频
                        else {//就看新闻
                            UiObject uiAuther = new UiObject(
                                    new UiSelector().resourceId("com.startnews.plugin:id/tv_news_from").instance(0).index(0));
                            uiAuther.click();
                        }
                        Thread.sleep(1500);//要听一下  给一些加载时间
                    }
                } else {//去检测是不是我想要的界面  是就进行处理
                    UiObject uiRecycler =
                            new UiObject(new UiSelector().resourceId("com.startnews.plugin:id/recycler_detail"));//收藏
                    UiObject uiPlayer = new UiObject(
                            new UiSelector().resourceId("com.startnews.plugin:id/news2345_media_player_root"));//视频播放器
                    if (uiPlayer.exists()) {//是视频
                        Random r = new Random();
                        int number = r.nextInt(90) + 1;
                        Thread.sleep((45 + number) * 1000);
                        uiDevice.pressBack();
                    } else if (uiRecycler.exists()) {//是新闻

                        boolean isRun = true;
                        while (isRun) {
                            UiObject uilike = new UiObject(
                                    new UiSelector().text("不喜欢").resourceId("com.startnews.plugin:id/tv_unlike"));//喜欢
                            if (uilike.exists()) {
                                isRun = false;
                                uiDevice.pressBack();
                            } else {
                                uiDevice.swipe(400, 1200, 534, 802, 10);
                                Thread.sleep(2000);
                            }
                        }
                    } else {
                        //处理异常情况 首页领取奖励后的dialog
                        UiObject uiClose = new UiObject(new UiSelector().resourceId("com.popnews2345:id/incentive_video_close_img"));
                        UiObject uiClose02 = new UiObject(new UiSelector().resourceId("com.popnews2345:id/iv_delete"));
                        if (uiClose.exists()) {
                            uiClose.click();
                        } else if (uiClose02.exists()) {
                            uiClose02.click();
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