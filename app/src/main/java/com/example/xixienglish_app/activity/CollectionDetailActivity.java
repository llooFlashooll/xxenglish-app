package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;

import com.example.xixienglish_app.R;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class CollectionDetailActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;

    @Override
    protected int initLayout() {
        return R.layout.activity_collection_detail;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public void onBackClick(View v) {finish();}

}