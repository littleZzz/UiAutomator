package com.littlez.uiautomator.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;

import com.littlez.uiautomator.MainActivity;
import com.littlez.uiautomator.R;
import com.littlez.uiautomator.base.Constant;
import com.littlez.uiautomator.bean.VideosBean;
import com.littlez.uiautomator.bean.eventbus.EventbusBean;
import com.littlez.uiautomator.util.CommonUtil;
import com.littlez.uiautomator.util.LogUtil;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.greenrobot.eventbus.EventBus;

/**
 * created by xiaozhi
 * <p>进行后台运行uiautomator的  服务
 * Date 2019/12/16
 */
public class BackService extends Service {

    private Thread thread;
    private ArrayList<VideosBean> datas;
    private long startTime = 0L;//开始运行的时间
    private long notifyTime = 0L;//唤醒uiautomator的时间值标记
    private long gapTime = 30 * 60 * 1000;//间隔的时间 默认半小时

    boolean isReStartUiTask = true;//是否重启ui任务 目的是UI任务只重启一次 重启一次后就设置不在重启了
    boolean isStartToHomeUiTask = true;//是否可以启动ToHomeUi任务

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
        //后台弄一个无声播放器
        mMediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.silent);
        mMediaPlayer.setLooping(true);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("BackService  onStartCommand");
        datas = Constant.videosBeans;
        LogUtil.e("datas----->" + datas.toString());
        //初次启动UI任务 如果startTime=0 就是重新开始   否则就是启动暂停之前的任务
        CommonUtil.stopUiautomator();//停止当前任务
        if (startTime <= 0) {//初次启动
            startTime = System.currentTimeMillis();//重新设置开始时间
            String testClass = datas.get(Constant.startFlag % datas.size()).getTestClass();
            gapTime = datas.get(Constant.startFlag % datas.size()).getGapTime();
            CommonUtil.startUiautomator(testClass);//开始一个任务
            Constant.startFlag++;
        } else {//暂停后 的启动 启动之前的任务
            String testClass = datas.get((Constant.startFlag - 1) % datas.size()).getTestClass();
            CommonUtil.startUiautomator(testClass);//开始一个任务
        }
        if (thread == null || !thread.isAlive()) {//线程死掉了 就重启线程
            thread = new MyThread();
            thread.start();
            LogUtil.e("新建了线程  音乐是否播放中：" + mMediaPlayer.isPlaying());
        } else {//thread 没有死掉  就继续
            LogUtil.e("没有新建线程  音乐是否播放中：" + mMediaPlayer.isPlaying());
        }
        notifyTime = System.currentTimeMillis();//设置唤醒ui任务的时间值
        return START_STICKY/*super.onStartCommand(intent, flags, startId)*/;
    }

    //自建线程
    class MyThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                startPlayMusic();//播放音乐
                while (Constant.isrun) {
                    if (CommonUtil.isBelongPeriodTime("01:00", "07:00")) {//外挂去检测是否在某一时间段内
                        if (isStartToHomeUiTask) {//启动ToHome任务
                            CommonUtil.stopUiautomator();//停止当前任务
                            CommonUtil.startUiautomator("A001ToHometest");//开始一个任务
                            ArrayList<VideosBean> videosBeans = CommonUtil.shuffleList(datas);
                            datas = null;
                            datas = videosBeans;
                            Constant.startFlag = 0;//重置flag
                            LogUtil.e(datas.toString());
                            isStartToHomeUiTask = false;
                        }
                        Thread.sleep(60 * 1000);//一分钟检测一次
                        LogUtil.e("时间段内 01:00- 07:00 停止运行");
                        continue;
                    }
                    try {
                        long time = System.currentTimeMillis();
                        if (startTime <= 0 || time - startTime >= gapTime) {//已经到切换的时间了
                            CommonUtil.stopUiautomator();//停止当前任务
                            //获取到开始那一条任务 并运行
                            String testClass = datas.get(Constant.startFlag % datas.size()).getTestClass();
                            gapTime = datas.get(Constant.startFlag % datas.size()).getGapTime();
                            CommonUtil.startUiautomator(testClass);//开始一个任务
                            Constant.startFlag++;
                            startTime = System.currentTimeMillis();//重新设置开始时间
                            isReStartUiTask = true;//设置可以重启
                            isStartToHomeUiTask = true;//设置可以启动
                        }
                        LogUtil.e((Constant.startFlag - 1 + 1) % datas.size() + "/" + datas.size()
                                + datas.get((Constant.startFlag - 1) % datas.size()).getTestClass() +
                                ";gapTime=" + (gapTime / 1000 / 60) + "分钟;" + (time - startTime) / 1000 / 60);
                        Thread.sleep(5000);
                        //另外设一个时间值  判断当前运行uiautomator  是不是没有运行  是就去唤醒他
                        if (time - notifyTime > 15000) {//15秒一个间隔去检查是不是
                            boolean uiautomatorRuning = CommonUtil.isUiautomatorRuning();
                            if (!uiautomatorRuning) {//如果没有uiautomator任务运行
                                String testClass = datas.get((Constant.startFlag - 1) % datas.size()).getTestClass();
                                if (isReStartUiTask) {//重新启动UI任务
                                    CommonUtil.startUiautomator(testClass);//开始一个任务
                                    isReStartUiTask = false;
                                    EventbusBean eventbusBean = new EventbusBean();
                                    eventbusBean.setErrorStr(true);
                                    String parseTime = CommonUtil.parseTime(new Date(), 1);
                                    eventbusBean.setErrorStr(parseTime + " : " + testClass + ";");
                                    EventBus.getDefault().post(eventbusBean);
                                }
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                LogUtil.e(e.toString());
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        stopPlayMusic();//停止播放器服务

        LogUtil.e("BackService  onDestroy");
        if (!Constant.isCloseService) {// 重启自己
            Intent intent = new Intent(getApplicationContext(), BackService.class);
            startService(intent);
        }
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

    /**
     * 做一个 后台无声的播放音乐
     */
    private MediaPlayer mMediaPlayer;

    private void startPlayMusic() {
        if (mMediaPlayer != null) {
            LogUtil.e("启动后台播放音乐");
            if (!mMediaPlayer.isPlaying()) mMediaPlayer.start();
        }
    }

    private void stopPlayMusic() {
        if (mMediaPlayer != null) {
            LogUtil.e("关闭后台播放音乐");
            if (mMediaPlayer.isPlaying()) mMediaPlayer.stop();
        }
    }
}
