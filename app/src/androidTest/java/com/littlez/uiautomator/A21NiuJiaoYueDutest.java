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
 * <p>牛角免费小说  测试用例
 * Date 2019/12/3
 */
public class A21NiuJiaoYueDutest extends TestCase {

    /*app 名字*/
    private String appName = "牛角免费小说";
    private boolean appRun = true;//appRun

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName, null);
        ;//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置次数
        long startTime = System.currentTimeMillis();//开始时间
        try {

            UiObject uiBookName = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/book_store_name"));//书名
            UiObject uiReadPage = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/read_pv_page_layout"));
            UiObject uiReadBook = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/btn_read_book"));//
            UiObject uiTabText = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/welfare_tab_text"));//日程任务和累计任务文本的id
            while (appRun) {

                if (uiBookName.exists()) {//书架页面 根据书名存在判断是书架页面
                    uiBookName.click();//点击进行阅读
                    A00UtilTest.backUntilObjOrTime(uiDevice, uiReadBook, 20);
                } else if (uiReadPage.exists()) {//是阅读界面
                    long currentTimeMillis = System.currentTimeMillis();
                    if (currentTimeMillis - startTime >= 30 * 60 * 1000) {//回到主页 获取奖励
                        uiDevice.pressBack();
                        Thread.sleep(2000);
                        A00UtilTest.backUntilObjOrTimeToBack(uiDevice, uiReadBook, 20);
                        Thread.sleep(2000);
                        UiObject uiGiift = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/tv_tab_title").text("福利"));
                        if (uiGiift.exists()) {
                            uiGiift.click();
                            startTime = currentTimeMillis;
                        }
                    } else {
                        A00UtilTest.swipleft(uiDevice);
                        Random r = new Random();
                        int number = r.nextInt(5) + 1;
                        Thread.sleep((15 + number) * 1000);
                    }
                } else if (uiTabText.exists()) {//福利页面
                    welare(uiDevice);
                } else {//处理异常情况
                    //com.yincheng.njread:id/app_up_progress升级按钮   com.yincheng.njread:id/app_up_close//升级取消
                    UiObject uiClose = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/home_rad_submit"));
                    UiObject uiShare = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/sign_sueecss_close"));
                    UiObject uiGide = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/guide_invite_close"));
                    UiObject uiUpdateCancle = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/app_up_close"));

                    if (uiClose.exists()) {
                        uiClose.click();
                    } else if (uiShare.exists()) {
                        uiShare.click();
                    } else if (uiGide.exists()) {
                        uiGide.click();
                    } else if (uiUpdateCancle.exists()) {
                        uiUpdateCancle.click();
                    } else {//最终的强制搞一波
                        A00UtilTest.baseMethod(uiDevice, 1, appName, new A00UtilTest.MyCallBack() {
                            @Override
                            public void callback(boolean isStop) {
                                appRun = false;//出问题了停止运行
                            }
                        });
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
        UiObject uiDailyTask =
                new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/welfare_tab_text").text("日常任务"));
        if (uiDailyTask.isSelected()) {
            int count = 0;
            while (count <= 3) {
                UiObject uiGet =
                        new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/item_task_submit").text("可领取"));
                UiObject uigetSure = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/home_rad_submit"));
                if (uigetSure.exists()) uigetSure.click();
                else if (uiGet.exists()) uiGet.click();
                else {
                    uiDevice.swipe(620, 1300, 700, 830, 15);
                    count++;
                }
                Thread.sleep(2000);
            }
            //跳转到累计任务
            UiObject uiAccumTask =
                    new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/welfare_tab_text").text("累计任务"));
            if (uiAccumTask.exists()) uiAccumTask.click();
            //累计任务循环
            while (true) {//累计阅读
                UiObject uiRecycler = new UiObject(
                        new UiSelector().resourceId("com.yincheng.njread:id/item_accumul_tast_recycler").instance(0));
                UiObject child = uiRecycler.getChild(new UiSelector().className("android.view.ViewGroup").enabled(true));
                UiObject uigetSure = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/home_rad_submit"));

                if (uigetSure.exists()) uigetSure.click();
                if (child.exists()) child.click();
                else break;
                Thread.sleep(2000);
            }
            while (true) {//已连续登录
                UiObject uiRecycler = new UiObject(
                        new UiSelector().resourceId("com.yincheng.njread:id/item_accumul_tast_recycler").instance(2));
                UiObject child = uiRecycler.getChild(new UiSelector().className("android.view.ViewGroup").enabled(true));
                UiObject uigetSure = new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/home_rad_submit"));
                if (uigetSure.exists()) uigetSure.click();
                if (child.exists()) child.click();
                else break;
                Thread.sleep(2000);
            }
            UiObject uiBooks =
                    new UiObject(new UiSelector().resourceId("com.yincheng.njread:id/tv_tab_title").text("书架"));
            uiBooks.click();
            Thread.sleep(1000);
        }
    }

}