package com.littlez.uiautomator.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;

/**
 * created by xiaozhi
 * <p>
 * Date 2019/12/18
 */
public class MyApplication extends Application {

    
    public static Context appContext;
    public static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();

        appContext = getApplicationContext();
        app = this;
    }
}
