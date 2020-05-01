package com.littlez.uiautomator;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.littlez.uiautomator.util.LogUtil;

import junit.framework.TestCase;

import java.util.Random;

/**
 * created by xiaozhi
 * <p>赚钱小视频  测试用例
 * Date 2019/12/3
 */
public class A12ZhuanQianTvtest extends TestCase {

    /*app 名字*/
    private String appName = "赚钱小视频";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置次数
        try {
            while (appRun) {
                //主页
                UiObject uiMain = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/fixed_bottombar_container"));//底部最外层控件
                UiObject uiTVRecycle = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/short_video_recyclerView"));//看短视频界面
                UiObject uiSeeTVGetMore = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/gold_dialog_bt_ll"));
                UiObject uiAdvClose = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/tt_video_ad_close_layout"));//广告关闭
                UiObject uiAdvPlayPage = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:style/AlertDialog.AppCompat.Light"));//
                UiObject uiAdvPlayPage02 = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/tt_reward_root"));//
                UiObject uiGetMoreClose = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/money_dialog_getcash_new2_close"));//获取更多的关闭按钮
                UiObject uiImgClose = new UiObject(new UiSelector().packageName("com.sljh.zqxsp").className("android.widget.ImageView").index(1));

                if (uiMain.exists()) {//是主页  滑动选择条目
                    UiObject uiSmallTv = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/menu_small_video"));
                    if (uiSmallTv.exists()) uiSmallTv.click();
                } else if (uiTVRecycle.exists()) {//是视频
                    UiObject uiConin = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/newsgold_coin_tx1"));
                    if (uiConin.exists()) {
                        uiConin.click();
                    } else {
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        /*随机数 进行判断 点击心或者滑动到下一个视频*/
                        if (number <= 3) {//上一条
                            A00UtilTest.swipUp(uiDevice);
                        } else if (number <= 98) {//下一条
                            A00UtilTest.swipDown(uiDevice);
                            Random rr = new Random();
                            Thread.sleep((20 + rr.nextInt(15) + 1) * 1000);//播放 时长
                        } else {//3点击心
                            UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/item_short_video_heart"));
                            if (uiHeart.exists()) uiHeart.click();
                        }
                    }
                } else if (uiSeeTVGetMore.exists()) {//看视频获得更多
                    uiSeeTVGetMore.click();
                    Thread.sleep(2000);
                    //A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
                } else if (uiGetMoreClose.exists()) {//获取更多关闭
                    uiGetMoreClose.click();
                } else if (uiAdvPlayPage.exists() || uiAdvPlayPage02.exists()) {//看视频广告中界面
                    if (uiAdvClose.exists()) uiAdvClose.click();
                    else if (uiImgClose.exists()) uiImgClose.click();
                } else if (uiAdvClose.exists() || uiImgClose.exists()) {//看视屏广告 关闭按钮存在
                    if (uiAdvClose.exists()) uiAdvClose.click();
                    else if (uiImgClose.exists()) uiImgClose.click();
                } else {
                    UiObject uiDia02 = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/dialog_close"));//应用评价不评价 第二个弹框
                    UiObject uiDia01 = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/msg_dialog_left"));//应用评价不评价
                    UiObject uiTuiSongDia = new UiObject(new UiSelector().resourceId("com.sljh.zqxsp:id/money_dialog_open_notice_close"));

                    if (uiTuiSongDia.exists()) {//开启推送的dia
                        uiTuiSongDia.click();
                    } else if (uiDia02.exists()) {
                        uiDia02.click();
                    } else if (uiDia01.exists()) {
                        uiDia01.click();
                    } else if (uiAdvClose.exists()) {//广告关闭
                        uiAdvClose.click();
                    } else {//最终的强制搞一波
                        A00UtilTest.baseMethod(uiDevice, 1, appName, new A00UtilTest.MyCallBack() {
                            @Override
                            public void callback(boolean isStop) {
                                appRun = false;//出问题了停止运行
                            }
                        });
                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}