package com.littlez.uiautomator.network.netutils;

/**
 * Created by littleZ on 2018/3/27.
 */
public interface DataCallbackListener {
    /**
     * 请求成功
     *
     * @param result
     * @param flag
     */
    void onSuccess(Object result, String flag);

    /**
     * 请求失败
     *
     * @param error
     * @param flag
     */
    void onFault(Object error, String flag);
}
