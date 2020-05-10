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

        UiObject uiBottomItem = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/we"));//底部栏
        UiObject uiHomeTxt = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/wg").text("首页"));
        UiObject uiRedPackTxt = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/wl").text("红包"));
        UiObject uiViewGroup = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/gi"));//判断首页
        UiObject uiHeart = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/of"));
        UiObject uiAdvGet = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/a2q").text("领取"));

        try {
            A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
            A00UtilTest.errorCount = 0;//重置错误次数
            while (appRun) {

                if (uiViewGroup.exists()) {//外层Viewpager的下面两层的 Viewgroup 判断是首页
                    if (uiAdvGet.exists()) uiAdvGet.click();//领取广告收益
                    Random r = new Random();
                    int number = r.nextInt(100) + 1;
                    /*随机数 进行判断 点击心或者滑动到下一个视频*/
                    if (number <= 2) {//上一条
                        A00UtilTest.swipUp(uiDevice);
                    } else if (number <= 97) {//下一条
                        A00UtilTest.swipDown(uiDevice);
                        Random rr = new Random();
                        Thread.sleep((2 + rr.nextInt(5) + 1) * 1000);//播放 时长
                    } else if (number <= 99) {//点击红包
                        if (uiRedPackTxt.exists()) uiRedPackTxt.click();
                    }  else {//3点击心
                        if (uiHeart.exists()) uiHeart.click();
                    }
                } else if (uiBottomItem.exists()) {//不在首页  点击首页就是了
                    uiHomeTxt.click();
                }  else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiInviteDia = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/a5v"));
                    UiObject uiShaoNianBaohu = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/qt"));
                    UiObject uiUpdateLate = new UiObject(new UiSelector().resourceId("com.ss.android.ugc.livelite:id/aih"));//跟新 取消

                    if (uiInviteDia.exists()) {//邀请码弹框关闭按钮  邀请好友弹框
                        uiInviteDia.click();
                    } else if (uiShaoNianBaohu.exists()) {//青少年保护弹框
                        uiShaoNianBaohu.click();
                    }else if (uiUpdateLate.exists()) {//跟新升级 暂不跟新
                        uiUpdateLate.click();
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