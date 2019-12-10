package com.littlez.uiautomator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.littlez.uiautomator.util.ExeCommand;
import com.littlez.uiautomator.util.LogUtil;

import java.io.DataOutputStream;
import java.io.IOException;

//  https://www.testwo.com/blog/7057  这个是标准的配置文档
public class MainActivity extends AppCompatActivity {

    Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = (Button) findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LogUtil.e("点击");
                //这里面已经默认开启了线程
                ExeCommand cmd = new ExeCommand(false)
                        .run("uiautomator runtest AutoRunner.jar -c testpackage.Uitest", 60000);
            }
        });
    }


}
