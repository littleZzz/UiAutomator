package com.littlez.uiautomator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.littlez.uiautomator.adapter.LogsAdapter;
import com.littlez.uiautomator.adapter.VideosAdapter;
import com.littlez.uiautomator.bean.VideosBean;
import com.littlez.uiautomator.bean.eventbus.EventbusBean;
import com.littlez.uiautomator.service.BackService;
import com.littlez.uiautomator.util.CommonUtil;
import com.littlez.uiautomator.util.ExeCommand;
import com.littlez.uiautomator.util.LogUtil;
import com.littlez.uiautomator.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

//  https://www.testwo.com/blog/7057  这个是标准的配置文档
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext = this;
    /*各个平台对应的信息*/
    final ArrayList<VideosBean> videos = new ArrayList<>();
    ArrayList<String> logDatas = new ArrayList<>();

    private VideosAdapter adapter;
    private LogsAdapter logsAdapter;
    private RecyclerView rvLogs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        rvLogs = (RecyclerView) findViewById(R.id.rvLogs);
        CheckBox cbCheckAll = (CheckBox) findViewById(R.id.cbCheckAll);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        Button btnStartServe = (Button) findViewById(R.id.btnStartServe);


        //设置logs adapter
        LinearLayoutManager logslayoutManager = new LinearLayoutManager(mContext);
        rvLogs.setLayoutManager(logslayoutManager);
        logsAdapter = new LogsAdapter(R.layout.adapter_logs_item, logDatas);
        rvLogs.setAdapter(logsAdapter);

        //设置产品列表
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        /*这里就是数据源*/
        videos.add(new VideosBean("快手极速版", "KuaiSJiSutest", 60 * 60 * 1000));
        videos.add(new VideosBean("刷宝短视频", "ShuaBaotest", 60 * 60 * 1000));
        videos.add(new VideosBean("彩蛋视频", "CaiDantest", 60 * 60 * 1000));

        videos.add(new VideosBean("淘看点", "TaoKanDiantest", 60 * 60 * 1000));
        videos.add(new VideosBean("火山极速版", "HuoShanJiSutest", 35 * 60 * 1000));
        videos.add(new VideosBean("趣头条", "QuTouTiaotest", 35 * 60 * 1000));

        //下面的数据都是写不给力的数据
        videos.add(new VideosBean("微视", "WeiShitest", 10 * 60 * 1000));

        //下面这个是空数据占位子用的
        videos.add(new VideosBean("抖音极速版", "DouYinJiSutest", 35 * 60 * 1000));
        videos.add(new VideosBean("空数据", "hahhh", 30 * 60 * 1000));


        adapter = new VideosAdapter(R.layout.adapter_videos_item, videos);
        recyclerView.setAdapter(adapter);


        cbCheckAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for (int i = 0; i < videos.size(); i++) {
                    adapter.checkMaps.put(i, isChecked);
                }
                adapter.notifyDataSetChanged();
            }
        });

        btnStop.setOnClickListener(this);
        btnStartServe.setOnClickListener(this);

    }


    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btnStop://停止按钮

                CommonUtil.stopUiautomator();//停止调用

                break;
            case R.id.btnStartServe://启动服务

                ArrayList<VideosBean> videosBeans = new ArrayList<>();

                HashMap<Integer, Boolean> checkMaps = adapter.getCheckMaps();
                Set<Integer> keys = checkMaps.keySet();
                for (Integer key : keys) {
                    if (checkMaps.get(key)) videosBeans.add(videos.get(key));
                }
                if (videosBeans.size() <= 0) {
                    ToastUtils.show("还没有选中平台");
                    return;
                }

                Intent intent = new Intent(mContext, BackService.class);
                intent.putParcelableArrayListExtra("datas", videosBeans);
                startService(intent);

                break;
            case R.id.btnStopServe://停止服务
                stopService(new Intent(mContext, BackService.class));
                break;

        }
    }


    /**
     * eventBus事件处理
     *
     * @param eventbusBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(EventbusBean eventbusBean) {

        String log = eventbusBean.getLog();
        logDatas.add(0, log);
        logsAdapter.notifyDataSetChanged();
//        rvLogs.scrollToPosition(logsAdapter.getItemCount() - 1);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        //实现home 效果
        Intent i = new Intent(Intent.ACTION_MAIN);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addCategory(Intent.CATEGORY_HOME);
        startActivity(i);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
