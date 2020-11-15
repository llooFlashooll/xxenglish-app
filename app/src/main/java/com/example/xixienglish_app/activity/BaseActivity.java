package com.example.xixienglish_app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.widget.toast.XToast;

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    public Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        mContext = this;
        setContentView(initLayout());
        initView();
        initData();
    }

    /**
     * 绑定xml
     */
    protected abstract int initLayout();

    /**
     * 获取组件
     */
    protected abstract void initView();

    /**
     * 组件触发逻辑
     */
    protected abstract void initData();

    public void showToast(String msg) {
        XToast.info(mContext, msg).show();
//        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    //子线程打印Toast,不报错
    public void showToastSync(String msg) {
        Looper.prepare();
        XToast.info(mContext, msg).show();
//        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    public void navigateTo(Class cls) {
        Intent in = new Intent(mContext, cls);
        startActivity(in);
    }

    // 清除先前已跳转页面
    public void navigateToWithFlags(Class cls, int flags) {
        Intent in = new Intent(mContext, cls);
        in.setFlags(flags);
        startActivity(in);
    }

    // 存token
    protected void insertVal(String key, String val) {
        SharedPreferences sp = getSharedPreferences("sp_flash", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

    // 取token
    protected String findByKey(String key) {
        SharedPreferences sp = getSharedPreferences("sp_flash", MODE_PRIVATE);
        return sp.getString(key, "");
    }
}
