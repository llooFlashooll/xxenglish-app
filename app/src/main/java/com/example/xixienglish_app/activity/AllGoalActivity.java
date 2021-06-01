package com.example.xixienglish_app.activity;

import android.widget.Button;

import com.example.xixienglish_app.R;

public class AllGoalActivity extends BaseActivity {
    private Button readingBtn;
    private Button listeningBtn;
    private Button wordBtn;
    private Button backBtn;

    @Override
    protected int initLayout() {
        return R.layout.activity_all_goal;
    }

    @Override
    protected void initView() {
        this.readingBtn = findViewById(R.id.reading_btn);
        this.listeningBtn = findViewById(R.id.listening_btn);
        this.wordBtn = findViewById(R.id.word_btn);
        this.backBtn = findViewById(R.id.back);
    }

    @Override
    protected void initData() {
        this.backBtn.setOnClickListener(v -> finish());
        this.readingBtn.setOnClickListener(v -> showToast("您已经订阅过该目标了欧~"));
        this.wordBtn.setOnClickListener(v -> showToast("您已经订阅过该目标了欧~"));
        this.listeningBtn.setOnClickListener(v -> showToast("您已经订阅过该目标了欧~"));
    }
}