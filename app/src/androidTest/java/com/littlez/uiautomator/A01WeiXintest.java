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
 * <p>微信的  测试用例  //每天启动2-3次   2-3个卷群啊这些活跃群  2-3个订阅号查看新闻或者视频  可以看朋友圈不一定发朋友圈
 * Date 2019/12/3
 * 第一天 添加2个好友  1个微信群  1个订阅号
 * 第二天 添加2个好友  1个微信群  1个订阅号
 * 第三天 添加2个好友  1个微信群  1个订阅号
 * 第四天 添加2个好友  1个微信群  1个订阅号
 * 第五天 添加2个好友  1个微信群  1个订阅号
 * 第六天 添加2个好友  1个微信群  1个订阅号
 * 第七天 添加2个好友  1个微信群  1个订阅号
 * 日常操作  微信、通讯录、发现、我红点消除；聊天记录查看、假装聊天一手（文字、图片、语音）、
 */
public class A01WeiXintest extends TestCase {

    /*app 名字*/
    private String appName = "微信";
    private boolean appRun = true;//appRun
    private boolean isRunFinish = false;//是否运行完一轮了

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        UiDevice uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置错误次数

        //声明id  可以写在外面
        UiObject uiAppMainPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/cwx"));//微信底部tab的id
        UiObject uiWeiXinPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/cl7").text("微信"));//tab 文字id
        UiObject uiContactsPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/cl7").text("通讯录"));//tab 文字id
        UiObject uiFaXianPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/cl7").text("发现"));//tab 文字id
        UiObject uiWoPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/cl7").text("我"));//tab 文字id
        UiObject uiIsWeiXinPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/dd6"));//微信页面 listView id
        UiObject uiIsContactsPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/f4"));//通讯录页面 listView id
        UiObject uiIsFaXianPage = new UiObject(new UiSelector().resourceId("android:id/title").text("朋友圈"));//发现页面 朋友圈文字id
        UiObject uiIsWoPage = new UiObject(new UiSelector().resourceId("android:id/title").text("支付"));//我页面 支付文字id

        UiObject uiIsChatPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/amt"));//聊天页面 底部右边+号按钮linearlayout的id
        UiObject uiChatNumIndex = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/g6k"));//聊天数字角标
        UiObject uiChatDotIndex = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/tn"));//聊天点角标

        UiObject uiIsTencentNewsMainPage = new UiObject(new UiSelector().resourceId("android:id/text1").text("腾讯新闻"));//腾讯新闻主页 顶部文字匹配
        UiObject uiTencentNewsItem = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/enl"));//腾讯新闻  新闻 linearlayout 外层id
        UiObject uiIsDingYueNewsMainPage = new UiObject(new UiSelector().resourceId("android:id/text1").text("订阅号消息"));//订阅号消息主页 顶部文字匹配
        UiObject uiDingYueNewsItem = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/a23"));//订阅号消息主页 item  内容linearlayout 外层id

        UiObject uiTabNumIndex = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/gdy"));//tab数字角标

        try {
            while (appRun) {

                if (isRunFinish) {//完成运行任务了
                    Thread.sleep(15000);
                } else if (uiAppMainPage.exists()) {//在微信主页  随机去选择  微信页面做事  通讯录做事  发现做事  我做事

                    if (uiIsWeiXinPage.exists()) {//微信页面：消灭所有的红点聊天记录  下滑一下小程序  订阅号（查看公众号和订阅号文章）
                        if (uiChatNumIndex.exists()) {//有数字角标的聊天未读内容或者腾讯新闻
                            uiChatNumIndex.click();
                        } else if (uiChatDotIndex.exists()) {//有点角标未读内容 如订阅号消息
                            uiChatDotIndex.click();
                        } else uiContactsPage.click();//微信页面的内容弄完了  去弄通讯录的内容

                    } else if (uiIsContactsPage.exists()) {//通讯录页面：公众号和群聊
                        uiFaXianPage.click();
                    } else if (uiIsFaXianPage.exists()) {//发现页面：朋友圈  游戏  小程序
                        uiIsFaXianPage.click();
                        Thread.sleep(1000);
                        int count = 0;
                        while (count < 3 + A00UtilTest.getRandom(5)) {
                            A00UtilTest.swipDown(uiDevice, 25);
                            Thread.sleep(2000);
                            count++;
                        }
                        uiDevice.pressBack();
                        Thread.sleep(1000);
                        uiWoPage.click();//去到  我的界面
                    } else if (uiIsWoPage.exists()) {//我的页面：个人页面  支付  收藏
                        uiIsWoPage.click();
                        Thread.sleep(1000);
                        int count = 0;
                        while (count < A00UtilTest.getRandom(2)) {
                            A00UtilTest.swipDown(uiDevice, 25);
                            Thread.sleep(2000);
                            count++;
                        }
                        uiDevice.pressBack();
                        Thread.sleep(1000);
                        uiWeiXinPage.click();
                        isRunFinish = true;
                    }

                } else if (uiIsChatPage.exists()) {//是聊天页面
                    int count = 0;
                    while (count < (2 + A00UtilTest.getRandom(4))) {
                        A00UtilTest.swipUp(uiDevice, 25);
                        Thread.sleep(1000);
                        count++;
                    }
                    uiDevice.pressBack();
                } else if (uiIsTencentNewsMainPage.exists()) {//腾讯新闻主页
                    int count = 0;
                    while (count < A00UtilTest.getRandom(1)) {
                        A00UtilTest.swipUp(uiDevice, 25);
                        Thread.sleep(2000);
                        count++;
                    }
                    uiTencentNewsItem.click();
                    count = 0;
                    while (count < 3 + A00UtilTest.getRandom(4)) {
                        A00UtilTest.swipDown(uiDevice, 25);
                        Thread.sleep(10000);
                        count++;
                    }
                    uiDevice.pressBack();
                    Thread.sleep(1000);
                    uiDevice.pressBack();
                } else if (uiIsDingYueNewsMainPage.exists()) {//订阅号（公众号）消息：查看阅读文章
                    uiDingYueNewsItem.click();
                    Thread.sleep(1000);
                    int count = 0;
                    while (count < 3 + A00UtilTest.getRandom(12)) {
                        A00UtilTest.swipDown(uiDevice, 25);
                        Thread.sleep(2000);
                        count++;
                    }
                    uiDevice.pressBack();
                    Thread.sleep(1000);
                    uiDevice.pressBack();
                } /*else if () {//朋友圈主页：滑动浏览朋友圈   点赞等  发布朋友圈

                }*/ else {
                    UiObject uiUpdateCancle = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/dit"));//跟新取消  com.tencent.mm:id/dj6 安装

                    if (uiUpdateCancle.exists()) {//跟新取消
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
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}