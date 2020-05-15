package com.littlez.uiautomator;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import junit.framework.TestCase;

/**
 * created by xiaozhi
 * <p>微信的  测试用例   //当前脚本基于版本 7.0.14
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
    UiDevice uiDevice;

    //    @Test
    public void test() throws UiObjectNotFoundException {
        // 获取设备对象
        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        uiDevice = UiDevice.getInstance(instrumentation);

        A00UtilTest.baseMethod(uiDevice, 0, appName, null);//启动时  先关闭其他的
        A00UtilTest.errorCount = 0;//重置错误次数

        //声明id  可以写在外面
        UiObject uiIsAppMainPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/czl"));//微信底部tab relLayout的id
        UiObject uiToWeiXinPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/cns").text("微信"));//tab 文字id
        UiObject uiToContactsPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/cns").text("通讯录"));//tab 文字id
        UiObject uiToFaXianPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/cns").text("发现"));//tab 文字id
        UiObject uiToWoPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/cns").text("我"));//tab 文字id
        UiObject uiIsWeiXinPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/dg2"));//微信页面 listView id
        UiObject uiIsContactsPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/f4"));//通讯录页面 listView id
        UiObject uiIsFaXianPage = new UiObject(new UiSelector().resourceId("android:id/title").text("朋友圈"));//发现页面 朋友圈文字id
        UiObject uiIsWoPage = new UiObject(new UiSelector().resourceId("android:id/title").text("支付"));//我页面 支付文字id

        UiObject uiChatNumIndex = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/ga3"));//聊天数字角标
        UiObject uiChatDotIndex = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/tt"));//聊天点角标
        UiObject uiTabNumIndex = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/gik"));//tab数字角标

        UiObject uiContactsQunLiao = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/fx").text("群聊"));//文本 id
        UiObject uiContactsGongZongHao = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/a3c"));//文本上一层linearLayout id

        UiObject uiIsChatPage = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/anx"));//聊天页面 底部右边+号按钮linearlayout的id
        UiObject uiChatTitle = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/gas"));//聊天页面 聊天title文本的id
        UiObject uiChatSwitchBtn = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/anc"));//聊天页面 底部左边 切换按钮的id
        UiObject uiChatEditChatBtn = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/al_"));//聊天页面 编辑文本按钮的id
        UiObject uiChatPushChatBtn = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/grk"));//聊天页面 按钮说话按钮的id
        UiObject uiChatUnReadDot = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/ao1"));//聊天页面 语音未读红点id
        UiObject uiUnreadNews = uiChatUnReadDot.getFromParent(new UiSelector().resourceId("com.tencent.mm:id/aop"));//同层级的framlayout id

        UiObject uiWeiXinTuanDuiTitle = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/gas").text("微信团队"));//微信团队 title文本的id
        UiObject uiWeiXinZhiFuTitle = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/gas").text("微信支付"));//微信支付 title文本的id

        UiObject uiIsTencentNewsMainPage = new UiObject(new UiSelector().resourceId("android:id/text1").text("腾讯新闻"));//腾讯新闻主页 顶部文字匹配
        UiObject uiTencentNewsItem = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/b40"));//腾讯新闻  新闻 最里面的linearlayout 外层id
        UiObject uiIsDingYueNewsMainPage = new UiObject(new UiSelector().resourceId("android:id/text1").text("订阅号消息"));//订阅号消息主页 顶部文字匹配
        UiObject uiDingYueNewsItem = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/a29"));//订阅号消息主页 item的最外层内容linearlayout 外层id

        UiObject uiUpdateCancle = new UiObject(new UiSelector().resourceId("com.tencent.mm:id/dit"));//跟新取消  com.tencent.mm:id/dj6 安装


        try {
            while (appRun) {

                if (isRunFinish) {//完成运行任务了
                    Thread.sleep(15000);
                } else if (uiIsAppMainPage.exists()) {//在微信主页  随机去选择  微信页面做事  通讯录做事  发现做事  我做事
                    if (uiIsWeiXinPage.exists()) {//微信页面：消灭所有的红点聊天记录  下滑一下小程序  订阅号（查看公众号和订阅号文章）
                        if (uiChatNumIndex.exists()) {//有数字角标的聊天未读内容或者腾讯新闻
                            uiChatNumIndex.click();
                        } else if (uiChatDotIndex.exists()) {//有点角标未读内容 如订阅号消息
                            uiChatDotIndex.click();
                        } else {//微信页面完毕  随机跳转到通讯录页面或者是发现页面
                            if (A00UtilTest.getRandom(100) > 30) uiToFaXianPage.click();
                            else uiToContactsPage.click();
                        }
                    } else if (uiIsContactsPage.exists()) {//通讯录页面：公众号和群聊
                        // 通讯录界面  随机去点击一下 群聊、公众号、直接的某一个人(几率一定要低)
                        int random = A00UtilTest.getRandom(100);
                        if (random <= 10) {//10分之一的几率去点击
                            if (uiContactsQunLiao.exists()) uiContactsQunLiao.click();
                        } else if (random <= 20) {//10分之一的几率去点击
                            if (uiContactsGongZongHao.exists()) uiContactsGongZongHao.click();
                        } else {
                            if (uiToFaXianPage.exists()) uiToFaXianPage.click();
                        }
                    } else if (uiIsFaXianPage.exists()) {//发现页面：朋友圈、扫一扫、看一看、搜一搜、购物、游戏
                        //TODO 随机去点击 朋友圈、扫一扫、看一看、搜一搜、购物、游戏
                        uiIsFaXianPage.click();
                        Thread.sleep(1000);
                        randomSwip(3 + A00UtilTest.getRandom(5), false);
                        uiDevice.pressBack();
                        Thread.sleep(1000);
                        uiToWoPage.click();//去到  我的界面
                    } else if (uiIsWoPage.exists()) {//我的页面：头像、支付、收藏、相册、表情、设置
                        //TODO 随机去点击 头像、支付、收藏、相册、表情、设置
                        if (A00UtilTest.getRandom(100) <= 20) {
                            uiIsWoPage.click();
                            Thread.sleep(1000);
                            randomSwip(A00UtilTest.getRandom(2), false);
                            uiDevice.pressBack();
                        }
                        Thread.sleep(1000);
                        uiToWeiXinPage.click();
                        isRunFinish = true;
                    }
                } else if (uiIsChatPage.exists()) {//是聊天页面
                    //进行消息阅读  如果有语音未读消息语音消息  进行阅读
                    if (uiChatUnReadDot.exists()) {//有未读语音消息
                        readUnreadVioceMsg(uiChatUnReadDot, uiUnreadNews);
                    } else {//没有未读语音消息  简单滑动几次 假装阅读消息
                        randomSwip(A00UtilTest.getRandom(5), true);//上翻聊天记录
                    }
                    //如果是某个固定的群或者人  进行语音、文字、表情的聊天
                    if (uiChatTitle.exists() && isAppointChatMan(uiChatTitle)) {//指定的群 进行聊天操作 &9527
                        if (uiChatEditChatBtn.exists()) {//是编辑模式 切换到语音
                            uiChatSwitchBtn.click();
                            Thread.sleep(3000);
                        }
                        if (uiChatPushChatBtn.exists()) {//按下说话按钮
                            randomChat(3);//说话随机三次  每次最多十秒
                        }
                    }
                    uiDevice.pressBack();
                } else if (uiIsTencentNewsMainPage.exists()) {//腾讯新闻主页
                    randomSwip(A00UtilTest.getRandom(3), true);
                    uiTencentNewsItem.click();
                    Thread.sleep(2000);
                    randomSwip(3 + A00UtilTest.getRandom(4), false);
                    uiDevice.pressBack();
                    Thread.sleep(2000);
                    uiDevice.pressBack();
                } else if (uiIsDingYueNewsMainPage.exists()) {//订阅号（公众号）消息：查看阅读文章
                    uiDingYueNewsItem.click();
                    Thread.sleep(2000);
                    randomSwip(3 + A00UtilTest.getRandom(12), false);
                    uiDevice.pressBack();
                    Thread.sleep(2000);
                    uiDevice.pressBack();
                } else if (uiWeiXinTuanDuiTitle.exists() || uiWeiXinZhiFuTitle.exists()) {//微信团队 微信支付页面：
                    Thread.sleep(10000);
                    uiDevice.pressBack();
                }
                /*else if () {//朋友圈主页：滑动浏览朋友圈   点赞等  发布朋友圈
                } else if () {//群聊主页： //TODO 这个必须做
                } else if () {//公众号主页： //TODO 这个必须做
                } */
                else {
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

    //是否是指定的聊天人
    private boolean isAppointChatMan(UiObject uiChatTitle) {
        try {
            return uiChatTitle.getText().contains("A同行") || uiChatTitle.getText().contains("旺财")
                    || uiChatTitle.getText().contains("&9527");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //阅读微阅读的语音消息
    private void readUnreadVioceMsg(UiObject uiChatUnReadDot, UiObject uiUnreadNews) {
        try {
            while (true) {
                if (uiUnreadNews.exists()) {//本页还有未读内容
                    uiUnreadNews.click();
                    Thread.sleep(10000);
                } else {//本业没有未读内容
                    randomSwip(1, true);//上翻聊天记录
                    if (uiChatUnReadDot.exists()) {//如果有就开下下一个循环就行了
                    } else {//如果也没有就说明聊天内容读完了
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //随机向上滑动或者向下滑动的类
    public void randomSwip(int random, boolean upOrDown) {
        int count = 0;
        try {
            while (count < (random)) {
                if (upOrDown) A00UtilTest.swipUp(uiDevice, 25);
                else A00UtilTest.swipDown(uiDevice, 25);
                Thread.sleep(2000);
                count++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //随机进行发送聊天的消息（目前只支持语音消息） 时长10秒以内
    public void randomChat(int randomChatTimes) {
        int count = 0;
        try {
            while (count < (A00UtilTest.getRandom(randomChatTimes))) {
                uiDevice.swipe(530, 1840, 530, 1840, A00UtilTest.getRandom(10) * 50);//长按说话
                Thread.sleep(3000);
                count++;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}