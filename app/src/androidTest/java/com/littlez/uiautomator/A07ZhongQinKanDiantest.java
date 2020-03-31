package com.littlez.uiautomator;

import android.app.Instrumentation;
import android.util.Log;

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
 * <p>中青看点 测试用例
 * Date 2019/12/3
 */
public class A07ZhongQinKanDiantest extends TestCase {

    /*app 名字*/
    private String appName = "中青看点";

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
                UiObject uiSearch = new UiObject(new UiSelector().resourceId("cn.youth.news:id/a4a"));
                UiObject uiHeart = new UiObject(new UiSelector().resourceId("cn.youth.news:id/sm"));
                UiObject uiToGet = new UiObject(new UiSelector().resourceId("cn.youth.news:id/rc"));//广告的待领取

                if (uiSearch.exists()) {//选中的是首页
                    UiObject uiTv = new UiObject(new UiSelector().resourceId("cn.youth.news:id/a_r"));
                    uiTv.click();
                    Thread.sleep(2000);
                    if (!uiHeart.exists()) {
                        UiObject uiPrise = new UiObject(new UiSelector().resourceId("cn.youth.news:id/a_o").instance(0));
                        uiPrise.click();
                        Thread.sleep(2000);
                    }
                } else if (uiHeart.exists() || uiToGet.exists()) {//是视频页面
                    //阅读奖励
                    UiObject uiAward = new UiObject(new UiSelector().resourceId("cn.youth.news:id/aeq"));//阅读奖励
                    UiObject uiPro = new UiObject(new UiSelector().resourceId("cn.youth.news:id/f_"));//peogress
                    if (uiAward.exists() && !uiPro.exists()) {
                        LogUtil.e("sdfsdf");
                        uiAward.click();
                        Thread.sleep(5000);
                    } else {
                        Random r = new Random();
                        int number = r.nextInt(100) + 1;
                        /*随机数 进行判断 点击心或者滑动到下一个视频*/
                        if (number <= 1) {//上一条
                            A00UtilTest.swipUp(uiDevice);
                        } else if (number <= 95) {//下一条
                            A00UtilTest.swipDown(uiDevice);
                            Thread.sleep(10000);//播放 时长
                        } else {//3点击心
                            if (uiHeart.exists()) uiHeart.click();
                        }
                    }

                } else {//处理异常情况  1.0 点击重播 2.0 广告滑动一下
                    UiObject uiAwardDialog = new UiObject(new UiSelector().resourceId("cn.youth.news:id/nz"));

                    if (uiAwardDialog.exists()) {//阅读奖励dialog
                        uiAwardDialog.click();
                    } else {//最终的强制搞一波
                        A00UtilTest.baseMethod(uiDevice, 1, appName);
                    }
                }
                Thread.sleep(500);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}