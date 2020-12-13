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
import com.example.xixienglish_app.adapter.ArticleFragmentItemAdapter;
import com.example.xixienglish_app.adapter.ArticleVideoItemAdapter;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.HashMap;
import java.util.List;

/**
 * 获取用户收藏的帖子列表
 */
public class CollectionDetailActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private List<ArticleEntity> articleEntityList;
    private BaseActivity thisActivity = this;
    public static final int SET_ADAPTER = 0x1;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_ADAPTER:
                    recyclerView.setLayoutManager(new LinearLayoutManager(thisActivity));
                    // 实现条件判断
                    ArticleEntity e = null;
                    int tag = 0;
                    if (articleEntityList.size()!=0) {
                        e = articleEntityList.get(0);
                        if (String.valueOf(e.getTag().charAt(0)).equals("v")) {
                            tag = 1;
                        }
                        else {
                            tag = 0;
                        }
                        recyclerView.setAdapter(new ArticleVideoItemAdapter(thisActivity, articleEntityList, thisActivity, tag));
                    }
                    else {
                        recyclerView.setAdapter(new ArticleVideoItemAdapter(thisActivity, articleEntityList, thisActivity, tag));
                    }

                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_collection_detail;
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.rv);
    }

    @Override
    protected void initData() {
        Api.config("/require/favorites", new HashMap<>()).getRequest(thisActivity, new HttpCallBack() {
            @Override
            public void onSuccess(String res) {

                articleEntityList = JSON.parseArray(res, ArticleEntity.class);

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