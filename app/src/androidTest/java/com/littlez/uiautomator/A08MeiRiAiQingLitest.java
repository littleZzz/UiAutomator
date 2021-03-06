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
 * <p>每日爱清理
 * Date 2019/12/3
 */
public class A08MeiRiAiQingLitest extends TestCase {

    /*app 名字*/
    private String appName = "天天爱清理";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
            A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
            A00UtilTest.errorCount = 0;//重置错误次数

            while (appRun) {
                UiObject uiMainTitle = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/tv_main_title"));
                UiObject uiTvHeart = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/tv_like"));//
                UiObject uiRecycleView = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/community_recycler_view"));//外层recycleView
                UiObject uiRewardButton = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/tv_gold_double"));//看广告获取更多金币
                UiObject uiTV = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/ll_video"));
                UiObject uiTaskWeb = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/web_task"));//任务界面的 外层webview

                if (uiMainTitle.exists()) {//在首页
                    if (uiTV.exists()) uiTV.click();
                } else if (uiTvHeart.exists() || uiRecycleView.exists()) {//是视频界面
                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 3) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 97) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Random rr = new Random();
                        Thread.sleep((15 + rr.nextInt(15) + 1) * 1000);//播放 时长
                    } else if (number <= 99) {//点击任务
                        UiObject uiTask = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/ll_task"));
                        if (uiTask.exists()) uiTask.click();
                        Thread.sleep(3000);
                    } else if (number <= 100) {//点击heart
                        if (uiTvHeart.exists()) uiTvHeart.click();
                    }
                } else if (uiRewardButton.exists()) {//看广告获取更多
                    uiRewardButton.click();
                    UiObject uiadv = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/tt_video_ad_close_layout"));
                    A00UtilTest.backUntilObjOrTime(uiDevice, uiadv, 55);
                    Thread.sleep(2000);
                } else if (uiTV.exists()) {//不在视频界面
                    uiTV.click();
                } else {//处理异常情况
                    UiObject uiDialog = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/tv_close"));
                    UiObject uiDialog02 = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/ib_close_btn"));
                    UiObject uiDialog03 = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/iv_close"));
                    UiObject uiadv = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/tt_video_ad_close_layout"));
                    UiObject uiWebView = new UiObject(new UiSelector().className("android.webkit.WebView").descriptionContains("腾讯社交联盟广告"));
                    UiObject uiupdateCancle = new UiObject(new UiSelector().resourceId("com.xiaoqiao.qclean:id/tv_upgrade_cancel"));//取消更新
                    if (uiDialog.exists()) {//暂不领取
                        uiDialog.click();
                    } else if (uiupdateCancle.exists()) {//取消跟新
                        uiupdateCancle.click();
                    }else if (uiDialog02.exists()) {//获得金币dia
                        uiDialog02.click();
                    } else if (uiDialog03.exists()) {//邀请dia 关闭
                        uiDialog03.click();
                    } else if (uiadv.exists()) {//广告关闭按钮
                        uiadv.click();
                    } else if (uiWebView.exists()) {//广告页面
                        uiDevice.pressBack();
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