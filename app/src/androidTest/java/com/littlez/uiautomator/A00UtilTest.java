package com.littlez.uiautomator;

import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

public class A00UtilTest {

    public static int errorCount;

    /**
     * 基本的运行方法封装
     */
    public static void baseMethod(UiDevice uiDevice, int flag, String appName) {
        try {
            switch (flag) {
                case 0://CLEAR_APP
                    uiDevice.pressRecentApps();
                    Thread.sleep(800);
                    UiObject clearAll = new UiObject(
                            new UiSelector().resourceId("com.android.systemui:id/leui_recent_clear_all_btn_layout"));
                    if (clearAll.exists()) {
                        clearAll.click();
                        Thread.sleep(800);
                    }
                    uiDevice.pressHome();
                    Thread.sleep(200);
                    uiDevice.pressHome();
                    Thread.sleep(200);
                    break;
                case 1://Error_Base
                    if (errorCount > 6) {//这个强制方法走了10次  出现什么异常问题了 直接关闭应用  重新启动
                        uiDevice.pressHome();
                        Thread.sleep(500);
                        uiDevice.pressRecentApps();
                        Thread.sleep(500);
                        UiObject appClearAll = new UiObject(
                                new UiSelector().resourceId("com.android.systemui:id/leui_recent_clear_all_btn_layout"));
                        if (appClearAll.exists()) {
                            appClearAll.click();
                            errorCount = 0;//重置失败次数
                            Thread.sleep(500);
                        }
                        if (errorCount >= 8) {//重启两次都不行  关闭循环结束任务
                            break;
                        }
                    }

                    UiObject uiCrash = new UiObject(new UiSelector().resourceId("android:id/le_bottomsheet_default_confirm"));
                    if (uiCrash.exists()) {
                        uiCrash.click();//应用奔溃了 的页面
                    }

                    uiDevice.pressHome();
                    Thread.sleep(500);

                    //启动应用
                    UiObject uiVideo = new UiObject(new UiSelector().text(appName));
                    if (uiVideo.exists()) {
                        uiVideo.click();
                        Thread.sleep(10000);
                    }

                    errorCount++;//增加异常启动次数
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 上一条
     *
     * @param uiDevice
     */
    public static void swipUp(UiDevice uiDevice) {
        uiDevice.swipe(840, 460, 680, 1300, 6);
    }

    public static void swipUp(UiDevice uiDevice, int steps) {
        uiDevice.swipe(840, 460, 680, 1300, steps);
    }

    /**
     * 下一条
     *
     * @param uiDevice
     */
    public static void swipDown(UiDevice uiDevice) {
        uiDevice.swipe(460, 1300, 780, 480, 6);
    }

    public static void swipDown(UiDevice uiDevice, int steps) {
        uiDevice.swipe(460, 1300, 780, 480, steps);
    }

    /**
     * 左滑动（右边数据）
     *
     * @param uiDevice
     */
    public static void swipleft(UiDevice uiDevice) {
        uiDevice.swipe(960, 700, 300, 710, 10);
    }

    public static void swipleft(UiDevice uiDevice, int steps) {
        uiDevice.swipe(960, 700, 300, 710, steps);
    }

    /**
     * 打开应用
     *
     * @param uiDevice
     * @param appName
     */
    public static void openApp(UiDevice uiDevice, String appName) {
        //启动应用
        UiObject uiVideo = new UiObject(new UiSelector().text(appName));
        if (uiVideo.exists()) {
            try {
                uiVideo.click();
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param uiDevice
     * @param uiExitToClose 可以为空  为空就是不判断 等待固定时间
     * @param time          时间 时间值是int
     */
    public static void backUntilObjOrTime(UiDevice uiDevice, UiObject uiExitToClose, int time) {
        try {
            boolean isRun = true;
            long startTime = System.currentTimeMillis();//开始时间
            while (isRun) {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - startTime >= time * 1000) {//超过某个时间
                    uiDevice.pressBack();
                    isRun = false;
                    break;
                } else if (uiExitToClose != null && uiExitToClose.exists()) {
                    uiExitToClose.click();
                    isRun = false;
                    break;
                }
                Thread.sleep(5000);
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param uiDevice
     * @param otherClick    其他的一个跳出循环判断  比如福袋临朐  阅读奖励等
     * @param uiExitToClose 可以为空  为空就是不判断 等待固定时间
     * @param time          时间 时间值是int
     */
    public static void backUntilObjOrTime(UiDevice uiDevice, UiObject otherClick, UiObject uiExitToClose, int time) {
        try {
            boolean isRun = true;
            long startTime = System.currentTimeMillis();//开始时间
            while (isRun) {
                long currentTimeMillis = System.currentTimeMillis();
                if (currentTimeMillis - startTime >= time * 1000) {//超过某个时间
                    uiDevice.pressBack();
                    isRun = false;
                    break;
                } else if (otherClick != null && otherClick.exists()) {
                    otherClick.click();
                    isRun = false;
                    break;
                } else if (uiExitToClose != null && uiExitToClose.exists()) {
                    uiExitToClose.click();
                    isRun = false;
                    break;
                }
                Thread.sleep(5000);
            }
        } catch (UiObjectNotFoundException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
