package com.example.xixienglish_app.fragment;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.ArticleFragmentPagerAdapter;
import com.example.xixienglish_app.animation.DepthPageTransformer;
import com.example.xixienglish_app.databinding.ArticleFragmentBinding;
import com.google.android.material.tabs.TabLayout;
import com.xuexiang.xui.widget.tabbar.EasyIndicator;

public class ArticleFragment extends BaseFragment {

    private ViewPager pager;
    private TabLayout tabLayout;
    private ArticleFragmentPagerAdapter pagerAdapter;



    @Override
    protected int initLayout() {
        return R.layout.article_fragment;
    }

    @Override
    protected void initView() {
        pager = mRootView.findViewById(R.id.view_pager);
        tabLayout = mRootView.findViewById(R.id.tablayout);
        pagerAdapter = new ArticleFragmentPagerAdapter(getChildFragmentManager());
    }

    @Override
    protected void initData() {
        // tab绑定viewpager，实现tab切换触发fragment切换
        tabLayout.setupWithViewPager(pager);

        // 设置pager切换动画
        pager.setPageTransformer(true, new DepthPageTransformer());

        // todo: 将手动的addFrag改为调api
        pagerAdapter.addFrag(new ArticlePartitionFragment(), "Tab1");
        pagerAdapter.addFrag(new ArticlePartitionFragment(), "Tab2");
        pagerAdapter.addFrag(new ArticlePartitionFragment(), "Tab3");

        // 注意setAdaper后pagerAdapter就不能被改变了，因此要先addFrag后setAdapter
        pager.setAdapter(pagerAdapter);
    }


}