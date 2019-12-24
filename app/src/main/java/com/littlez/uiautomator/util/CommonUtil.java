package com.littlez.uiautomator.util;

import android.text.TextUtils;

/**
 * created by xiaozhi
 * <p>常用的方法都放在这里面的
 * Date 2019/12/24
 */
public class CommonUtil {


    /**
     * 开始uiautomator的调用
     *
     * @param className
     */
    public static void startUiautomator(String className) {

        //这里面已经默认开启了线程
        ExeCommand cmd = new ExeCommand(false);
        cmd.run("uiautomator runtest AutoRunner.jar --nohup -c testpackage."
                + className, 30000);
        LogUtil.e("run uiautomator---" + className);
    }

    /**
     * 停止uiautomator的调用
     */
    public static void stopUiautomator() {

        //通过文件来停止 和启动 uiautomator
        ExeCommand run = new ExeCommand(true)
                .run("ps | grep uiautomator", 30000);
        if (!TextUtils.isEmpty(run.getResult())) {//不是空
            String result = run.getResult();
            if (result.contains("root")) {//有
                int root = result.indexOf("root");
                String substring = result.substring(root + "root".length());
                String[] s = substring.split(" ");
                for (int i = 0; i < s.length; i++) {
                    if (!TextUtils.isEmpty(s[i])) {
                        ExeCommand run1 = new ExeCommand(true)
                                .run("su -c kill " + s[i], 30000);
                        LogUtil.e("stop uiautomator---" + run1.getResult());
                        break;
                    }
                }
            }
        }
        LogUtil.e("result---" + run.getResult());
    }

}
