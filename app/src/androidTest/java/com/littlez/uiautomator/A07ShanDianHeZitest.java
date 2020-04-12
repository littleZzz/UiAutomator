package com.littlez.uiautomator;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import junit.framework.TestCase;

import java.util.IdentityHashMap;
import java.util.Random;

/**
 * created by xiaozhi
 * <p>闪电盒子极速版 测试用例
 * Date 2019/12/3
 */
public class A07ShanDianHeZitest extends TestCase {

    /*app 名字*/
    private String appName = "闪电盒子极速版";
    private boolean appRun = true;//appRun
    private boolean isNewsOrTv = false;

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        try {
//            A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
//            A00UtilTest.errorCount = 0;//重置
            while (appRun) {

                UiObject uiMain = new UiObject(new UiSelector().resourceId("c.l.b:id/bottom_container"));//主页
                UiObject uiHomeTxt = new UiObject(new UiSelector().resourceId("c.l.b:id/tab_text").text("首页"));//首页
                UiObject uiMissionTxt = new UiObject(new UiSelector().resourceId("c.l.b:id/tab_text").text("任务 "));//任务
                UiObject uiMeTxt = new UiObject(new UiSelector().resourceId("c.l.b:id/tab_text").text("我的"));//我的

                UiObject uiNewsPage = new UiObject(new UiSelector().resourceId("c.l.b:id/title_tv").text("头条资讯"));//新闻主页面
                UiObject uiNewsCollect = new UiObject(new UiSelector().resourceId("c.l.b:id/collect_icon"));//新闻收藏
                UiObject uiTvHeart = new UiObject(new UiSelector().resourceId("c.l.b:id/layout_like"));//视频 heart

                if (uiMain.exists()) {//是主页
                    if (uiHomeTxt.isSelected()) {//选中的首页
                        UiObject uiGetMoney = new UiObject(new UiSelector().resourceId("c.l.b:id/btn_get_money").text("领取奖励"));
                        if (uiGetMoney.exists()) uiGetMoney.click();
                        UiObject uiNewsItem = new UiObject(new UiSelector().resourceId("c.l.b:id/text").text("头条资讯"));
                        UiObject uiTvItem = new UiObject(new UiSelector().resourceId("c.l.b:id/text").text("小视频"));
                        UiObject uiredTypeName = new UiObject(new UiSelector().resourceId("c.l.b:id/red_type_name"));
                        if (uiredTypeName.exists()) {
                            try {
                                String[] xes = uiredTypeName.getText().split("X");
                                String substring = xes[1].substring(0, xes[1].length() - 1);
                                if (Integer.getInteger(substring) > 60) {
                                    isNewsOrTv = true;
                                } else isNewsOrTv = false;
                            } catch (Exception e) {
                                isNewsOrTv = false;
                            }
                        }
                        if (isNewsOrTv) {
                            uiNewsItem.click();
                            isNewsOrTv = false;
                        } else {
                            uiTvItem.click();
                            isNewsOrTv = true;
                        }
                    } else if (uiMissionTxt.isSelected()) {//选中任务
                        uiHomeTxt.click();
                    } else if (uiMeTxt.isSelected()) {//选中我的
                        uiHomeTxt.click();
                    }else  uiHomeTxt.click();

                } else if (uiNewsPage.exists()) {//是新闻资讯主页面
                    Random r = new Random();//主页滑动选择条目
                    int number = r.nextInt(100) + 1;
                    if (number <= 2) {//上滑
                        A00UtilTest.swipUp(uiDevice, 16);
                    } else if (number <= 100) {//下滑
                        A00UtilTest.swipDown(uiDevice, 16);
                    }
                    UiObject uiImage01 = new UiObject(new UiSelector().resourceId("c.l.b:id/image1"));
                    if (uiImage01.exists()) uiImage01.click();//新闻三张图片的第一张id 判断  点击看新闻

                } else if (uiNewsCollect.exists()) {//看新闻页面
                    while (true) {
                        uiDevice.swipe(400, 1200, 534, 602, 10);
                        UiObject uiTvRewardButton = new UiObject(new UiSelector().resourceId("c.l.b:id/tv_rewardButton"));//看视频获取更多
                        UiObject uiRecommendTop = new UiObject(new UiSelector().resourceId("c.l.b:id/recommend_top"));//相关资讯栏
                        if (uiTvRewardButton.exists()) {//关闭循环让外面去处理奖励的事
                            break;
                        } else if (uiRecommendTop.exists()) {
                            uiDevice.pressBack();
                            break;
                        }
                        Thread.sleep(1800);
                    }
                } else if (uiTvHeart.exists()) {//看视频界面
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    if (number <= 2) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 97) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Random rr = new Random();
                        Thread.sleep((25 + rr.nextInt(30) + 1) * 1000);//播放 时长
                    } else if (number <= 100) {//点击我的
                        uiTvHeart.click();
                    }
                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiDialogClose = new UiObject(new UiSelector().resourceId("c.l.b:id/img_close"));
                    UiObject uiDialogClose02 = new UiObject(new UiSelector().resourceId("c.l.b:id/tv_btn"));
                    UiObject uiTvRewardButton = new UiObject(new UiSelector().resourceId("c.l.b:id/tv_rewardButton"));//看视频获取更多
                    UiObject uiAdvClose = new UiObject(new UiSelector().resourceId("c.l.b:id/reward_ad_close"));
                    UiObject uiAdvplaying = new UiObject(new UiSelector().resourceId("c.l.b:id/reward_ad_countdown"));//播放广告界面
                    if (uiDialogClose.exists()) {//获得金币关闭按钮
                        uiDialogClose.click();
                    } else if (uiDialogClose02.exists()) {//获得金币的确认按钮
                        uiDialogClose02.click();
                    } else if (uiTvRewardButton.exists()) {//看视频获取更多金币弹框
                        uiTvRewardButton.click();
                        A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
                    } else if (uiAdvClose.exists()) {//关闭按钮
                        uiAdvClose.click();
                    } else if (uiAdvplaying.exists()) {//播放广告界面
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