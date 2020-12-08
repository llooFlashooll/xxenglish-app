package com.example.xixienglish_app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.activity.InputCommentActivity;
import com.example.xixienglish_app.adapter.CommentItemAdapter;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.CommentEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CommentFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private List<CommentEntity> commentEntityList;
    public static final int SET_ADAPTER = 0x1;
    private BaseFragment thisFragment = this;
    private String newsId;
    private BaseActivity parentActivity;

    public CommentFragment(String newsId, BaseActivity parentActivity) {
        this.newsId = newsId;
        this.parentActivity = parentActivity;
    }

    /**
     * http请求的线程中不能setAdapter，移到handler中做
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_ADAPTER:
                    recyclerView.setAdapter(new CommentItemAdapter(getActivity(), commentEntityList, thisFragment));
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.fragment_comment;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.rv);
    }

    @Override
    protected void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 从后端请求评论列表
        getCommentList();

    }

    private void getCommentList() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("newsId", newsId);
        Api.config("/review/root", params).getRequest(getActivity(), new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                commentEntityList = JSON.parseArray(res, CommentEntity.class);
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


}