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
    private long gapTime = 30 * 60 * 1000;//间隔的时间
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

                            CommonUtil.stopUiautomator();//停止当前任务

                            //获取到开始那一条任务 并运行
                            String testClass = datas.get(startFlag % datas.size()).getTestClass();
                            CommonUtil.startUiautomator(testClass);//开始一个任务

                            startFlag++;
                            startTime = System.currentTimeMillis();//重新设置开始时间
                        }

                        LogUtil.e("运行中。。。" + datas.get((startFlag - 1) % datas.size()).getTestClass());
                        Thread.sleep(5000);

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
        gapTime = intent.getLongExtra("gapTime", gapTime);
        LogUtil.e("datas----->" + datas.toString() + ";;;gapTime===" + (gapTime / 1000 / 60) + "分");
        if (!thread.isAlive()) {
            thread.start();
            LogUtil.e("调用了 therad.start");
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        super.onDestroy();
        LogUtil.e("BackService  onDestroy");

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
