package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.xixienglish_app.R;


public class MyGoalActivity extends BaseActivity {
    private Button backBtn;

    @Override
    protected int initLayout() {
        return R.layout.activity_my_goal;
    }

    @Override
    protected void initView() {
        backBtn = findViewById(R.id.back);
    }

    @Override
    protected void initData() {
        backBtn.setOnClickListener(v -> finish());
    }
}