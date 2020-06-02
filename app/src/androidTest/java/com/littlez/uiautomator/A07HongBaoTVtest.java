package com.littlez.uiautomator;

import android.app.Instrumentation;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import junit.framework.TestCase;

/**
 * created by xiaozhi
 * <p>红包短视频 测试用例
 * Date 2019/12/3
 */
public class A07HongBaoTVtest extends TestCase {

    /*app 名字*/
    private String appName = "红包短视频";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
            A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
            A00UtilTest.errorCount = 0;//重置

            UiObject uiHome = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/fixed_bottombar_container"));//底部tab栏 最外层 framentlayout id
            UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/item_short_video_heart"));//心
            UiObject uiToTaskPage = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/menu_more_task"));//tab 外层relative id
            UiObject uiToMePage = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/menu_more_me"));//tab 外层relative id
            UiObject uiIsMePage = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/money_back_tv").text("我的"));//标题栏文本id
            UiObject uiIsTaskPage = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/money_back_tv").text("任务中心"));//标题栏文本id
            UiObject uiIsServicePage = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/money_back_tv").text("客服&反馈"));//标题栏文本id

            UiObject uiBubble = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/gold_bubble").className("android.widget.RelativeLayout"));//奖励气泡
            UiObject uiBubbleDiaClose = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/money_dialog_getcash_new2_close"));//奖励气泡dia 关闭按钮

            while (appRun) {

                if (uiHome.exists()) {//是首页 播放视频界面
                    int number = A00UtilTest.getRandom(100);
                    if (number <= 3) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 94) {//下一条
                        A00UtilTest.swipDown(uiDevice);
//                        Thread.sleep((A00UtilTest.getRandom(6)) * 1000);//播放 时长 检测控件时间长 用检测时间代替播放时长了
                        if (uiBubble.exists()) {//有奖励气泡
                            uiBubble.click();
                        }
                    } else if (number <= 96) {//任务
                        if (uiToTaskPage.exists()) uiToTaskPage.click();
                    } else if (number <= 98) {//我的
                        if (uiToMePage.exists()) uiToMePage.click();
                    } else {//3点击心
                        if (uiHeart.exists()) uiHeart.click();
                    }
                } else if (uiIsMePage.exists() || uiIsTaskPage.exists() || uiIsServicePage.exists()) {//我的  或者任务 客服界面
                    Thread.sleep(5000);
                    uiDevice.pressBack();
                } else {//处理异常情况
                    UiObject uiDialogClose = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/msg_dialog_left"));//好评对话框
                    UiObject uiDiaClose02 = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/money_mine_popups_bt_close"));//现金入账dia 关闭按钮
                    UiObject uiGuideClose = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/share_video_task_guide_finish"));//结束引导
                    UiObject uiGuideClose02 = new UiObject(new UiSelector().resourceId("com.jxbz.hbdsp:id/dialog_guide_bt_left"));//结束引导狠心放弃

                    if (uiDialogClose.exists()) {//
                        uiDialogClose.click();
                    } else if (uiDiaClose02.exists()) {//现金入账dia 关闭按钮
                        uiDiaClose02.click();
                    } else if (uiGuideClose.exists()) {//结束引导
                        uiGuideClose.click();
                    } else if (uiGuideClose02.exists()) {//结束引导02
                        uiGuideClose02.click();
                    } else if (uiBubbleDiaClose.exists()) {//
                        uiBubbleDiaClose.click();
                    } else {//最终的强制搞一波
                        A00UtilTest.baseMethod(uiDevice, 1, appName, new A00UtilTest.MyCallBack() {
                            @Override
                            public void callback(boolean isStop) {
                                appRun = false;//出问题了停止运行
                            }
                        });
                    }
                }
                Thread.sleep(500);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}