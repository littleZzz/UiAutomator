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
public class KuaiKanDianTVtest extends TestCase {


    /*app 名字*/
    private String appName = "快看点";

    public enum TYPE {
        CLEAR_APP, Error_Base,
    }

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

        try {


            baseMethod(uiDevice, TYPE.CLEAR_APP.ordinal());//启动时  先关闭其他的

            while (true) {

//                LogUtil.e("我运行了" + (count++));
                Thread.sleep(1000);

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

                    if (uiHome.exists() && uiHome.isSelected()) {//选中的首页
                        uiTV.click();
                    } else if (uiTV.exists() && uiTV.isSelected()) {//选中的小视频

                        //心
                        UiObject uiHeart = new UiObject(new UiSelector()
                                .resourceId("com.yuncheapp.android.pearl:id/like_icon"));
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        /*随机数 进行判断 点击心或者滑动到下一个视频*/
                        if (number <= 10) {//上滑
                            uiDevice.swipe(534, 802, 400, 1200, 2);
                        } else if (number <= 90) {//下滑
                            uiDevice.swipe(400, 1200, 534, 802, 2);
                            Thread.sleep(8000);//播放 时长
                        } else if (number <= 95) {//点击任务
                            uiMission.click();
                        } else {//3点击心
                            if (uiHeart.exists()) uiHeart.click();
                        }

                    } else if (uiMission.exists() && uiMission.isSelected()) {//选中的任务
                        uiTV.click();
                    } else {//其他
                        uiTV.click();
                    }


                } else {//处理异常情况
                    UiObject uiDialogClose = new UiObject(new UiSelector().resourceId("com.jifen.dandan:id/iv_close"));

                    UiObject uiRootT = new UiObject(new UiSelector().resourceId("com.kingroot.kinguser:id/title").text("UiAutomator"));
                    UiObject uiRootAllow = new UiObject(new UiSelector().resourceId("com.kingroot.kinguser:id/button_right"));

                    if (uiDialogClose.exists()) {//
                        uiDialogClose.click();
                    } else if (uiRootT.exists() && uiRootAllow.exists()) {//root 权限获取
                        uiRootAllow.click();
                    } else {//最终的强制搞一波

                        baseMethod(uiDevice, TYPE.Error_Base.ordinal());
                    }
                }


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
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}