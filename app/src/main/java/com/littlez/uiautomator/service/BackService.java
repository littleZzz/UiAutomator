package com.littlez.uiautomator.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.littlez.uiautomator.util.ExeCommand;
import com.littlez.uiautomator.util.LogUtil;

import androidx.annotation.Nullable;

/**
 * created by xiaozhi
 * <p>
 * Date 2019/12/16
 */
public class BackService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        LogUtil.e("BackService  onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.e("BackService  onStartCommand");

        //这里面已经默认开启了线程
        ExeCommand cmd = new ExeCommand(false)
                .run("uiautomator runtest AutoRunner.jar --nohup -c testpackage.Uitest", 60000);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LogUtil.e("BackService  onDestroy");

    }


}
