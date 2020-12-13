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
import com.example.xixienglish_app.adapter.CommentDetailAdapter;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntity;
import com.example.xixienglish_app.entity.NCommentEntity;

import java.util.HashMap;
import java.util.List;

public class CommentDetailActivity extends BaseActivity {
    private RecyclerView recyclerView;
    public static final int SET_ADAPTER = 0x1;
    private BaseActivity thisActivity = this;
    private List<NCommentEntity> nCommentEntities;
    /**
     * http请求的线程中不能setAdapter，移到handler中做
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_ADAPTER:
                    recyclerView.setLayoutManager(new LinearLayoutManager(thisActivity));
                    recyclerView.setAdapter(new CommentDetailAdapter(nCommentEntities, thisActivity, thisActivity));
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_comment_detail;
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.rv);
    }

    @Override
    protected void initData() {
        HashMap<String, Object> params = new HashMap<String, Object>() {{
            put("rootReviewId", getNavigationParams("rootReviewId"));
        }};
        Api.config("/review/second", params).getRequest(this, new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                nCommentEntities = JSON.parseArray(res, NCommentEntity.class);
                Message msg = new Message();
                msg.what = SET_ADAPTER;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }


    public void onBackClick(View v) {finish();}
}