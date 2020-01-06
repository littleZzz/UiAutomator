package com.littlez.uiautomator.dialog;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.littlez.uiautomator.R;

import androidx.annotation.NonNull;


/**
 * created by xiaozhi
 * <p>全局loading的 dialog
 * Date 2019/1/7
 */
public class LoadingDialog extends Dialog {


    private static Context mContext;
    private static LoadingDialog loadingDialog = null;

    public static LoadingDialog getInstance(Context context) {

//        if (mContext == context) {
//            if (loadingDialog == null) {
////                synchronized (LoadingDialog.class) {
////                    if (loadingDialog == null) {
//                loadingDialog = new LoadingDialog(context, R.style.TransparentDialog02);
////                    }
////                }
//            }
//        } else {
//            synchronized (LoadingDialog.class) {
//                loadingDialog = new LoadingDialog(context, R.style.TransparentDialog02);
//            }
//        }
//
//        return loadingDialog;
        return new LoadingDialog(context, R.style.TransparentDialog02);
    }

    private LoadingDialog(@NonNull Context context) {
        super(context);
        mContext = context;
    }

    private LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);

        setCanceledOnTouchOutside(false);
        /*去掉4.4模拟器 dialog出现蓝色线*/
        int divierId = mContext.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = findViewById(divierId);
        if (divider != null) divider.setBackgroundColor(Color.TRANSPARENT);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();

    }


}
