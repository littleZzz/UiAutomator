package com.littlez.uiautomator.bean;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * created by xiaozhi
 * <p>
 * Date 2019/12/18
 */
public class VideosBean implements Parcelable {

    private String appName;
    private String testClass;
    private long gapTime;


    public VideosBean(String appName, String testClass, long gapTime) {
        this.appName = appName;
        this.testClass = testClass;
        this.gapTime = gapTime;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTestClass() {
        return testClass;
    }

    public void setTestClass(String testClass) {
        this.testClass = testClass;
    }

    public long getGapTime() {
        return gapTime;
    }

    public void setGapTime(long gapTime) {
        this.gapTime = gapTime;
    }

    @Override
    public String toString() {
        return "VideosBean{" +
                "appName='" + appName + '\'' +
                ", testClass='" + testClass + '\'' +
                ", gapTime=" + gapTime +
                '}';
    }



    protected VideosBean(Parcel in) {
        appName = in.readString();
        testClass = in.readString();
        gapTime = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appName);
        dest.writeString(testClass);
        dest.writeLong(gapTime);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<VideosBean> CREATOR = new Creator<VideosBean>() {
        @Override
        public VideosBean createFromParcel(Parcel in) {
            return new VideosBean(in);
        }

        @Override
        public VideosBean[] newArray(int size) {
            return new VideosBean[size];
        }
    };

}
