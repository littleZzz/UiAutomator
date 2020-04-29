package com.littlez.uiautomator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.littlez.uiautomator.base.Constant;
import com.littlez.uiautomator.bean.VideosBean;
import com.littlez.uiautomator.bean.eventbus.EventbusBean;
import com.littlez.uiautomator.network.netsubscribe.PersionSubscribe;
import com.littlez.uiautomator.network.netutils.DataCallbackListener;
import com.littlez.uiautomator.service.BackService;
import com.littlez.uiautomator.util.CommonUtil;
import com.littlez.uiautomator.util.ExeCommand;
import com.littlez.uiautomator.util.LogUtil;
import com.littlez.uiautomator.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, DataCallbackListener {
    Context mContext = this;
    /*各个平台对应的信息*/
    final ArrayList<VideosBean> videos = new ArrayList<>();
    ArrayList<String> logDatas = new ArrayList<>();

    private VideosAdapter adapter;
    private LogsAdapter logsAdapter;
    private RecyclerView rvLogs;
    private PersionSubscribe persionSubscribe;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        persionSubscribe = new PersionSubscribe(mContext, this);

        TextView tvVersion = (TextView) findViewById(R.id.tvVersion);
        Button btnUpgradeApk = (Button) findViewById(R.id.btnUpgradeApk);
        Button btnUpgradeJar = (Button) findViewById(R.id.btnUpgradeJar);
        Button btnMakeUpTime = (Button) findViewById(R.id.btnMakeUpTime);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        rvLogs = (RecyclerView) findViewById(R.id.rvLogs);
        CheckBox cbCheckAll = (CheckBox) findViewById(R.id.cbCheckAll);
        tvError = (TextView) findViewById(R.id.tvError);
        TextView tvGetScreen = (TextView) findViewById(R.id.tvGetScreen);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        Button btnStopServe = (Button) findViewById(R.id.btnStopServe);
        Button btnStartServe = (Button) findViewById(R.id.btnStartServe);

        tvVersion.setText("版本:".concat(CommonUtil.packageName(mContext)));
        tvVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean belongPeriodTime = CommonUtil.isBelongPeriodTime("17:20", "17:28");
                ToastUtils.show(belongPeriodTime + "");
            }
        });

        //设置logs adapter
        LinearLayoutManager logslayoutManager = new LinearLayoutManager(mContext);
        rvLogs.setLayoutManager(logslayoutManager);
        logsAdapter = new LogsAdapter(R.layout.adapter_logs_item, logDatas);
        rvLogs.setAdapter(logsAdapter);

        //设置产品列表
//        LinearLayoutManager layoutManager =
//                new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

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
        btnStopServe.setOnClickListener(this);
        btnStartServe.setOnClickListener(this);
        btnUpgradeApk.setOnClickListener(this);
        btnUpgradeJar.setOnClickListener(this);
        btnMakeUpTime.setOnClickListener(this);
        tvError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showLong(tvError.getText().toString());
            }
        });
        tvGetScreen.setOnClickListener(this);
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        Constant.isCloseService = false;
        switch (v.getId()) {
            case R.id.btnStop://停止按钮
                Constant.isrun = false;//重置启动的数据
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
                Constant.isrun = true;//开启运行
                Constant.videosBeans = CommonUtil.shuffleList(videosBeans);//打乱顺序
                Intent intent = new Intent(mContext, BackService.class);
                startService(intent);
                break;
            case R.id.btnStopServe://停止服务
                Constant.isrun = false;//重置启动的数据
                Constant.startFlag = 0;
                Constant.isCloseService = true;
                stopService(new Intent(mContext, BackService.class));
                CommonUtil.stopUiautomator();//停止服务同时停止调用
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
            case R.id.btnMakeUpTime://补足时间
                makeUpTime();
                break;
            case R.id.tvGetScreen://截取dump  信息
                LogUtil.e("8秒后执行dump任务");
                Timer timer = new Timer();// 实例化Timer类  
                timer.schedule(new TimerTask() {
                    public void run() {
                        CommonUtil.startUiautomator("A000ScreenTest");//dump 任务
                    }
                }, 8000);// 这里百毫秒  
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
        try {
            if (eventbusBean.isErrorStr()) {
                String errorStr = eventbusBean.getErrorStr();
                if (!errorStr.contains("A001ToHometest"))
                    tvError.setText(tvError.getText().toString().concat(errorStr));
            } else {
                /*添加 日志 记录 更新adapter*/
                String log = eventbusBean.getLog();
                logDatas.add(0, log);
                logsAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            LogUtil.e(e.toString());
        }
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
        videos.add(new VideosBean("刷宝短视频", "A02ShuaBaotest", 30 * 60 * 1000));
        videos.add(new VideosBean("彩蛋视频", "A03CaiDantest", 30 * 60 * 1000));
        videos.add(new VideosBean("牛角免费小说", "A21NiuJiaoYueDutest", 35 * 60 * 1000));
        videos.add(new VideosBean("音浪短视频", "A04YinLangtest", 30 * 60 * 1000));
        videos.add(new VideosBean("火山极速版", "A05HuoShanJISutest", 30 * 60 * 1000));
        videos.add(new VideosBean("每日爱清理", "A08MeiRiAiQingLitest", 30 * 60 * 1000));//测试中
        videos.add(new VideosBean("回首页待运行", "A001ToHometest", testGapTime));

        /*待确定的数据*/
//        videos.add(new VideosBean("番茄免费小说", "A23FanQieXiaoshuotest", 30 * 60 * 1000));
//        videos.add(new VideosBean("趣看点", "A13QuKanDiantest", 10 * 60 * 1000));//测试中  收益低
//        videos.add(new VideosBean("想看", "A11XiangKantest", 30 * 60 * 1000));//165 手机号会异常 控件检查页面
//        videos.add(new VideosBean("趣头条", "A06QuTouTiaotest", 30 * 60 * 1000));//好像经常变动id
//        videos.add(new VideosBean("快手极速版", "A01KuaiSJiSutest", 25 * 60 * 1000));//需要验证
//        vdeos.add(new VideosBean("趣看看", "A09QuKanKanNewstest", 0 * 60 * 1000));//165 暂时被封号了 和玩赚同数据
//        videos.add(new VideosBean("玩赚星球", "A04WanZhuanXingQiutest", 20 * 60 * 1000));//165被封号了
//        videos.add(new VideosBean("中青看点", "A07ZhongQinKanDiantest", 10 * 60 * 100ma0));//很慢放弃了ideos.add(new VideosBean("东方头条", "A10DongFangTTtest", 10 * 60 * 1000));
//        videos.add(new VideosBean("微鲤看看", "A08WeiLiKanKantest", 10 * 60 * 1000));//操作微慢 查找id 特别慢
        //下面  是无用|或使用过废弃的数据  如 火山  淘看点 等等
//        videos.add(new VideosBean("米读极速版", "A22MiDuJiSutest", 35 * 60 * 1000));//收益太低了  不想搞
//        videos.add(new VideosBean("快看点新闻", "A05KuaiKanDianNewstest", 30 * 60 * 1000));//收益低 而且提现165 171手机不通过
//        videos.add(new VideosBean("闪电盒子极速版", "A07ShanDianHeZitest", 20 * 60 * 1000));//提现必须要支付宝
        //        videos.add(new VideosBean("搜狐资讯", "A12SouHuZiXuntest", 30 * 60 * 1000));//收益低到窒息

        //淘新闻 注册第二天就异常
    }

    long testGapTime = 30 * 60 * 1000;

    //补足时间
    public void makeUpTime() {
        long gapTime = 0l;
        //筛选出已checkitem
        ArrayList<VideosBean> videosBeans = new ArrayList<>();
        HashMap<Integer, Boolean> checkMaps = adapter.getCheckMaps();
        for (int i = 0; i < videos.size(); i++) {
            if (checkMaps.get(i)) videosBeans.add(videos.get(i));
        }
        //计算已经选中的时间
        for (int i = 0; i < videosBeans.size(); i++) {
            if ("A001ToHometest".equals(videosBeans.get(i).getTestClass())) continue;
            gapTime += videosBeans.get(i).getGapTime();
        }
        testGapTime = 6 * 60 * 60 * 1000 - gapTime;//得出时间
        for (int i = 0; i < videos.size(); i++) {//跟新数据
            if ("A001ToHometest".equals(videos.get(i).getTestClass())) {
                videos.get(i).setGapTime(testGapTime);
            }
        }
        adapter.notifyDataSetChanged();
    }

}
