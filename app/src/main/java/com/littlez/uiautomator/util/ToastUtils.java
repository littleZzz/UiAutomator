package com.littlez.uiautomator.util;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.littlez.uiautomator.base.MyApplication;

/**
 * created by xiaozhi
 * <p> ToastUtils
 * Date 2018/12/26
 */
public class ToastUtils {

    private static Toast toast;
    private static ImageView imgIcon;
    private static TextView tvContent;

    public static void show(String content) {
        Toast.makeText(MyApplication.appContext, content, Toast.LENGTH_SHORT).show();
    }

}
