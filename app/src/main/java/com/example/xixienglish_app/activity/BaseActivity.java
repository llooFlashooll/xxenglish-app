package com.example.xixienglish_app.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 必须先initBinding再initLayout，因为只有在binding中setContentView才能够findViewById以及findNavController
        initBinding();
        initLayout();
    }

    /**
     * 初始化页面布局
     */
    protected abstract void initLayout();

    /**
     * 初始化databinding和viewmodel
     */
    protected abstract void initBinding();
}
