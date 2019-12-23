package com.littlez.uiautomator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.littlez.uiautomator.adapter.VideosAdapter;
import com.littlez.uiautomator.bean.VideosBean;
import com.littlez.uiautomator.service.BackService;
import com.littlez.uiautomator.util.ExeCommand;
import com.littlez.uiautomator.util.LogUtil;
import com.littlez.uiautomator.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

//  https://www.testwo.com/blog/7057  这个是标准的配置文档
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext = this;
    /*各个平台对应的信息*/
    final ArrayList<VideosBean> videos = new ArrayList<>();
    private VideosAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        Button btnCheck = (Button) findViewById(R.id.btnCheck);
        Button btnStop = (Button) findViewById(R.id.btnStop);
        Button btnStartServe = (Button) findViewById(R.id.btnStartServe);

        LinearLayoutManager layoutManager =
                new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        /*这里就是数据源*/
        videos.add(new VideosBean("快手极速版", "KuaiSJiSutest"));
        videos.add(new VideosBean("刷宝短视频", "ShuaBaotest"));
        videos.add(new VideosBean("彩蛋视频", "CaiDantest"));
        videos.add(new VideosBean("火山极速版", "HuoShanJiSutest"));//火山也是low的一匹
        videos.add(new VideosBean("微视", "WeiShitest"));//微视 目前是最low的
        videos.add(new VideosBean("抖音极速版", "DouYinJiSutest"));//抖音是最low的而且还要支付宝提现


        adapter = new VideosAdapter(R.layout.adapter_videos_item, videos);
        recyclerView.setAdapter(adapter);


        btnCheck.setOnClickListener(this);
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
                                LogUtil.e("result---" + run1.getResult());
                                break;
                            }
                        }
                    }
                }
                LogUtil.e("result---" + run.getResult());
                break;
            case R.id.btnCheck://查看选中

                StringBuffer content = new StringBuffer();
                HashMap<Integer, Boolean> checkMaps = adapter.getCheckMaps();

                Set<Integer> keys = checkMaps.keySet();
                for (Integer key : keys) {
                    if (checkMaps.get(key)) content.append(videos.get(key).getAppName() + " ; ");
                }
                ToastUtils.show(TextUtils.isEmpty(content.toString()) ? "还没有选中平台" : content.toString());
                break;

            case R.id.btnStartServe://启动服务

                Intent intent = new Intent(mContext, BackService.class);
                Bundle bundle = new Bundle();
                bundle.putString("datas", "dsfak");
                intent.putExtra("bundle", bundle);
                startService(intent);

                break;

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
}
