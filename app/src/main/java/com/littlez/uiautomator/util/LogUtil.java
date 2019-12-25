package com.littlez.uiautomator.util;

import android.util.Log;

import com.littlez.uiautomator.bean.eventbus.EventbusBean;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

/**
 * created by xiaozhi
 * <p>
 * Date 2019/12/3
 */
public class LogUtil {

    public static EventbusBean eventbusBean;

    public static void e(String str) {
        Log.e("999", str);
        if (eventbusBean == null) eventbusBean = new EventbusBean();
        eventbusBean.setLog(CommonUtil.parseTime(new Date(), 1) + " : " + str);
        EventBus.getDefault().post(eventbusBean);
    }
}
