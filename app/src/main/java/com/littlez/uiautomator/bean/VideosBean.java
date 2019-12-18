package com.littlez.uiautomator.bean;

/**
 * created by xiaozhi
 * <p>
 * Date 2019/12/18
 */
public class VideosBean {

    private String appName;
    private String testClass;

    public VideosBean(String appName, String testClass) {
        this.appName = appName;
        this.testClass = testClass;
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
}
