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
 * <p>火山极速版本
 * * Date 2019/12/3
 */
public class A05HuoShanJISutest extends TestCase {

    /*app 名字*/
    private String appName = "火山极速版";
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

                UiObject uiHomeTxt = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/wg").text("首页"));
                UiObject uiredPacTxt = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/wl").text("红包"));
                UiObject uiMeTxt = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/wo").text("我的"));
                UiObject uiViewGroup = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/gi"));//判断首页
                UiObject uiLinearlayout = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/ww"));//判断红包
                UiObject uiGold = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/l7"));//判断我的
                UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/of"));

                if (uiViewGroup.exists()) {//外层Viewpager的下面两层的 Viewgroup 判断是首页
                    UiObject uiAdvGet = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/a2q").text("领取"));
                    if (uiAdvGet.exists()) uiAdvGet.click();//领取广告收益
                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 2) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 94) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Random rr = new Random();
                        Thread.sleep((8 + rr.nextInt(15) + 1) * 1000);//播放 时长
                    } else if (number <= 97) {//点击我的
                        if (uiMeTxt.exists()) uiMeTxt.click();
                    } /*else if (number <= 97) {//点击红包
                        if (uiredPacTxt.exists()) uiredPacTxt.click();
                    }*/ else {//3点击心
                        if (uiHeart.exists()) uiHeart.click();
                    }
                } else if (uiGold.exists()) {//现金余额 判断是我的界面
                    uiHomeTxt.click();
                } /*else if (uiLinearlayout.exists()) {//外层Viewpager的下面两层的 Linearlayout 判断是首页
                    //红包页面玩去哪是html  暂时不做算了
                    UiObject uiSignTxt = new UiObject(new UiSelector().description("签到").className("android.view.View"));
                    if (uiSignTxt.exists()) uiSignTxt.click();
                }*/ else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiInviteDia = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/a5v"));
                    UiObject uiShaoNianBaohu = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/qt"));

                    if (uiInviteDia.exists()) {//邀请码弹框关闭按钮  邀请好友弹框
                        uiInviteDia.click();
                    } else if (uiShaoNianBaohu.exists()) {//青少年保护弹框
                        uiShaoNianBaohu.click();
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