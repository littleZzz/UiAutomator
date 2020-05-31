package com.littlez.uiautomator.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.littlez.uiautomator.R;
import com.littlez.uiautomator.adapter.InstallAppListAdapter;
import com.littlez.uiautomator.bean.InstallAppBean;
import com.littlez.uiautomator.network.netsubscribe.PersionSubscribe;
import com.littlez.uiautomator.network.netutils.DataCallbackListener;
import com.littlez.uiautomator.util.ToastUtils;

import java.util.ArrayList;

/**
 * 安装应用界面
 */
public class InstallAppActivity extends AppCompatActivity implements DataCallbackListener {

    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install_app);

        final PersionSubscribe persionSubscribe = new PersionSubscribe(mContext, this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        final ArrayList<InstallAppBean> strings = new ArrayList<>();
        strings.add(new InstallAppBean("微信", "weixin", "7.0.13"));
        strings.add(new InstallAppBean("豌豆荚", "wandoujia", "6.17.31"));

        strings.add(new InstallAppBean("彩蛋视频", "caidan", "1.20.0.0525.1623"));
        strings.add(new InstallAppBean("火山极速", "huoshanjisu", "7.5.0"));
        strings.add(new InstallAppBean("牛角阅读", "niujiao", "2.3.3"));
        strings.add(new InstallAppBean("刷宝短视频", "shuabao", "2.200"));
        strings.add(new InstallAppBean("天天爱清理", "tiantianaiqingli", "1.1.1.1.0.0422.1406"));
        strings.add(new InstallAppBean("趣铃声", "qulingsheng", "2.0.9.100.0515.1054"));
        strings.add(new InstallAppBean("天天短视频", "tiantianduanshiping", " 1.1.2"));
        strings.add(new InstallAppBean("想看", "xiangkan", " 4.9.51"));

//        strings.add(new InstallAppBean("音浪短视频", "yinlang", "1.0.5.0"));//提现不了
//        strings.add(new InstallAppBean("赚钱小视频", "zhuanqianxiaoshiping", "1.6.7.0.5.0"));//取消了
        //设置安装应用列表
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        InstallAppListAdapter adapter = new InstallAppListAdapter(R.layout.adapter_installapp_item, strings);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtils.show("下载apk中  请耐心等候一下");
                persionSubscribe.InstallApk(strings.get(position).getDownUrl());//跟新apk
            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onSuccess(Object result, String flag) {

    }

    @Override
    public void onFault(Object error, String flag) {

    }
}
