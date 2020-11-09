package com.example.xixienglish_app.activity;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
