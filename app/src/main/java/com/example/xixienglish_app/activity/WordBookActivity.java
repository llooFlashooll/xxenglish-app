package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.CourseAdapter;
import com.example.xixienglish_app.adapter.WordBookAdapter;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.CourseEntity;
import com.example.xixienglish_app.entity.WordbookEntity;

import java.util.HashMap;
import java.util.List;

public class WordBookActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private List<WordbookEntity> wordbookEntityList;
    private BaseActivity thisActivity = this;
    private static final int SET_ADAPTER = 0X1;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_ADAPTER:
                    recyclerView.setLayoutManager(new LinearLayoutManager(thisActivity));
                    recyclerView.setAdapter(new WordBookAdapter(wordbookEntityList, thisActivity, thisActivity));
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_word_book;
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.rv);
    }

    @Override
    protected void initData() {
        Api.config("/require/glossary", new HashMap<>()).getRequest(thisActivity, new HttpCallBack() {
            @Override
            public void onSuccess(String res) {

                wordbookEntityList = JSON.parseArray(res, WordbookEntity.class);

                Message msg = new Message();
                msg.what = SET_ADAPTER;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Exception e) { e.printStackTrace();}
        });
    }

    public void onBackClick(View v) {finish();}

}