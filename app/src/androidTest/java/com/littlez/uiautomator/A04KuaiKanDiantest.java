package com.littlez.uiautomator;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import junit.framework.TestCase;

import java.util.Random;

/**
 * created by xiaozhi
 * <p>快看点
 * Date 2019/12/3
 */
public class A04KuaiKanDiantest extends TestCase {

    /*app 名字*/
    private String appName = "快看点";
    private boolean appRun = true;//appRun
    private boolean isToEarn = true;//是否去执行签到人物


    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName, null);
        ;//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置次数
        try {
            UiObject uiIsHomePage = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/channel_manage_btn"));//+号按钮id
            UiObject uiIsMissionPage = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/avatar"));//头像id
            UiObject uiToHomePage = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/tab_tv").text("首页"));//文本id
            UiObject uiToMissionPage = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/tab_tv").text("任务"));//文本id
            UiObject uiHomePageItem = new UiObject(new UiSelector().resourceId("com.yuncheapp.android.pearl:id/name"));//作者名字文字id

            UiObject uiIsTVPlayPage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/layout_video_container"));//viewgroup 下frament id
            UiObject uiFuDai = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/fudai_icon"));//福袋
            UiObject uiIsNewsPage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/news_detail_layout"));//viewgroup 下frament id
            UiObject uiNewsOver = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_title").text("评论"));//评论文字id

            UiObject uiDiaClose = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/iv_close"));//福袋等 dia 关闭按钮
            UiObject uiAdvClose = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tt_video_ad_close_layout"));//广告的关闭按钮


            while (appRun) {
                //主页
                if (uiIsHomePage.exists()) {//是首页
                    A00UtilTest.swipDown(uiDevice, 25);//下滑一下
                    if (uiHomePageItem.exists()) uiHomePageItem.click();
                } else if (uiIsMissionPage.exists()) {//是我的页面
                    if (uiToHomePage.exists()) uiToHomePage.click();
                } else if (uiIsTVPlayPage.exists()) {//是视频
                    boolean isRun = true;
                    long startTime = System.currentTimeMillis();//开始时间
                    while (isRun) {
                        long currentTimeMillis = System.currentTimeMillis();
                        if (uiFuDai.exists()) {//福袋存在  点击 然后退出当前循环
                            uiFuDai.click();
                            A00UtilTest.backUntilObjOrTime(uiDevice, uiDiaClose, 20);
                        } else if (currentTimeMillis - startTime >= (2.5 + A00UtilTest.getRandom(2)) * 60 * 1000) {//超过5分钟自动退出
                            isRun = false;
                            uiDevice.pressBack();
                        }
                        Thread.sleep(5000);
                    }

                } else if (uiIsNewsPage.exists()) {//是新闻
                    boolean isRun = true;
                    while (isRun) {
                        uiDevice.swipe(400, 1200, 534, 602, 20);
                        Thread.sleep(3000);
                        if (uiNewsOver.exists()) {
                            isRun = false;
                            uiDevice.pressBack();
                        }
                    }
                } else {
                    //com.xiangkan.android:id/updateSingleTv  升级按钮   com.xiangkan.android:id/closeIv  升级关闭
                    //处理异常情况 首页领取奖励后的dialog
                    UiObject uiSingDia = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_sign_btn"));//签到弹框看视频在领取
                    UiObject uiCancleUpdate = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/closeIv"));//取消跟新

                    if (uiSingDia.exists()) {//签到弹框 看视频再领取
                        uiSingDia.click();
                        A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
                    } else if (uiDiaClose.exists()) {//福袋的关闭  等
                        uiDiaClose.click();
                    } else if (uiCancleUpdate.exists()) {//取消跟新
                        uiCancleUpdate.click();
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