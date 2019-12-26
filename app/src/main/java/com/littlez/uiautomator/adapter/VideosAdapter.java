package com.littlez.uiautomator.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.littlez.uiautomator.R;
import com.littlez.uiautomator.bean.VideosBean;
import com.littlez.uiautomator.util.CommonUtil;
import com.littlez.uiautomator.util.ExeCommand;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.Nullable;

/**
 * created by xiaozhi
 * <p>首页视频列表的 adapter
 * Date 2019/12/17
 */
public class VideosAdapter extends BaseQuickAdapter<VideosBean, BaseViewHolder> {


    /*记录checkBox check状态的map  注意 如果 notifidata的时候要考虑是否清除这个map*/
    public HashMap<Integer, Boolean> checkMaps = new HashMap<>();

    public HashMap<Integer, Boolean> getCheckMaps() {
        return checkMaps;
    }

    public VideosAdapter(int layoutResId, @Nullable List<VideosBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(final BaseViewHolder helper, final VideosBean item) {
        final CheckBox checkbox = (CheckBox) helper.getView(R.id.checkbox);

        TextView tvName = (TextView) helper.getView(R.id.tvName);
        final EditText etSetRunTimeGap = (EditText) helper.getView(R.id.etSetRunTimeGap);
        Button btnStart = (Button) helper.getView(R.id.btnStart);

        tvName.setText(item.getAppName());

        /*edittext  设置tag 解决复用问题*/
        if (etSetRunTimeGap.getTag() instanceof TextWatcher) {
            etSetRunTimeGap.removeTextChangedListener((TextWatcher) etSetRunTimeGap.getTag());
        }
        etSetRunTimeGap.setText("".concat((item.getGapTime() / 1000 / 60) + ""));
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {//为空
                    etSetRunTimeGap.setText("0");
                    etSetRunTimeGap.setSelection(0);//定位光标
                    item.setGapTime(0);
                } else {
                    int time = Integer.parseInt(s.toString().trim());
                    item.setGapTime(time * 60 * 1000);//注意 这里需要变换为毫秒值
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        etSetRunTimeGap.addTextChangedListener(textWatcher);
        etSetRunTimeGap.setTag(textWatcher);

        /*开始按钮*/
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(item.getTestClass())) {
                    Toast.makeText(mContext, "开发中。", Toast.LENGTH_SHORT).show();
                    return;
                }

                //这里面已经默认开启了线程
                CommonUtil.startUiautomator(item.getTestClass());

            }
        });

        /*checkBox 事件*/
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkMaps.put(helper.getLayoutPosition(), checkbox.isChecked());
            }
        });
        /*  恢复checked 和列表显示的状态 状态 */
        if (checkMaps.containsKey(helper.getLayoutPosition())) {
            checkbox.setChecked(checkMaps.get(helper.getLayoutPosition()));
        } else {//设置默认数据 全部商品默认收起 其他默认展开
            checkbox.setChecked(false);
        }

    }
}
