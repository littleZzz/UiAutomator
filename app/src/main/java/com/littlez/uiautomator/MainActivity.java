package com.littlez.uiautomator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.littlez.uiautomator.util.ShellUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnStart = (Button) findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new StartTest().start();
            }
        });
    }


//    com.example.myapplication.test1#test com.example.myapplication.test
//    格式为：报名.测试类名#测试方法 报名.test/android.support.test.runner.AndroidJUnitRunner
//    报名.test/android.support.test.runner.AndroidJUnitRunner为固定格式

    class StartTest extends Thread {
        @Override
        public void run() {
            //class后的参数是包名+类名
            ShellUtils.execCommand(
                    generateCommand("com.littlez.uiautomator",
                            "Uitest", "test"), false);

        }
    }


    /**
     * 生成命令
     *
     * @param pkgName uiautomator包名
     * @param clsName uiautomator类名
     * @param mtdName uiautomator方法名
     * @return
     */
    public String generateCommand(String pkgName, String clsName, String mtdName) {
        String command = "am instrument -w -r -e debug false -e class "
                + pkgName + "." + clsName + "#" + mtdName + " "
                + pkgName + ".test/android.support.test.runner.AndroidJUnitRunner";
        Log.e("test1: ", command);
        return command;
    }


}
