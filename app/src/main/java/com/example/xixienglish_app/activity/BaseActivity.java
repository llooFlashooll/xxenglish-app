package com.example.xixienglish_app.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import com.xuexiang.xui.widget.toast.XToast;

import java.util.Map;

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;
    public Bundle mSavedInstanceState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
        mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); // 全局禁用横屏
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
    }

    // 子线程打印Toast,不报错，但有问题！！Looper.Loop()调用子线程直接结束
    public void showToastSync(String msg) {
        Looper.prepare();
        XToast.info(mContext, msg).show();
        Looper.loop();
    }

    public void showToastOnOtherThread(String msg) {
        runOnUiThread(() -> XToast.info(mContext, msg).show());
    }

    public void navigateTo(Class cls) {
        Intent in = new Intent(mContext, cls);
        startActivity(in);
    }

    /**
     * 带有参数的activity跳转，由于activity之间通信
     * @param cls
     * @param hash
     */
    public void navigateToWithParams(Class cls, Map<String, String> hash){
      Intent in = new Intent(mContext, cls);
      for(Map.Entry<String, String> cur : hash.entrySet())
        in.putExtra(cur.getKey(), cur.getValue());
      startActivity(in);
    }

    public void navigateToUrl(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

    /**
     * 根据key获取导航传递过来的value
     * @param key
     * @return
     */
    public String getNavigationParams(String key){
        Intent intent = getIntent();
        return intent.getStringExtra(key);
    }

    // 清除先前已跳转页面
    public void navigateToWithFlags(Class cls, int flags) {
        Intent in = new Intent(mContext, cls);
        in.setFlags(flags);
        startActivity(in);
    }

    // 存token
    protected void insertValueToSp(String key, String val) {
        SharedPreferences sp = getSharedPreferences("sp_xixienglish", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

    // 取token
    public String getValueFromSp(String key) {
        SharedPreferences sp = getSharedPreferences("sp_xixienglish", MODE_PRIVATE);
        return sp.getString(key, "");
    }

    // 删除token数据
    protected void removeValueFromSp(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences("sp_xixienglish", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
