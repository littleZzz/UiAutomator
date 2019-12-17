package com.littlez.uiautomator.adapter;

import android.widget.Button;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.littlez.uiautomator.R;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * created by xiaozhi
 * <p>首页视频列表的 adapter
 * Date 2019/12/17
 */
public class VideosAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public VideosAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        TextView tvName = (TextView) helper.getView(R.id.tvName);
        TextView btnStart = (TextView) helper.getView(R.id.btnStart);

        tvName.setText(item);

    }
}
