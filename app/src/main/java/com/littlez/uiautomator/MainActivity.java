package com.littlez.uiautomator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.littlez.uiautomator.util.ExeCommand;
import com.littlez.uiautomator.util.LogUtil;

//  https://www.testwo.com/blog/7057  这个是标准的配置文档
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnWeishi = (Button) findViewById(R.id.btnWeishi);
        Button btnShuaBao = (Button) findViewById(R.id.btnShuaBao);
        Button btnStop = (Button) findViewById(R.id.btnStop);

        btnWeishi.setOnClickListener(this);
        btnShuaBao.setOnClickListener(this);
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
            case R.id.btnWeishi:

                LogUtil.e("点击");
                //这里面已经默认开启了线程
                new ExeCommand(false)
                        .run("uiautomator runtest AutoRunner.jar --nohup -c testpackage.WeiShitest", 60000);
                break;
            case R.id.btnShuaBao:

                LogUtil.e("点击");
                //这里面已经默认开启了线程
                new ExeCommand(false)
                        .run("uiautomator runtest AutoRunner.jar --nohup -c testpackage.Uitest", 60000);

                break;

            case R.id.btnStop://停止按钮

                //通过文件来停止 和启动 uiautomator

                ExeCommand run = new ExeCommand(true)
                        .run("ps | grep uiautomator", 30000);

                if (!TextUtils.isEmpty(run.getResult())) {//不是空
                    String result = run.getResult();
                    if (result.contains("root")) {//有
                        int root = result.indexOf("root");
                        String substring = result.substring(root);
                        String[] s = substring.split(" ");
                        for (int i = 0; i < s.length; i++) {
                            if (!TextUtils.isEmpty(s[i])) {
                                new ExeCommand(true)
                                        .run("kill " + s[i], 30000);
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
