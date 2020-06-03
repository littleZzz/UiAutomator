package com.littlez.uiautomator.adapter;

import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.littlez.uiautomator.R;
import com.littlez.uiautomator.bean.InstallAppBean;

import java.util.List;

/**
 * created by xiaozhi
 * <p>安装应用列表的 adapter
 * Date 2019/12/17
 */
public class InstallAppListAdapter extends BaseQuickAdapter<InstallAppBean, BaseViewHolder> {

    public InstallAppListAdapter(int layoutResId, @Nullable List<InstallAppBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final InstallAppBean item) {
        TextView tvName = (TextView) helper.getView(R.id.tvName);
        TextView tvVersion = (TextView) helper.getView(R.id.tvVersion);

        tvName.setText(item.getAppName());
        tvVersion.setText(item.getVersionName());

    }
}
