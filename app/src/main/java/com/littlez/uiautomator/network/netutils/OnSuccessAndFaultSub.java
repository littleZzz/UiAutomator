package com.littlez.uiautomator.network.netutils;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.littlez.uiautomator.base.MyApplication;
import com.littlez.uiautomator.dialog.LoadingDialog;
import com.littlez.uiautomator.util.ToastUtils;

import org.json.JSONObject;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.SSLHandshakeException;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;
import retrofit2.HttpException;

/**
 * Created by littleZ on 2018/3/27.
 * 用于在Http请求开始时，自动显示一个ProgressDialog
 * 在Http请求结束是，关闭ProgressDialog
 * 调用者自己对请求数据进行处理   成功时 通过result是否等于1分别回调onSuccess和onFault，默认处理了401错误转登录。
 * 回调结果为String，需要手动序列化
 */

public class OnSuccessAndFaultSub extends DisposableObserver<ResponseBody>
        implements ProgressCancelListener {

    private static final String TAG = "OnSuccessAndFaultSub";
    /**
     * 是否需要显示默认Loading
     */
    private boolean showProgress = false;
    private OnSuccessAndFaultListener mOnSuccessAndFaultListener;

    public Context mContext;
    private LoadingDialog loadingDialog;


    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        mContext = MyApplication.getConText();
    }


    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, Context context) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.mContext = context;
        loadingDialog = LoadingDialog.getInstance(context);
    }


    /**
     * @param mOnSuccessAndFaultListener 成功回调监听
     * @param context                    上下文
     * @param showProgress               是否需要显示默认Loading
     */
    public OnSuccessAndFaultSub(OnSuccessAndFaultListener mOnSuccessAndFaultListener, Context context, boolean showProgress) {
        this.mOnSuccessAndFaultListener = mOnSuccessAndFaultListener;
        this.mContext = context;
        loadingDialog = LoadingDialog.getInstance(context);
        this.showProgress = showProgress;
    }


    private void showProgressDialog() {
        if (showProgress && null != loadingDialog && mContext != null) {
            try {
                loadingDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void dismissProgressDialog() {
        if (showProgress && null != loadingDialog && mContext != null) {
            try {
                loadingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 订阅开始时调用
     * 显示ProgressDialog
     */
    @Override
    public void onStart() {

        showProgressDialog();

    }


    /**
     * 完成，隐藏ProgressDialog
     */
    @Override
    public void onComplete() {
        dismissProgressDialog();
//        loadingDialog = null;
        dispose();
    }


    /**
     * 对错误进行统一处理
     * 隐藏ProgressDialog
     */
    @Override
    public void onError(Throwable e) {
        try {

            if (e instanceof SocketTimeoutException) {//请求超时
                ToastUtils.show("网络超时");
                mOnSuccessAndFaultListener.onFault("网络超时");
            } else if (e instanceof ConnectException) {//网络连接超时
                ToastUtils.show("网络连接超时");
                mOnSuccessAndFaultListener.onFault("网络连接超时");
            } else if (e instanceof SSLHandshakeException) {//安全证书异常
                ToastUtils.show("安全证书异常");
                mOnSuccessAndFaultListener.onFault("安全证书异常");
            } else if (e instanceof HttpException) {//请求的地址不存在
                int code = ((HttpException) e).code();
                if (code == 504) {
                    ToastUtils.show("网络异常，请检查您的网络状态");
                    mOnSuccessAndFaultListener.onFault("网络异常，请检查您的网络状态");
                } else if (code == 404) {
                    ToastUtils.show("请求的地址不存在");
                    mOnSuccessAndFaultListener.onFault("请求的地址不存在");
                } else {
                    ToastUtils.show("请求失败");
                    mOnSuccessAndFaultListener.onFault("请求失败");
                }
            } else if (e instanceof UnknownHostException) {//域名解析失败
                ToastUtils.show("网络异常，请检查您的网络状态");
                mOnSuccessAndFaultListener.onFault("网络异常，请检查您的网络状态");
            } else {
                ToastUtils.show("error:" + e.getMessage());
                mOnSuccessAndFaultListener.onFault("error:" + e.getMessage());
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(TAG, "catch--->:" + e2.getMessage());
        } finally {
            Log.e(TAG, "finally--->error:" + e.getMessage());
            //mOnSuccessAndFaultListener.onFault("error:" + e.getMessage());
            dismissProgressDialog();
//            loadingDialog = null;

        }

    }


    /**
     * 当result等于1回调给调用者，否则自动显示错误信息，若错误信息为401跳转登录页面。
     * ResponseBody  body = response.body();//获取响应体
     * InputStream inputStream = body.byteStream();//获取输入流
     * byte[] bytes = body.bytes();//获取字节数组
     * String str = body.string();//获取字符串数据
     */
    @Override
    public void onNext(ResponseBody body) {
        try {
            String result = body.string();
            JSONObject jsonObject = new JSONObject(result);
            int code = jsonObject.getInt("code");

            if (code == 0) {// 0是成功
                Log.e("请求成功的body", result);

                mOnSuccessAndFaultListener.onSuccess(result);
            } else if (code == 1000) {//Internal Error
                String message = jsonObject.getString("message");
                Log.e(TAG, "请求失败message: " + message);
                mOnSuccessAndFaultListener.onFault(message);
                ToastUtils.show( message);
            } else if (code == 1001) {//Params Error
                String message = jsonObject.getString("message");
                Log.e(TAG, "请求失败message: " + message);
                mOnSuccessAndFaultListener.onFault(message);
                ToastUtils.show( message);
            } else if (code == 1002) {//Instance Not Exists
                String message = jsonObject.getString("message");
                Log.e(TAG, "请求失败message: " + message);
                mOnSuccessAndFaultListener.onFault(message);
                ToastUtils.show( message);
            } else if (code == 1003) {//权限不够
                String message = jsonObject.getString("message");
                Log.e(TAG, "请求失败message: " + message);
                mOnSuccessAndFaultListener.onFault(message);
                ToastUtils.show( message);
            } else if (code == 1004) {//存在关联数据
                String message = jsonObject.getString("message");
                Log.e(TAG, "请求失败message: " + message);
                mOnSuccessAndFaultListener.onFault(message);
                ToastUtils.show( message);
            } else if (code == 1005) {//存在关联数据
                String message = jsonObject.getString("message");
                Log.e(TAG, "请求失败message: " + message);
                mOnSuccessAndFaultListener.onFault(message);
                ToastUtils.show( message);
            } else if (code == 1010) {//缺少token
                String message = jsonObject.getString("message");
                Log.e(TAG, "请求失败message: " + message);
                mOnSuccessAndFaultListener.onFault(message);
                ToastUtils.show( "您还未登录,请登录");
            } else if (code == 1011) {//token错误
                String message = jsonObject.getString("message");
                Log.e(TAG, "请求失败message: " + message);
                mOnSuccessAndFaultListener.onFault(message);
                ToastUtils.show("登录错误");
            } else if (code == 1012) {//token过期
                String message = jsonObject.getString("message");
                Log.e(TAG, "请求失败message: " + message);
                mOnSuccessAndFaultListener.onFault(message);
            } else if (code == 2001) {//表示账号注册但未激活
                //跳转到未激活界面
            } else if (code >= 3000 || code == 2004) {//自定义的错误
                String message = jsonObject.getString("message");
                Log.e(TAG, "请求失败message: " + message);
                mOnSuccessAndFaultListener.onFault(message);
                ToastUtils.show( message);
            } else {
                String message = jsonObject.getString("message");
                Log.e(TAG, "请求失败message: " + message);
                mOnSuccessAndFaultListener.onFault(message);
                ToastUtils.show( message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消ProgressDialog的时候，取消对observable的订阅，同时也取消了http请求
     */
    @Override
    public void onCancelProgress() {
        if (!this.isDisposed()) {
            this.dispose();
        }
    }
}
