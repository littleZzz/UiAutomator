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
 * <p>必看免费小说  测试用例
 * Date 2019/12/3
 */
public class A25BiKanXiaoshuotest extends TestCase {

    /*app 名字*/
    private String appName = "必看免费小说";

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

//        A00UtilTest.baseMethod(uiDevice, 0, appName);//启动时  先关闭其他的
//        A00UtilTest.errorCount = 0;//重置次数
        long startTime = System.currentTimeMillis();//开始时间
        try {
            while (true) {
                //主页
                UiObject uiHome = new UiObject(new UiSelector().resourceId("com.lwby.breader.ad:id/rl_bookstore_home_indicator"));
                UiObject uiReadPage = new UiObject(new UiSelector().resourceId("com.lwby.breader.ad:id/fy_book_view_root"));
                UiObject uiWelare = new UiObject(new UiSelector().resourceId("com.lwby.breader.ad:id/home_task_rl"));

                if (uiHome.exists()) {//是主页
                    UiObject uiBookCity = new UiObject(new UiSelector().resourceId("com.lwby.breader.ad:id/home_store_rl"));
                    UiObject uiBooks = new UiObject(new UiSelector().resourceId("com.lwby.breader.ad:id/home_shelf_rl"));
                    if (uiBookCity.isSelected()) {//选中的书城
                        uiBooks.click();
                    } else if (uiWelare.isSelected()) {//选中的福利
                        welare(uiDevice);   //福利页面
                    } else if (uiBooks.exists()) {//选中的书架
                        UiObject uiBookImg =
                                new UiObject(new UiSelector().resourceId("com.lwby.breader.ad:id/history_scroll_iv"));
                        if (uiBookImg.exists()) uiBookImg.click();//点击阅读历史阅读
                        Thread.sleep(2000);
                    } else {
                        uiBooks.click();
                    }
                } else if (uiReadPage.exists()) {//是阅读界面
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - startTime >= 30 * 60 * 1000) {//回到主页 获取奖励
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
                    UiObject uiClose = new UiObject(new UiSelector().resourceId("com.lwby.breader.ad:id/tv_shelf"));//去书架

                    if (uiClose.exists()) {
                        uiClose.click();
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
    private void welare(UiDevice uiDevice) throws UiObjectNotFoundException, InterruptedException {

        int count = 0;
        while (count <= 5) {
            UiObject uiGet = new UiObject(new UiSelector().description("领200金币"));
            UiObject uiGet02 = new UiObject(new UiSelector().description("领500金币"));
            UiObject uiGet03 = new UiObject(new UiSelector().description("领800金币"));
            UiObject uiGet04 = new UiObject(new UiSelector().description("领1500金币"));
            UiObject uiGet05 = new UiObject(new UiSelector().description("领2000金币"));
            if (uiGet.exists()) uiGet.clickTopLeft();
            if (uiGet02.exists()) uiGet02.clickTopLeft();
            if (uiGet03.exists()) uiGet03.clickTopLeft();
            if (uiGet04.exists()) uiGet04.clickTopLeft();
            if (uiGet05.exists()) uiGet05.clickTopLeft();
            else {
                uiDevice.swipe(620, 1300, 700, 830, 15);
                count++;
            }
            Thread.sleep(2000);
        }
    }

}