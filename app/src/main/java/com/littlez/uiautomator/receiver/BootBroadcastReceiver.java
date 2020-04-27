package com.littlez.uiautomator.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.littlez.uiautomator.MainActivity;
import com.littlez.uiautomator.util.LogUtil;

/**
 * created by xiaozhi
 * <p> 监听开机广播
 * Date 2020/4/27
 */
public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "BootBroadcastReceiver";
    private static final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_BOOT)) { //开机启动完成后，要做的事情
            Intent bootActivityIntent=new Intent(context, MainActivity.class);
            bootActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(bootActivityIntent);//要启动应用程序的首界面
            LogUtil.e("这个是开机监听");
        }
    }

}
