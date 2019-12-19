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
import com.littlez.uiautomator.util.LogUtil;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

/**
 * created by xiaozhi
 * <p>
 * Date 2019/12/16
 */
public class BackService extends Service {

    private Thread thread;

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
                while (true) {
                    try {
                        LogUtil.e("我运行着呢 不要费");
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

        if (!thread.isAlive()) thread.start();


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
                    .setContentTitle("下拉列表中的Title") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("要显示的内容") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

            Notification notification = builder.build(); // 获取构建好的Notification
            notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
            startForeground(1, notification);// 开始前台服务
        }

    }

}
