package com.littlez.uiautomator.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.littlez.uiautomator.MainActivity;
import com.littlez.uiautomator.R;
import com.littlez.uiautomator.bean.VideosBean;
import com.littlez.uiautomator.util.CommonUtil;
import com.littlez.uiautomator.util.LogUtil;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * created by xiaozhi
 * <p>进行后台运行uiautomator的  服务
 * Date 2019/12/16
 */
public class BackService extends Service {

    private Thread thread;

    private boolean isrun = true;
    private ArrayList<VideosBean> datas;
    private long startTime = 0L;//开始运行的时间
    private long notifyTime = 0L;//唤醒uiautomator的时间值标记
    private long gapTime = 30 * 60 * 1000;//间隔的时间 默认半小时
    private int startFlag = 0;//运行到第几条的flag PS：是一直自增的 所以用的时候进行%运算

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.e("BackService  onCreate");
        initNotifyication();//初始化前台通知 创建前台服务

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isrun) {

                    try {


                        long time = System.currentTimeMillis();
                        if (startTime <= 0 || time - startTime >= gapTime) {//已经到切换的时间了


                            while (CommonUtil.isUiautomatorRuning()) {//循环保证一定停止当前任务
                                CommonUtil.stopUiautomator();
                                Thread.sleep(500);
                            }

                            //获取到开始那一条任务 并运行
                            String testClass = datas.get(startFlag % datas.size()).getTestClass();
                            gapTime = datas.get(startFlag % datas.size()).getGapTime();
                            CommonUtil.startUiautomator(testClass);//开始一个任务

                            startFlag++;
                            startTime = System.currentTimeMillis();//重新设置开始时间
                        }

                        LogUtil.e("运行中.." + datas.get((startFlag - 1) % datas.size()).getTestClass() +
                                ";gapTime=" + (gapTime / 1000 / 60) + "分钟;" + (time - startTime) / 1000 / 60);
                        Thread.sleep(5000);

                        //另外设一个时间值  判断当前运行uiautomator  是不是没有运行  是就去唤醒他
                        if (time - notifyTime > 15000) {//15秒一个间隔去检查是不是
                            boolean uiautomatorRuning = CommonUtil.isUiautomatorRuning();
                            if (!uiautomatorRuning) {//如果没有uiautomator任务运行
                                //重新启动UI任务
                                String testClass = datas.get((startFlag - 1) % datas.size()).getTestClass();
                                if (!"test".endsWith(testClass))//排除一下空数据
                                    CommonUtil.startUiautomator(testClass);//开始一个任务
                            }
                        }

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("BackService  onStartCommand");
        datas = intent.getParcelableArrayListExtra("datas");
        LogUtil.e("datas----->" + datas.toString());
        if (!thread.isAlive()) {
            thread.start();
            LogUtil.e("调用了 therad.start");
        } else {//否则就重置现在最新的数据
            //重置标记  重新开始任务
            startTime = 0;
            startFlag = 0;
        }
        notifyTime = System.currentTimeMillis();//设置唤醒ui任务的时间值
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        super.onDestroy();
        LogUtil.e("BackService  onDestroy");

        isrun = false;

    }


    /*初始化通知*/
    private void initNotifyication() {
        String CHANNEL_ID = "ahhhhh";
        String CHANNEL_NAME = "xiiii";
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
            Intent intent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID).
                    setContentTitle("UIautomator").
                    setContentText("来吧 我看你能运行多久").
                    setWhen(System.currentTimeMillis()).
                    setSmallIcon(R.mipmap.ic_launcher).
                    setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)).
                    setContentIntent(pendingIntent).build();
            startForeground(1, notification);
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(this.getApplicationContext());
            Intent nfIntent = new Intent(this, MainActivity.class);
            builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                            R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
                    .setContentTitle("UIautomator") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("来吧 我看你能运行多久") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

            Notification notification = builder.build(); // 获取构建好的Notification
            notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
            startForeground(1, notification);// 开始前台服务
        }

    }

}
