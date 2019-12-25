package com.littlez.uiautomator.adapter;

import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.littlez.uiautomator.R;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * created by xiaozhi
 * <p>展示log  信息的列表数据
 * Date 2019/12/25
 */
public class LogsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public LogsAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        TextView tvContent = (TextView) helper.getView(R.id.tvContent);
        tvContent.setText(item);

    }
}
