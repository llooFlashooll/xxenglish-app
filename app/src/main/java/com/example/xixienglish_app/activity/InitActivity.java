package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntitySet;
import com.example.xixienglish_app.fragment.ArticlePartitionFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InitActivity extends BaseActivity {

    public static final int NOTIFY = 0x1;
    private Map<String, String> navigationParams = new HashMap<>();
    /**
     * 记录接口请求次数
     */
    private int cnt = 0;


    protected Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case NOTIFY:
                    // 当文章和视频两个接口都请求成功跳转
                    if ( ++ cnt == 2) navigateToWithParams(MainActivity.class, navigationParams);
                    break;
            }
        }
    };


    @Override
    protected int initLayout() {
        return R.layout.activity_init;
    }

    @Override
    protected void initView() { }

    @Override
    protected void initData() {
        HashMap<String, Object> httpParams = new HashMap<>();
        // 请求文章的第1页数据
        httpParams.put("pageId", 1);
        Api.config("/noTagNews", httpParams).getRequest(this, new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                navigationParams.put("article", res);
                Message msg = new Message();
                msg.what = NOTIFY;
                handler.sendMessage(msg);
            }
            @Override
            public void onFailure(Exception e) { e.printStackTrace(); }
        });
        // 请求视频的第1页数据
        httpParams.put("pageId", 1);
        Api.config("/noTagVideos", httpParams).getRequest(this, new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                navigationParams.put("video", res);
                Message msg = new Message();
                msg.what = NOTIFY;
                handler.sendMessage(msg);
            }
            @Override
            public void onFailure(Exception e) { e.printStackTrace(); }
        });


    }
}