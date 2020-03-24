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
                            int number = r.nextInt(60) + 1;
                            Thread.sleep((45 + number) * 1000);
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
                                Thread.sleep(500);
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
                            A00UtilTest.swipUp(uiDevice, 10);
                        } else if (number <= 95) {//下滑
                            A00UtilTest.swipDown(uiDevice, 10);
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