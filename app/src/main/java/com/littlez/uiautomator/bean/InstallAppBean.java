package com.littlez.uiautomator.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * created by xiaozhi
 * <p>
 * Date 2019/12/18
 */
public class InstallAppBean {

    private String appName;
    private String downUrl;
    private String versionName;

    public InstallAppBean(String appName, String downUrl, String versionName) {
        this.appName = appName;
        this.downUrl = downUrl;
        this.versionName = versionName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDownUrl() {
        return downUrl;
    }

    public void setDownUrl(String downUrl) {
        this.downUrl = downUrl;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
}
