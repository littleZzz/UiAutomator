package com.littlez.uiautomator;

import android.app.Instrumentation;

import junit.framework.TestCase;

import java.util.Random;

import androidx.test.espresso.action.Swipe;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.littlez.uiautomator.util.LogUtil;

/**
 * created by xiaozhi
 * <p>趣头条 测试用例
 * Date 2019/12/3
 */
public class A06QuTouTiaotest extends TestCase {

    /*app 名字*/
    private String appName = "趣头条";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置
        long startTime = System.currentTimeMillis();//开始时间
        boolean isToMoney = true;
        while (appRun) {
            try {
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/m7"));//主页 tabid
                UiObject uiFunItem = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/bml"));//聊天 收藏栏
                UiObject uiTvOut = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/tp"));//视频最外层
                //看视频的广告结束后的关闭按钮
                UiObject uiSeeTvClose = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/tt_video_ad_close_layout"));
                UiObject uiSeeTVGetMoreDia = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/v4"));//看视频再领取金币弹框

                if (uiHome.exists()) {//是主页  滑动选择条目
                    UiObject uiHomeChild = uiHome.getChild(new UiSelector().className("android.widget.FrameLayout").index(0));
                    UiObject uiHomeChild3 = uiHome.getChild(new UiSelector().className("android.widget.FrameLayout").index(3));
                    if (uiHomeChild.isSelected()) {//选中的是首页
                        if (isToMoney && uiHomeChild3.exists()) {
                            uiHomeChild3.click();
                            isToMoney = false;
                            continue;
                        }
                        Random r = new Random();//主页滑动选择条目
                        int number = r.nextInt(100) + 1;
                        if (number <= 2) {//上滑
                            A00UtilTest.swipUp(uiDevice, 10);
                        } else if (number <= 100) {//下滑
                            A00UtilTest.swipDown(uiDevice, 10);
                        }
                        UiObject uiGet = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/bsx").text("领取"));//首页领取
                        UiObject uiGetAgain = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/bso"));//看视频再领取
                        if (uiGet.exists()) {
                            uiGet.click();
                            continue;
                        } /*else if (uiGetAgain.exists()) {// 等查找id 再领取暂时不做了
                        }*/
                        Thread.sleep(500);
                        UiObject uiAuther = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/aly"));
                        if (uiAuther.exists()) uiAuther.click();//跳转到查看任务
                        Thread.sleep(600);//要停一下  给一些加载时间
                    } else if (uiHomeChild3.exists() && uiHomeChild3.isSelected()) {//选中的是赚钱页面
                        //如果看视频领取金币存在
                        UiObject uiSeeTv = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/bgo").text("看视频领金币"));
                        UiObject uiBoxGet = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/bhf").text("立即领取"));//箱子获取
                        UiObject uiDailySeeTv = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/bf7").text("看广告视频拿金币"));
                        UiObject uiNowSee = uiDailySeeTv.getFromParent(new UiSelector().resourceId("com.jifen.qukan:id/bff").text("立即观看"));
                        A00UtilTest.swipUp(uiDevice);//滑动一下 确保在开始
                        if (uiSeeTv.exists()) {//看视频赚金币存在
                            uiSeeTv.click();
                            A00UtilTest.backUntilObjOrTime(uiDevice, uiSeeTvClose, 60);
                        } else if (uiBoxGet.exists()) {//宝箱金币获取
                            uiBoxGet.click();
                            UiObject uiReplay = new UiObject(new UiSelector().className("android.widget.TextView").text("点击重播"));
                            A00UtilTest.backUntilObjOrTime(uiDevice, null, 50);
                        } else {
                            A00UtilTest.swipDown(uiDevice);//滑动一下 查看是否有日常任务
                            if (uiDailySeeTv.exists() && uiNowSee.exists()) {
                                uiDailySeeTv.click();
                                UiObject uiReplay = new UiObject(new UiSelector().className("android.widget.TextView").text("点击重播"));
                                A00UtilTest.backUntilObjOrTime(uiDevice, null, 50);
                            } else {
                                uiHomeChild.click();//否则回到首页
                            }
                        }
                    }
                } else if (uiFunItem.exists() && uiTvOut.exists()) {//是视频
                    boolean isRun = true;
                    while (isRun) {//循环观看视频
                        UiObject uiAward = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/aw_"));//阅读奖励
                        if (uiAward.exists()) {
                            uiAward.click();
                            Thread.sleep(2000);
                            break;//要跳过这次循环
                        }
                        UiObject uiRePlay = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/btv"));
                        if (uiRePlay.exists()) {
                            isRun = false;
                            uiDevice.pressBack();
                        }
                        Thread.sleep(2000);
                    }
                } else if (uiFunItem.exists()) {//是新闻

                    boolean isRun = true;
                    int count = 0;//记录滑动次数  超过一定次数就直接返回
                    while (isRun) {
                        uiDevice.swipe(400, 1200, 534, 602, 10);
                        count++;
                        UiObject uiAward = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/aw_"));//阅读奖励
                        if (uiAward.exists()) {
                            uiAward.click();
                            Thread.sleep(2000);
                            break;//要跳过这次循环
                        }
                        if (count >= 35) {
                            isRun = false;
                            uiDevice.pressBack();
                        }
                        Thread.sleep(1500);
                    }
                } else if (uiSeeTVGetMoreDia.exists()) {//看视频在再领取弹框界面
                    uiSeeTVGetMoreDia.click();
                    Thread.sleep(2000);
                    UiObject uiReplay = new UiObject(new UiSelector().className("android.widget.TextView").text("点击重播"));
                    A00UtilTest.backUntilObjOrTime(uiDevice, null, 45);
                } else {
                    //处理异常情况
                    UiObject uiClose = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/a5n"));
                    UiObject uiClose02 = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/v8"));//阅读奖励点击后的dialog
                    UiObject uiClose03 = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/yc"));
                    UiObject uiClose04 = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/a92"));
                    UiObject uiClose05 = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/aw_"));//阅读奖励
                    UiObject uiClose06 = new UiObject(new UiSelector().resourceId("com.jifen.qukan:id/aec"));//dialog

                    if (uiClose.exists()) {
                        uiClose.click();
                    } else if (uiClose02.exists()) {
                        uiClose02.click();
                    } else if (uiClose03.exists()) {
                        uiClose03.click();
                    } else if (uiClose04.exists()) {
                        uiClose04.click();
                    } else if (uiClose05.exists()) {
                        uiClose05.click();
                    } else if (uiClose06.exists()) {
                        uiClose06.click();
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}