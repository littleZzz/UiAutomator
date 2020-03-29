package com.littlez.uiautomator;

import android.app.Instrumentation;
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
 * <p>微鲤畅聊版  测试用例
 * Date 2019/12/3
 */
public class A08WeiLiKanKantest extends TestCase {

    /*app 名字*/
    private String appName = "微鲤看看";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
            A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
            A00UtilTest.errorCount = 0;//重置
            while (true) {
                //主页 根据和 底部bottom 同一级的上级内容判断
                UiObject uiHome = new UiObject(new UiSelector().resourceId("cn.weli.story:id/rl_content_foot"));
                LogUtil.e("1");
                if (uiHome.exists()) {//是主页  滑动选择条目
                    LogUtil.e("2");

                    UiObject uiHomeTab = new UiObject(new UiSelector().resourceId("cn.weli.story:id/rl_nav"));
                    if (uiHomeTab.exists()) {//选中的是首页
                        //主页滑动选择条目
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        if (number <= 2) {//上滑
                            A00UtilTest.swipUp(uiDevice, 10);
                        } else if (number <= 95) {//下滑
                            A00UtilTest.swipDown(uiDevice, 10);
                        } else {//做一些其他额外的附加任务
                            UiObject uiGet =
                                    new UiObject(new UiSelector().resourceId("cn.weli.story:id/tv_time").text("时段奖励"));//首页领取
                            if (uiGet.exists()) {
                                uiGet.click();
                                continue;
                            }
                        }
                        Thread.sleep(500);
                        UiObject uiTv = new UiObject(new UiSelector().resourceId("cn.weli.story:id/view_video_message"));
                        if (uiTv.exists()) {//视频
                            uiTv.click();
                        } else {//新闻
                            UiObject uititle = new UiObject(new UiSelector().resourceId("cn.weli.story:id/tv_title"));
                            uititle.click();
                        }
                        Thread.sleep(600);//要听一下  给一些加载时间
                    }

                } else {//去检测是不是我想要的界面  是就进行处理
                    UiObject uiVideoTitle = new UiObject(new UiSelector().resourceId("cn.weli.story:id/tv_video_title"));//视频标题
                    UiObject uiCollect = new UiObject(new UiSelector().resourceId("cn.weli.story:id/btn_collect"));//收藏
                    LogUtil.e("sdf000");
                    if (uiVideoTitle.exists()) {//是视频
                        LogUtil.e("sdf999999");
                        boolean isRun = true;
                        while (isRun) {//循环观看视频
                            UiObject uiRePlay = new UiObject(new UiSelector().resourceId("cn.weli.story:id/ll_close_ad"));
                            if (uiRePlay.exists()) {
                                isRun = false;
                                uiDevice.pressBack();
                            }
                            Thread.sleep(2000);
                        }
                    } else if (uiCollect.exists()) {//是新闻

                        LogUtil.e("sdf");
                        boolean isRun = true;
                        while (isRun) {
                            LogUtil.e("2656");
                            UiObject uilike
                                    = new UiObject(new UiSelector().resourceId("cn.weli.story:id/ll_center_dislike"));//喜欢
                            UiObject uiMore
                                    = new UiObject(new UiSelector().resourceId("cn.weli.story:id/tv_height_more"));//查看更多
                            LogUtil.e("99987");
                            if (uilike.exists()) {
                                isRun = false;
                                uiDevice.pressBack();
                            } else if (uiMore.exists()) {
                                uiMore.click();
                                Thread.sleep(500);
                            } else {
                                uiDevice.swipe(400, 1200, 534, 802, 10);
                                Thread.sleep(2000);
                            }
                        }

                    } else {
                        //处理异常情况
                        UiObject uiClose = new UiObject(new UiSelector().resourceId("cn.weli.story:id/tv_ok"));

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