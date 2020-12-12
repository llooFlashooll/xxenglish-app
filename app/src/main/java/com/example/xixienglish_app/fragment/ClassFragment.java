package com.example.xixienglish_app.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.activity.LiveActivity;
import com.example.xixienglish_app.adapter.CommentDetailAdapter;
import com.example.xixienglish_app.adapter.CourseAdapter;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.CourseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private static final int SET_ADAPTER = 0X1;
    private List<CourseEntity> courseEntityList;
    private BaseFragment thisFragment = this;

    /**
     * http请求的线程中不能setAdapter，移到handler中做
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_ADAPTER:
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(new CourseAdapter(courseEntityList, getActivity(), 0, thisFragment));
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.class_fragment;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.rv);
    }

    @Override
    protected void initData() {
        Api.config("/course", new HashMap<>()).getRequest(getActivity(), new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                courseEntityList = JSON.parseArray(res, CourseEntity.class);
                Message msg = new Message();
                msg.what = SET_ADAPTER;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Exception e) { e.printStackTrace();}
        });
    }
}
