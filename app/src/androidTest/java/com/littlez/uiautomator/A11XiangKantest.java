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
    private boolean appRun = true;//appRun
    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName,null);;//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置次数
        boolean isToEarn = true;
        try {
            while (appRun) {
                //主页
                UiObject uiMain = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/bottom_tab_layout"));
                UiObject uiTVTitle = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/vo_video_detail_title"));//标题
                UiObject uiCollect = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/img_thumbUp"));//收藏
                UiObject uiAdvClose = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tt_video_ad_close_layout"));//广告的关闭按钮

                if (uiMain.exists()) {//是主页  滑动选择条目
                    UiObject uiHomeTitle = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/ll_home_title_bar"));
                    UiObject uiEarn = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/earn_coins_scroll_view"));//scrllView
                    if (uiHomeTitle.exists()) {//选中的是首页
                        if (isToEarn) {
                            UiObject uiEarnMoney = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/ll_tap").instance(2));
                            if (uiEarnMoney.exists()) {//赚金币存在
                                uiEarnMoney.click();
                                Thread.sleep(2000);
                                isToEarn = false;
                                continue;
                            }
                        }
                        Random r = new Random(); //主页滑动选择条目
                        int number = r.nextInt(100) + 1;
                        if (number <= 1) {//上滑
                            A00UtilTest.swipUp(uiDevice, 10);
                        } else if (number <= 100) {//下滑
                            A00UtilTest.swipDown(uiDevice, 10);
                        } //做一些其他额外的附加任务
                        //首页领取  做一个时段任务奖励的领取  想看 时段任务暂时不领取了 太慢了
                        UiObject uiGet = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_box_time_new").text("领金币"));
                        if (uiGet.exists()) {
                            uiGet.click();
                            continue;
                        }
                        Thread.sleep(500);//没有时段领取 选择一条进行跳转
                        UiObject uiAuther = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tvInfo").instance(0));
                        uiAuther.click();//这个不分是视频还是阅读了
                        Thread.sleep(1500);//要听一下  给一些加载时间

                    } else if (uiEarn.exists()) {//选中的是赚金币页面 外层scrollView 判断
                        A00UtilTest.swipUp(uiDevice);
                        UiObject uiSeeTv = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_watch_video").text("看视频"));
                        if (uiSeeTv.exists()) {//看视频的金币存在
                            uiSeeTv.click();
                            A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
                        }
                        UiObject uiToRead = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/fudai_goto_read"));
                        if (uiToRead.exists()) uiToRead.click();//前往首页阅读
                    }
                } else if (uiTVTitle.exists() && uiCollect.exists()) {//是视频
                    UiObject uiRePlay = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/replay_tv"));
                    UiObject uiFuDai = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/fudai_icon"));
                    A00UtilTest.backUntilObjOrTime(uiDevice, uiFuDai, uiRePlay, 300);//遇到福袋、重播、大于300秒跳出判断

                } else if (uiCollect.exists()) {//是新闻
                    boolean isRun = true;
                    boolean isGetMore = true;
                    while (isRun) {
                        UiObject uiShare = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/share_wechat_tv"));//分享微信
                        UiObject uiFuDai = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/fudai_icon"));
                        if (uiFuDai.exists()) {//福袋奖励
                            uiFuDai.click();
                            isRun = false;
                        } else if (uiShare.exists()) {
                            if (isGetMore) {//通过能查找的来进行判断
                                int right = uiShare.getBounds().right / 2;
                                int top = uiShare.getBounds().top;
                                uiDevice.swipe(right, top - 70, right, top - 70, 20);
                                isGetMore = false;
                                Thread.sleep(1500);
                                continue;
                            }
                            isRun = false;
                            uiDevice.pressBack();
                        } else {
                            Thread.sleep(1000);
                            uiDevice.swipe(400, 1200, 534, 802, 10);
                        }
                    }
                } else {
                    //处理异常情况 首页领取奖励后的dialog
                    UiObject uiSingDia = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_sign_btn"));//签到弹框看视频在领取
                    UiObject uiTimeWare = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_integer_coin_action"));//时段奖励看视频再领取
                    UiObject uiFudaiWareDia = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_go_read"));//福袋奖励

                    if (uiSingDia.exists()) {//签到弹框 看视频再领取
                        uiSingDia.click();
                        A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
                    } else if (uiTimeWare.exists()) {//时段奖励看视频再领取
                        uiTimeWare.click();
                        A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
                    } else if (uiFudaiWareDia.exists()) {//福袋奖励 点击再得金币
                        uiFudaiWareDia.click();
                        A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
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