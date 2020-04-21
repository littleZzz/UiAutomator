package com.littlez.uiautomator.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;

import com.littlez.uiautomator.bean.VideosBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

/**
 * created by xiaozhi
 * <p>常用的方法都放在这里面的
 * Date 2019/12/24
 */
public class CommonUtil {

    private static final String TAG = "CommonUtil";

    /**
     * 开始uiautomator的调用
     *
     * @param className
     */
    public static void startUiautomator(String className) {

        //这里面已经默认开启了线程
        ExeCommand cmd = new ExeCommand(false);
        cmd.run("uiautomator runtest AutoRunner.jar --nohup -c testpackage."
                + className, 30000);
        LogUtil.e("run uiautomator---" + className);
    }

    /**
     * 停止uiautomator的调用
     */
    public static void stopUiautomator() {
        while (isUiautomatorRuning()) {//循环保证一定停止当前任务
            try {
                //通过文件来停止 和启动 uiautomator
                ExeCommand run = new ExeCommand(true)
                        .run("ps | grep uiautomator", 30000);
                if (!TextUtils.isEmpty(run.getResult())) {//不是空
                    String result = run.getResult();
                    if (result.contains("root")) {//有
                        int root = result.indexOf("root");
                        String substring = result.substring(root + "root".length());
                        String[] s = substring.split(" ");
                        for (int i = 0; i < s.length; i++) {
                            if (!TextUtils.isEmpty(s[i])) {
                                ExeCommand run1 = new ExeCommand(true)
                                        .run("su -c kill " + s[i], 30000);
                                LogUtil.e("stop uiautomator---" + run1.getResult());
                                break;
                            }
                        }
                    }
                }
                LogUtil.e("result---" + run.getResult());
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断是不是有uiautomator在运行
     *
     * @return
     */
    public static boolean isUiautomatorRuning() {

        boolean back = false;
        //通过文件来停止 和启动 uiautomator
        ExeCommand run = new ExeCommand(true)
                .run("ps | grep uiautomator", 30000);
        if (!TextUtils.isEmpty(run.getResult())) {//不是空
            String result = run.getResult();
            if (result.contains("root")) {//有
                back = true;
            }
        }
        LogUtil.e("isUiautomatorRuning---" + back);
        return back;
    }


    /**
     * 根据Date解析日期
     *
     * @param date
     * @return
     */
    public static String parseTime(Date date, int patternFlag) {

        String pattern = "";
        if (patternFlag == 1) {
            pattern = "MM-dd HH:mm:ss";
        } else if (patternFlag == 2) {
            pattern = "yyyy年MM月dd日 HH时mm分";
        } else {
            pattern = "yyyy-MM-dd HH:mm:ss";
        }

        SimpleDateFormat dspFmt = new SimpleDateFormat(pattern);
        String format = dspFmt.format(date);

        return format;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }

    /*
     *  判断当前时间是否在设置的dark mode时间段内
     *  @param date1: 开始时间（hh:mm）
     *  @param date2: 结束时间（hh:mm）
     */
    public static boolean isBelongPeriodTime(String date1, String date2){
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Date currentTime = new Date(System.currentTimeMillis());
        Date startTimeDate;
        Date endTimeDate;
        Calendar date = Calendar.getInstance();
        Calendar begin = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try{
            date.setTime(df.parse(df.format(currentTime)));
            startTimeDate = df.parse(date1);
            endTimeDate = df.parse(date2);
            begin.setTime(startTimeDate);
            end.setTime(endTimeDate);
            if (endTimeDate.getHours() < startTimeDate.getHours()) {
                if (date.after(begin) || date.before(end)) {
                    Log.d(TAG, "current time is belong to " + date1 + " - " + date2);
                    return true;
                }else {
                    Log.d(TAG, "current time isn't belong to " + date1 + " - " + date2);
                    return false;
                }
            }else if(endTimeDate.getHours() == startTimeDate.getHours()){
                if (endTimeDate.getMinutes() < startTimeDate.getMinutes()) {
                    if (date.after(begin) || date.before(end)) {
                        Log.d(TAG, "current time is belong to " + date1 + " - " + date2);
                        return true;
                    }else {
                        Log.d(TAG, "current time isn't belong to " + date1 + " - " + date2);
                        return false;
                    }
                }
            }
        }catch(ParseException e){
            e.printStackTrace();
        }
        //这里是时间段的起止都在同一天的情况，只需要判断当前时间是否在这个时间段内即可
        if (date.after(begin) && date.before(end)) {
            Log.d(TAG, "current time is belong to " + date1 + " - " + date2);
            return true;
        }else {
            Log.d(TAG, "current time isn't belong to " + date1 + " - " + date2);
            return false;
        }
    }

    public static ArrayList<VideosBean> shuffleList(ArrayList<VideosBean> videosBeans) {
        //打乱list运行的顺序
        if (videosBeans.get(videosBeans.size() - 1).getTestClass().equals("A001ToHometest")) {
            VideosBean videosBean = videosBeans.get(videosBeans.size() - 1);
            videosBeans.remove(videosBeans.size() - 1);
            Collections.shuffle(videosBeans);//打乱list运行的顺序
            videosBeans.add(videosBean);
        } else {
            Collections.shuffle(videosBeans);
        }
        return videosBeans;
    }

}
