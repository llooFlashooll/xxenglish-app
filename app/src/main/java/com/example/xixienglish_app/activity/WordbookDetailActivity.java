package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.WordbookEntity;

import java.util.HashMap;
import java.util.List;

public class WordbookDetailActivity extends BaseActivity {

    private Button pre;
    private Button next;
    private TextView english;
    private TextView chinese;
    private BaseActivity thisActivity = this;
    private List<WordbookEntity> wordbookEntityList;
    int curIdx = 0;

    @Override
    protected int initLayout() {
        return R.layout.activity_wordbook_detail;
    }

    @Override
    protected void initView() {
        pre = findViewById(R.id.pre_btn);
        next = findViewById(R.id.next_btn);
        english = findViewById(R.id.english);
        chinese = findViewById(R.id.chinese);
    }

    @Override
    protected void initData() {
        Api.config("/require/glossary", new HashMap<>()).getRequest(thisActivity, new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                wordbookEntityList = JSON.parseArray(res, WordbookEntity.class);
            }
            @Override
            public void onFailure(Exception e) { e.printStackTrace();}
        });
        pre.setOnClickListener(v -> {
            if (curIdx == 0) return;
            curIdx -- ;
            render();
        });
        next.setOnClickListener(v -> {
            if (curIdx == wordbookEntityList.size() - 1) return;
            curIdx ++ ;
            render();
            plusCnt("word");
        });
    }

    private void render() {
        if (curIdx < 0 || curIdx >= wordbookEntityList.size()) return;
        english.setText(wordbookEntityList.get(curIdx).getEnglish());
        chinese.setText(wordbookEntityList.get(curIdx).getChinese());
    }

    public void onBackClick(View v) {
        finish();
    }
}