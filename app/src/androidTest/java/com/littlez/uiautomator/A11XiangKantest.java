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
 * <p>想看  测试用例
 * Date 2019/12/3
 */
public class A11XiangKantest extends TestCase {

    /*app 名字*/
    private String appName = "想看";
    private boolean appRun = true;//appRun
    private boolean isToEarn = true;//是否去执行签到人物

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName,null);;//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置次数
        boolean isToEarn = true;
        try {
            UiObject uiMainPage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/bottom_tab_layout"));//底部tab栏的id
            UiObject uiIsHomePage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/iv_home_search"));//搜索按钮id
            UiObject uiIsTVPage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/iv_video_search"));//搜索按钮id
            UiObject uiIsEarnPage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/title").text("任务中心"));//文本id
            UiObject uiIsMePage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/inviteCodeTv"));//邀请文本的id
            UiObject uiToHomePage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_tab_title").text("首页"));//文本id
            UiObject uiToTVPage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_tab_title").text("视频"));//文本id
            UiObject uiToEarnPage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tab_title_text").text("赚金币"));//文本id
            UiObject uiToMePage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_tab_title").text("我的"));//文本id
            UiObject uiHomePageItem = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tvTitle"));//标题文本id

            UiObject uiIsTVPlayPage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/vo_video_detail_source"));//作者名字
            UiObject uiFuDai = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/fudai_icon"));//福袋
            UiObject uiIsNewsPage = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/img_thumbUp"));//点赞按钮id
            UiObject uiNewsOver = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_title").text("评论"));//评论文字id

            UiObject uiDiaClose = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/iv_close"));//福袋等 dia 关闭按钮
            UiObject uiAdvClose = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tt_video_ad_close_layout"));//广告的关闭按钮


            while (appRun) {
                //主页
                if (uiMainPage.exists()) {//是主页  滑动选择条目
                    if (uiIsHomePage.exists()) {//是首页
                        if (isToEarn) {//跳转到tv这些执行签到任务
                            if (uiToTVPage.exists()) uiToTVPage.click();
                            isToEarn = false;
                            continue;
                        } else {
                            A00UtilTest.swipDown(uiDevice, 25);//下滑一下
                            if (uiHomePageItem.exists()) uiHomePageItem.click();
                        }
                    } else if (uiIsTVPage.exists()) {//是视频页面
                        if (uiToEarnPage.exists()) uiToEarnPage.click();
                    } else if (uiIsEarnPage.exists()) {//是赚金币页面
                        if (uiToMePage.exists()) uiToMePage.click();
                    } else if (uiIsMePage.exists()) {//是我的页面
                        if (uiToHomePage.exists()) uiToHomePage.click();
                    }
                    Thread.sleep(3000);
                } else if (uiIsTVPlayPage.exists()) {//是视频
                    boolean isRun = true;
                    long startTime = System.currentTimeMillis();//开始时间
                    while (isRun) {
                        long currentTimeMillis = System.currentTimeMillis();
                        if (uiFuDai.exists()) {//福袋存在  点击 然后退出当前循环
                            uiFuDai.click();
                            A00UtilTest.backUntilObjOrTime(uiDevice, uiDiaClose, 20);
                        } else if (currentTimeMillis - startTime >= (2.5 + A00UtilTest.getRandom(2)) * 60 * 1000) {//超过5分钟自动退出
                            isRun = false;
                            uiDevice.pressBack();
                        }
                        Thread.sleep(5000);
                    }

                } else if (uiIsNewsPage.exists()) {//是新闻
                    boolean isRun = true;
                    while (isRun) {
                        uiDevice.swipe(400, 1200, 534, 602, 20);
                        Thread.sleep(3000);
                        if (uiNewsOver.exists()) {
                           isRun=false;
                           uiDevice.pressBack();
                        }
                    }
                } else {
                    //com.xiangkan.android:id/updateSingleTv  升级按钮   com.xiangkan.android:id/closeIv  升级关闭
                    //处理异常情况 首页领取奖励后的dialog
                    UiObject uiSingDia = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/tv_sign_btn"));//签到弹框看视频在领取
                    UiObject uiCancleUpdate = new UiObject(new UiSelector().resourceId("com.xiangkan.android:id/closeIv"));//取消跟新

                    if (uiSingDia.exists()) {//签到弹框 看视频再领取
                        uiSingDia.click();
                        A00UtilTest.backUntilObjOrTime(uiDevice, uiAdvClose, 50);
                    } else if (uiDiaClose.exists()) {//福袋的关闭  等
                        uiDiaClose.click();
                    } else if (uiCancleUpdate.exists()) {//取消跟新
                        uiCancleUpdate.click();
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

}