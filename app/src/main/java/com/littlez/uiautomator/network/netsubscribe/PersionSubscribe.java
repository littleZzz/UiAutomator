package com.littlez.uiautomator.network.netsubscribe;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;


import com.google.gson.Gson;
import com.littlez.uiautomator.dialog.LoadingDialog;
import com.littlez.uiautomator.network.netapi.HttpApi;
import com.littlez.uiautomator.network.netutils.DataCallbackListener;
import com.littlez.uiautomator.network.netutils.HttpMethods;
import com.littlez.uiautomator.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Random;

import androidx.core.content.FileProvider;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by littleZ on 2018/12/29.
 * 基本的订阅
 */

public class PersionSubscribe {
    private static final String TAG = "PersionSubscribe";
    private Gson gson;
    private Context mContext;
    private DataCallbackListener dataCallback;
    private String baseDownloadUrl = "http://148.70.179.199:3001/";

    public PersionSubscribe(Context context, DataCallbackListener dataCallbackListener) {
        this.gson = new Gson();
        this.mContext = context;
        this.dataCallback = dataCallbackListener;
    }


    //下载jar脚本并放置到文件中
    public void downloadJar() {

        Random random = new Random();
        int id = random.nextInt();

        HashMap<String, Object> map = new HashMap<>();// AutoRunner.jar
        map.put("id", id);//默認值: 10
        //这里对地址的格式有很多要求
        Call<ResponseBody> bodyCall = HttpMethods.getInstance().getRetrofit().newBuilder()
                .baseUrl(baseDownloadUrl).build().create(HttpApi.class).downloadJar(map);
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null) return;
                try {
                    writeResponseBodyToDisk(response.body(), true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                LogUtil.e(t.toString());
            }
        });
    }


    /**
     * 跟新apk
     */
    public void InstallApk(String appName) {

        Random random = new Random();
        int id = random.nextInt();

        final LoadingDialog dialog = LoadingDialog.getInstance(mContext);
        dialog.show();
        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);//默認值: 10
        //这里对地址的格式有很多要求
        Call<ResponseBody> bodyCall = HttpMethods.getInstance().getRetrofit().newBuilder()
                .baseUrl(baseDownloadUrl).build().create(HttpApi.class).installApk(appName, map);
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null) return;
                try {
                    boolean b = writeResponseBodyToDisk(response.body(), false);
                    dialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                LogUtil.e(t.toString());
            }
        });
    }

    /**
     * 跟新apk
     */
    public void updateApk() {

        Random random = new Random();
        int id = random.nextInt();

        HashMap<String, Object> map = new HashMap<>();
        map.put("id", id);//默認值: 10
        //这里对地址的格式有很多要求
        Call<ResponseBody> bodyCall = HttpMethods.getInstance().getRetrofit().newBuilder()
                .baseUrl(baseDownloadUrl).build().create(HttpApi.class).updateApk(map);
        bodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.body() == null) return;
                try {
                    writeResponseBodyToDisk(response.body(), false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

                LogUtil.e(t.toString());
            }
        });
    }

    private String jar_path = "/data/local/tmp/AutoRunner.jar";//存放jar的地址
    private String apk_path = "/sdcard/UiAutomator.apk";//存放apk的地址

    /**
     * 写入文件
     */
    private boolean writeResponseBodyToDisk(ResponseBody body, boolean isJarOrApk) {
        try {

            File futureStudioIconFile = new File(isJarOrApk ? jar_path : apk_path);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                    LogUtil.e("file download: " + fileSizeDownloaded + " of " + fileSize);
                }
                outputStream.flush();
                LogUtil.e("file download: over");

                //是安装apk  进行安装
                if (!isJarOrApk) installAPK();

                return true;
            } catch (IOException e) {
                LogUtil.e(e.toString());
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            LogUtil.e(e.toString());
            return false;
        }
    }


    /**
     * 安装Apk
     */
    //下载到本地后执行安装
    private void installAPK() {
        File file = new File(apk_path);
        //获取下载文件的Uri
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //7.0 以上需要FileProvider进行设置
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".fileprovider", file);
            intent.setDataAndType(apkUri, mContext.getContentResolver().getType(apkUri));
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        mContext.startActivity(intent);
    }


}
