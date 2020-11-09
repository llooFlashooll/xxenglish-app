package com.example.xixienglish_app.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.ArticleFragmentItemAdapter;

/**
 * Article页面下具体的某一tag，比如商业/科技/体育
 */
public class ArticlePartitionFragment extends BaseFragment {

    private RecyclerView recyclerView;


    @Override
    protected int initLayout() {
        return R.layout.fragment_article_partition;
    }

    @Override
    protected void initView() {
        recyclerView= mRootView.findViewById(R.id.rv);
    }

    @Override
    protected void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ArticleFragmentItemAdapter(getActivity()));
    }
}