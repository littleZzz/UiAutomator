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
 * <p>想看  测试用例
 * Date 2019/12/3
 */
public class A11XiangKantest extends TestCase {

    /*app 名字*/
    private String appName = "想看";

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
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/bottom_tab_layout"));
                if (uiHome.exists()) {//是主页  滑动选择条目
                    UiObject uiHomeTitle = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tab_layout"));
                    if (uiHomeTitle.exists()) {//选中的是首页
                        //主页滑动选择条目
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        if (number <= 2) {//上滑
                            A00UtilTest.swipUp(uiDevice, 12);
                        } else if (number <= 95) {//下滑
                            A00UtilTest.swipDown(uiDevice, 12);
                        } //做一些其他额外的附加任务
                        //首页领取  做一个时段任务奖励的领取
                        UiObject uiGet = new UiObject(new UiSelector()
                                .resourceId("com.xiangkan.android:id/tv_box_time_new").text("领金币"));
                        if (uiGet.exists()) {
                            uiGet.click();
                            continue;
                        }
                        Thread.sleep(500);//没有时段领取 选择一条进行跳转
                        UiObject uiTitle = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tvTitle"));
                        uiTitle.click();//这个不分是视频还是阅读了
                        Thread.sleep(1500);//要听一下  给一些加载时间
                    }

                } else {//去检测是不是我想要的界面  是就进行处理
                    UiObject uiTVTitle =
                            new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/vo_video_detail_title"));//标题
                    UiObject uiCollect = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/img_thumbUp"));//收藏
                    LogUtil.e("1");
                    if (uiTVTitle.exists() && uiCollect.exists()) {//是视频
                        Random r = new Random();
                        int number = r.nextInt(90) + 1;
                        Thread.sleep((45 + number) * 1000);
                        uiDevice.pressBack();
                    } else if (uiCollect.exists()) {//是新闻
                        LogUtil.e("2");
                        boolean isRun = true;
                        while (isRun) {
                            LogUtil.e("3");
                            UiObject uiShare =
                                    new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/share_wechat_tv"));//分享微信
                            UiObject uiMore =
                                    new UiObject(new UiSelector().text("点击阅读全文").className("android.view.View"));//这个查找慢
                            LogUtil.e("4");
                            if (uiMore.exists()) {//查看更多
                                LogUtil.e("5");
                                uiMore.click();
                                Thread.sleep(500);
                            } else if (uiShare.exists()) {
                                LogUtil.e("6");
                                isRun = false;
                                uiDevice.pressBack();
                            } else {
                                LogUtil.e("7");
                                uiDevice.swipe(400, 1200, 534, 802, 10);
                                Thread.sleep(1500);
                            }
                        }
                    } else {
                        //处理异常情况 首页领取奖励后的dialog
                        UiObject uiClose = new UiObject(new UiSelector().resourceId("sdf"));//时段奖励领取

                        if (uiClose.exists()) {
                            uiClose.click();
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