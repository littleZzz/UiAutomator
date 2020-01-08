package com.littlez.uiautomator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.littlez.uiautomator.adapter.LogsAdapter;
import com.littlez.uiautomator.adapter.VideosAdapter;
import com.littlez.uiautomator.bean.VideosBean;
import com.littlez.uiautomator.bean.eventbus.EventbusBean;
import com.littlez.uiautomator.network.netsubscribe.PersionSubscribe;
import com.littlez.uiautomator.network.netutils.DataCallbackListener;
import com.littlez.uiautomator.service.BackService;
import com.littlez.uiautomator.util.CommonUtil;
import com.littlez.uiautomator.util.ExeCommand;
import com.littlez.uiautomator.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

//  https://www.testwo.com/blog/7057  这个是标准的配置文档
public class MainActivity extends AppCompatActivity implements View.OnClickListener, DataCallbackListener {

    Context mContext = this;
    /*各个平台对应的信息*/
    final ArrayList<VideosBean> videos = new ArrayList<>();
    ArrayList<String> logDatas = new ArrayList<>();

    private VideosAdapter adapter;
    private LogsAdapter logsAdapter;
    private RecyclerView rvLogs;
    private PersionSubscribe persionSubscribe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        persionSubscribe = new PersionSubscribe(mContext, this);

        TextView tvVersion = (TextView) findViewById(R.id.tvVersion);
        Button btnUpgradeApk = (Button) findViewById(R.id.btnUpgradeApk);
        Button btnUpgradeJar = (Button) findViewById(R.id.btnUpgradeJar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        rvLogs = (RecyclerView) findViewById(R.id.rvLogs);
        CheckBox cbCheckAll = (CheckBox) findViewById(R.id.cbCheckAll);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        Button btnStartServe = (Button) findViewById(R.id.btnStartServe);

        tvVersion.setText("版本：".concat(CommonUtil.packageName(mContext)));

        //设置logs adapter
        LinearLayoutManager logslayoutManager = new LinearLayoutManager(mContext);
        rvLogs.setLayoutManager(logslayoutManager);
        logsAdapter = new LogsAdapter(R.layout.adapter_logs_item, logDatas);
        rvLogs.setAdapter(logsAdapter);

        //设置产品列表
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        initData();//添加数据源

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
        btnUpgradeApk.setOnClickListener(this);
        btnUpgradeJar.setOnClickListener(this);

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
                for (int i = 0; i < videos.size(); i++) {
                    if (checkMaps.get(i)) videosBeans.add(videos.get(i));
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

            case R.id.btnUpgradeApk://更新apk
                ToastUtils.show("更新apk中");

                persionSubscribe.updateApk();//跟新apk
                break;
            case R.id.btnUpgradeJar://更新jar包
                ToastUtils.show("更新jar中");

                //修改文件可写入权限
                ExeCommand cmd = new ExeCommand(true);
                cmd.run("chmod -R 777 /data/local/tmp/", 30000);

                persionSubscribe.downloadJar();//跟新jar包
                break;

        }
    }


    /**
     * 网络请求
     *
     * @param result
     * @param flag
     */
    @Override
    public void onSuccess(Object result, String flag) {

    }

    @Override
    public void onFault(Object error, String flag) {

    }


    /**
     * eventBus事件处理
     *
     * @param eventbusBean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getEvent(EventbusBean eventbusBean) {

        /*添加 日志 记录 更新adapter*/
        String log = eventbusBean.getLog();
        logDatas.add(0, log);
        logsAdapter.notifyDataSetChanged();

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


    /**
     * 添加数据
     */
    private void initData() {
        /*这里就是数据源*/
        /*稳定数据*/
        videos.add(new VideosBean("快手极速版", "KuaiSJiSutest", 35 * 60 * 1000));
        videos.add(new VideosBean("刷宝短视频", "ShuaBaotest", 35 * 60 * 1000));
        videos.add(new VideosBean("彩蛋视频", "CaiDantest", 35 * 60 * 1000));
        videos.add(new VideosBean("微视", "WeiShitest", 10 * 60 * 1000));

        /*待确定的数据*/
        videos.add(new VideosBean("快看点视频", "KuaiKanDianTVtest", 25 * 60 * 1000));
        videos.add(new VideosBean("趣头条", "QuTouTiaotest", 25 * 60 * 1000));//id 经常变
        videos.add(new VideosBean("快看点新闻", "KuaiKanDianNewstest", 25 * 60 * 1000));
        videos.add(new VideosBean("微鲤畅聊版", "A08WeiLiChangLiaotest", 25 * 60 * 1000));

        //下面  是无用|或使用过废弃的数据  如 火山  淘看点 等等
        videos.add(new VideosBean("空数据", "hahhh", 30 * 60 * 1000));
//        videos.add(new VideosBean("火山极速版", "HuoShanJiSutest", 35 * 60 * 1000));//id 经常变  效率实在是差
    }

}
