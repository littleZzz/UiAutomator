package com.littlez.uiautomator;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.UiWatcher;

import com.littlez.uiautomator.base.Constant;

import junit.framework.TestCase;

import java.util.Random;

/**
 * created by xiaozhi
 * <p>番茄免费小说  测试用例
 * Date 2019/12/3
 */
public class A23FanQieXiaoshuotest extends TestCase {

    /*app 名字*/
    private String appName = "番茄免费小说";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置次数
        long startTime = System.currentTimeMillis();//开始时间
        try {
            while (true) {
                //主页
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.dragon.read:id/ih"));
                UiObject uiReadPage = new UiObject(new UiSelector().resourceId("com.dragon.read:id/a_s"));
                UiObject uiWelare = new UiObject(new UiSelector().resourceId("com.dragon.read:id/io"));
                UiObject uiAdvClose = new UiObject(new UiSelector().resourceId("com.dragon.read:id/tt_video_ad_close"));

                if (uiHome.exists()) {//是主页
                    UiObject uiBookCity = new UiObject(new UiSelector().resourceId("com.dragon.read:id/im"));
                    UiObject uiBooks = new UiObject(new UiSelector().resourceId("com.dragon.read:id/ip"));
                    if (uiBookCity.isChecked()) {//选中的书城
                        uiBooks.click();
                    } else if (uiWelare.isChecked()) {//选中的福利
                        welare(uiDevice, uiAdvClose);   //福利页面
                    } else if (uiBooks.exists()) {//选中的书架
                        UiObject uiBookTitle = new UiObject(new UiSelector().resourceId("com.dragon.read:id/wp"));
                        if (uiBookTitle.exists()) uiBookTitle.click();//点击阅读历史阅读
                        Thread.sleep(2000);
                    } else {
                        uiBooks.click();
                    }
                } else if (uiReadPage.exists()) {//是阅读界面
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - startTime >= 1 * 60 * 1000) {//回到主页 获取奖励
                        uiDevice.pressBack();
                        Thread.sleep(2000);
                        if (uiWelare.exists()) {
                            uiWelare.click();
                            startTime = currentTimeMillis;
                        }
                    } else {
                        A00UtilTest.swipleft(uiDevice);
                        Random r = new Random();
                        int number = r.nextInt(5) + 1;
                        Thread.sleep((15 + number) * 1000);
                    }
                } else {//处理异常情况
                    UiObject uiSeeTvGetMoreDia =
                            new UiObject(new UiSelector().resourceId("adsfadsf"));

                    if (uiSeeTvGetMoreDia.exists()) {
                        uiSeeTvGetMoreDia.click();
                        A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
                    } else {//最终的强制搞一波
                        A00UtilTest.baseMethod(uiDevice, 1, appName);
                    }
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //福利页面处理
    private void welare(UiDevice uiDevice, UiObject uiAdvClose) throws UiObjectNotFoundException, InterruptedException {
        UiObject uiSignSuss = new UiObject(new UiSelector().descriptionContains("看视频再领").className("android.widget.Button"));
        if (uiSignSuss.exists()) {
            uiSignSuss.click();
            A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
        }
        Thread.sleep(1000);
        UiObject uiSignDialog = new UiObject(new UiSelector().description("好的").className("android.view.View"));
        if (uiSignDialog.exists()) uiSignDialog.click();
        Thread.sleep(1000);
        UiObject uiOpenBox = new UiObject(new UiSelector().description("开宝箱得金币").className("android.view.View"));
        if (uiOpenBox.exists()) {
            uiOpenBox.click();
        }
        Thread.sleep(1000);
        UiObject uiSeeTvGetMoreDia = new UiObject(new UiSelector().descriptionContains("看视频领取最高").className("android.widget.Button"));
        if (uiSeeTvGetMoreDia.exists()) {
            uiSeeTvGetMoreDia.click();
            A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
        }
        //滑动到去看广告
        int count = 0;
        while (count <= 3) {
            UiObject uiToSee = new UiObject(new UiSelector().descriptionContains("立即观看").className("android.view.View"));
            if (uiToSee.exists()) {
                uiToSee.clickTopLeft();
                A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
                continue;
            } else {
                uiDevice.swipe(460, 1300, 1200, 480, 16);
            }
            count++;
            Thread.sleep(1000);
        }
        UiObject uiBooks = new UiObject(new UiSelector().resourceId("com.dragon.read:id/ip"));//回到书城
        if (uiBooks.exists()) uiBooks.click();
        else {
            uiDevice.pressBack();
        }
        Thread.sleep(1000);
    }
}