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

public class ArticleFragment extends Fragment {

    private ViewPager pager;
    private TabLayout tabLayout;
    private ArticleFragmentPagerAdapter pagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.article_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 获取控件
        pager = view.findViewById(R.id.view_pager);
        tabLayout = view.findViewById(R.id.tablayout);
        pagerAdapter = new ArticleFragmentPagerAdapter(getChildFragmentManager());

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