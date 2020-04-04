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
 * <p>趣看点  测试用例
 * Date 2019/12/3
 */
public class A13QuKanDiantest extends TestCase {

    /*app 名字*/
    private String appName = "趣看点";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置

        while (true) {
            try {
                //主页
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.zhangku.qukandian:id/mBottomHomeView"));
                if (uiHome.exists()) {//是主页
                    UiObject uiMission = new UiObject(new UiSelector().resourceId("com.zhangku.qukandian:id/mBottomTaskView"));
                    if (uiHome.isSelected()) {//选中的首页
                        Random r = new Random();      //item
                        int number = r.nextInt(100) + 1;
                        if (number <= 2) {//上滑
                            A00UtilTest.swipUp(uiDevice, 15);
                        } else if (number <= 97) {//下滑
                            A00UtilTest.swipDown(uiDevice, 15);
                        } else {
                            uiMission.click();//跳转到查看任务
                            continue;
                        }
                        UiObject uiGet = new UiObject(
                                new UiSelector().resourceId("com.zhangku.qukandian:id/getGoldTV").text("可领取"));//首页领取
                        if (uiGet.exists()) {
                            uiGet.click();
                            continue;
                        }
                        UiObject uiAuthor = new UiObject(new UiSelector()
                                .resourceId("com.zhangku.qukandian:id/item_information_adapter_one_pic_author").instance(0));
                        UiObject uiTVTitle = new UiObject(new UiSelector()
                                .resourceId("com.zhangku.qukandian:id/item_video_fragment_adapter_view_title"));
                        if (uiTVTitle.exists()) uiTVTitle.click();//需要优先点击视屏观看
                        else uiAuthor.click();
                        Thread.sleep(1000);//要听一下  给一些加载时间
                    } else {//其他
                        uiHome.click();
                    }
                } else {//去检测是不是我想要的界面  是就进行处理
                    UiObject uiCollect = new UiObject(new UiSelector().resourceId("com.zhangku.qukandian:id/collectIV"));
                    UiObject uitvNoLike =
                            new UiObject(new UiSelector().resourceId("com.zhangku.qukandian:id/header_video_layout_play_nolike"));//视频
                    if (uitvNoLike.exists()) {//是视频
                        boolean isRun = true;
                        while (isRun) {//循环观看视频
                            UiObject uiRePlay = new UiObject(new UiSelector().resourceId("com.zhangku.qukandian:id/start_play_btn"));
                            if (uiRePlay.exists()) {
                                isRun = false;
                                uiDevice.pressBack();
                            }
                            Thread.sleep(2000);
                        }
                    } else if (uiCollect.exists()) {//新闻
                        boolean isRun = true;
                        while (isRun) {
                            UiObject uiNewsNoLike = new UiObject(
                                    new UiSelector().resourceId("com.zhangku.qukandian:id/header_information_layout_nolike"));
                            UiObject uiMore = new UiObject(
                                    new UiSelector().resourceId("com.zhangku.qukandian:id/header_information_read_all_btn"));
                            if (uiMore.exists()) {
                                uiMore.click();
                            } else if (uiNewsNoLike.exists()) {
                                isRun = false;
                                uiDevice.pressBack();
                            } else {
                                uiDevice.swipe(400, 1200, 534, 802, 10);
                                Thread.sleep(2000);
                            }
                        }
                    } else {//处理异常情况
                        //靠点赞按钮判断是不是在播放视频那个整个界面
                        UiObject uiDialog = new UiObject(new UiSelector().resourceId("com.zhangku.qukandian:id/dialog_operate_close"));
                        UiObject uiGetDialog =
                                new UiObject(new UiSelector().resourceId("com.zhangku.qukandian:id/dialog_sign_cancel"));

                        if (uiDialog.exists()) {//
                            uiDialog.click();
                        } else if (uiGetDialog.exists()) {
                            uiGetDialog.click();
                        } else {//最终的强制搞一波
                            A00UtilTest.baseMethod(uiDevice, 1, appName);
                        }
                    }
                }
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}