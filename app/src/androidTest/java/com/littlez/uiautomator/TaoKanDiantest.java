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
 * <p>淘看点  测试用例
 * Date 2019/12/3
 */
public class TaoKanDiantest extends TestCase {


    /*app 名字*/
    private String appName = "淘看点";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        // 获取上下文
//        Context context = instrumentation.getContext();

//        LogUtil.e("我开始运行了");
        int count = 0;
        int errorCount = 0;//记录异常强制启动次数  超过10次就关闭应用

        try {

            //腾讯微视完全要自己特别定制方案 因为需要每次一达到目标就进行点击
            while (true) {

//                LogUtil.e("我运行了" + (count++));
                Thread.sleep(1000);

                //首页
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.ly.taokandian:id/share_iv"));
                //心
                UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.ly.taokandian:id/like_iv"));


                if (uiHome.exists()) {//是首页

                    Random r = new Random();
                    int number = r.nextInt(100) + 1;

                    UiObject uiGet = new UiObject(new UiSelector().resourceId("com.ly.taokandian:id/tv_time_reward"));
                    if (uiGet.exists()) {//有一个每个一定时间  就可以点击领取奖励的任务
                        uiGet.click();
                        continue;
                    }
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 10) {//上滑
                        uiDevice.swipe(534, 802, 400, 1200, 2);
                    } else if (number <= 95) {//下滑
                        uiDevice.swipe(400, 1200, 534, 802, 2);
                        Thread.sleep(7000);//播放 时长
                    } else {//3点击心
                        if (uiHeart.exists()) uiHeart.click();
                    }

                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下

                    UiObject uiRootT = new UiObject(new UiSelector().resourceId("com.kingroot.kinguser:id/title").text("UiAutomator"));
                    UiObject uiRootAllow = new UiObject(new UiSelector().resourceId("com.kingroot.kinguser:id/button_right"));

                    UiObject uiDialogClose = new UiObject(new UiSelector().resourceId("com.ly.taokandian:id/btn_close"));
                    UiObject uiDialogClose02 = new UiObject(new UiSelector().resourceId("com.ly.taokandian:id/btn_sure"));
                    UiObject uiWebView = new UiObject(new UiSelector().className("android.webkit.WebView").packageName("com.ly.taokandian"));

                    if (uiRootT.exists() && uiRootAllow.exists()) {//root 权限获取
                        uiRootAllow.click();
                    } else if (uiDialogClose.exists()) {//弹框（邀请好友）
                        uiDialogClose.click();
                    } else if (uiWebView.exists()) {//webview广告
                        uiDevice.pressBack();
                    } else if (uiDialogClose02.exists()) {
                        uiDialogClose02.click();
                    } else {//最终的强制搞一波

                        if (errorCount > 6) {//这个强制方法走了10次  出现什么异常问题了 直接关闭应用  重新启动
                            uiDevice.pressHome();
                            Thread.sleep(500);
                            uiDevice.pressRecentApps();
                            Thread.sleep(500);
                            UiObject appClearAll =
                                    new UiObject(new UiSelector().resourceId("com.android.systemui:id/clearButton"));
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
                        UiObject appLaunch = new UiObject(new UiSelector().text(appName));
                        if (appLaunch.exists()) {//没有彻底挂掉
                            appLaunch.click();
                            Thread.sleep(500);
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

                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}