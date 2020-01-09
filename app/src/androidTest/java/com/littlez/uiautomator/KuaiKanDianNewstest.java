package com.littlez.uiautomator;

import android.app.Instrumentation;
import android.graphics.Rect;
import android.util.Log;

import com.littlez.uiautomator.util.LogUtil;

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
public class KuaiKanDianNewstest extends TestCase {


    /*app 名字*/
    private String appName = "快看点";


    private int errorCount = 0;//记录异常强制启动次数  超过10次就关闭应用


    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        // 获取上下文
//        Context context = instrumentation.getContext();

//        LogUtil.e("我开始运行了");
        int count = 0;


        baseMethod(uiDevice, 0);//启动时  先关闭其他的

        while (true) {

            try {
//                LogUtil.e("我运行了" + (count++));

                //主页
                UiObject uiMain = new UiObject(new UiSelector()
                        .resourceId("com.yuncheapp.android.pearl:id/home_page_tab_bar"));
                //收藏 分享
                UiObject uiCollect = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/collect"));
                UiObject uiShare = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/share"));


                if (uiCollect.exists() && uiShare.exists()) {//查看新闻界面

                    boolean isRun = true;
                    //查看新闻
                    while (isRun && uiCollect.exists() && uiShare.exists()) {
                        UiObject uiWeixin02 = new UiObject(new UiSelector()
                                .resourceId("com.yuncheapp.android.pearl:id/wechat_wrapper"));//视频


                        if (uiWeixin02.exists()) {//视频
                            Random r = new Random();
                            int number = r.nextInt(30) + 1;
                            Thread.sleep((25 + number) * 1000);
                            uiDevice.pressBack();
                            isRun = false;
                            break;
                        } else {//新闻

                            UiObject uiRecy = new UiObject(
                                    new UiSelector().resourceId("com.yuncheapp.android.pearl:id/recycler_view"));

                            if (uiRecy.exists()) {
                                isRun = false;
                                uiDevice.pressBack();
                            } else {
                                uiDevice.swipe(400, 1200, 534, 802, 10);
                            }

                        }
                    }

                } else if (uiMain.exists()) {//是主页

                    UiObject uiHome = new UiObject(new UiSelector()
                            .resourceId("com.yuncheapp.android.pearl:id/tab_tv").text("首页"));
                    UiObject uiTV = new UiObject(new UiSelector()
                            .resourceId("com.yuncheapp.android.pearl:id/tab_tv").text("小视频"));
                    UiObject uiMission = new UiObject(new UiSelector()
                            .resourceId("com.yuncheapp.android.pearl:id/tab_tv").text("任务"));

                    if (uiHome.exists() && uiHome.isSelected()) {//选中的首页
                        //item
                        UiObject uiItem = new UiObject(new UiSelector()
                                .resourceId("com.yuncheapp.android.pearl:id/title").instance(0));

                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        if (number <= 2) {//上滑
                            uiDevice.swipe(534, 802, 400, 1200, 10);
                        } else if (number <= 95) {//下滑
                            uiDevice.swipe(400, 1200, 534, 802, 10);
                        } else {
                            uiMission.click();//跳转到查看任务
                            continue;
                        }
                        uiItem.click();
                        Thread.sleep(1000);//要听一下  给一些加载时间
                    } else {//其他
                        uiHome.click();
                    }

                } else {//处理异常情况
                    //靠点赞按钮判断是不是在播放视频那个整个界面
                    UiObject uiTV = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/like_icon"));

                    if (uiTV.exists()) {//
                        uiDevice.pressBack();
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
                        Thread.sleep(1000);
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