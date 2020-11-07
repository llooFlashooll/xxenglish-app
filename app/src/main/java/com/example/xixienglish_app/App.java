package com.example.xixienglish_app;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.xuexiang.xui.XUI;
// 导入xui需要创建App类
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        XUI.init(this); //初始化UI框架
        XUI.debug(true);  //开启UI框架调试日志
        Stetho.initializeWithDefaults(this);
    }
}

