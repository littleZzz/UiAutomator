package com.littlez.uiautomator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.littlez.uiautomator.adapter.VideosAdapter;
import com.littlez.uiautomator.util.ExeCommand;
import com.littlez.uiautomator.util.LogUtil;

import java.util.ArrayList;

//  https://www.testwo.com/blog/7057  这个是标准的配置文档
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext = this;
    String[] datas = {"WeiShitest", "Uitest", ""};//存放的是启动的数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
        Button btnStop = (Button) findViewById(R.id.btnStop);

        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        ArrayList<String> strings = new ArrayList<>();
        strings.add("微视");
        strings.add("刷宝短视频");
        strings.add("快手短视频极速版本");
        VideosAdapter adapter = new VideosAdapter(R.layout.adapter_videos_item, strings);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LogUtil.e("点击");
                if (TextUtils.isEmpty(datas[position])) {
                    Toast.makeText(mContext, "开发中。", Toast.LENGTH_SHORT).show();
                    return;
                }
                //这里面已经默认开启了线程
                ExeCommand cmd = new ExeCommand(false);
                cmd.run("uiautomator runtest AutoRunner.jar --nohup -c testpackage." + datas[position], 60000);
            }
        });
        recyclerView.setAdapter(adapter);


        btnStop.setOnClickListener(this);

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
        }
    }
}
