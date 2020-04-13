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
 * <p>搜狐资讯  测试用例
 * Date 2019/12/3
 */
public class A12SouHuZiXuntest extends TestCase {

    /*app 名字*/
    private String appName = "搜狐资讯";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置

        while (appRun) {
            try {
                //主页
                UiObject uiItem = new UiObject(new UiSelector().resourceId("com.sohu.infonews:id/item_title"));

                if (uiItem.exists()) {//是主页
                    // 主页
                    UiObject uiChannelMore = new UiObject(new UiSelector().resourceId("com.sohu.infonews:id/channel_more_1"));
                    if (uiChannelMore.exists()) {//选中的首页
                        //item
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        if (number <= 2) {//上滑
                            A00UtilTest.swipUp(uiDevice, 15);
                        } else if (number <= 100) {//下滑
                            A00UtilTest.swipDown(uiDevice, 15);
                        }
                        UiObject uiMediaTime = new UiObject(new UiSelector().resourceId("com.sohu.infonews:id/article_time"));
                        Thread.sleep(1000);//要听一下  给一些加载时间
                    } else {//其他
                        uiItem.click();
                    }
                } else {//去检测是不是我想要的界面  是就进行处理

                    UiObject uiComment = new UiObject(new UiSelector().resourceId("com.sohu.infonews:id/comment_btn"));
                    UiObject uiTvTitle = new UiObject(new UiSelector().resourceId("com.sohu.infonews:id/textTitle"));//视频

                    if (uiTvTitle.exists()) {//是视频
                        UiObject uiRePlay = new UiObject(new UiSelector().resourceId("com.sohu.infonews:id/tv_replay"));
                        boolean isRun = true;
                        long startTime = System.currentTimeMillis();//开始时间
                        while (isRun) {
                            long currentTimeMillis = System.currentTimeMillis();
                            if (currentTimeMillis - startTime >= 300 * 1000) {//超过某个时间
                                uiDevice.pressBack();
                                isRun = false;
                            } else if (uiRePlay.exists()) {
                                uiDevice.pressBack();
                                isRun = false;
                            }
                            Thread.sleep(5000);
                        }
                    } else if (uiComment.exists()) {//新闻
                        boolean isRun = true;
                        while (isRun) {
                            UiObject uiHotComment = new UiObject(
                                    new UiSelector().resourceId("com.sohu.infonews:id/hot_comment_tip"));
                            if (uiHotComment.exists()) {
                                isRun = false;
                                uiDevice.pressBack();
                            } else {
                                uiDevice.swipe(400, 1200, 534, 802, 10);
                                Thread.sleep(2000);
                            }
                        }

                    } else {//处理异常情况
                        //靠点赞按钮判断是不是在播放视频那个整个界面
                        UiObject uinet = new UiObject(new UiSelector().resourceId("com.sohu.infonews:id/blank_btn1"));
                        UiObject uiDialog = new UiObject(new UiSelector().resourceId("com.sohu.infonews:id/act_close_image"));
                        UiObject uiDialog02 = new UiObject(new UiSelector().resourceId("android:id/button2"));

                        if (uinet.exists()) {//
                            uiDevice.pressBack();
                        } else if (uiDialog.exists()) {//
                            uiDialog.click();
                        } else if (uiDialog02.exists()) {//提示对话框
                            uiDialog02.click();
                        } else {//最终的强制搞一波
                            A00UtilTest.baseMethod(uiDevice, 1, appName, new A00UtilTest.MyCallBack() {
                                @Override
                                public void callback(boolean isStop) {
                                    appRun = false;//出问题了停止运行
                                }
                            });
                        }
                    }
                }
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}