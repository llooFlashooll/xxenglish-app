package com.example.xixienglish_app.fragment;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.adapter.FragmentPagerAdapter;
import com.example.xixienglish_app.animation.DepthPageTransformer;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntitySet;
import com.example.xixienglish_app.entity.VideoEntitySet;
import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.List;


public class VideoFragment extends BaseFragment {

    private ViewPager pager;
    private TabLayout tabLayout;
    protected FragmentPagerAdapter pagerAdapter;


    @Override
    protected int initLayout() {
        return R.layout.video_fragment;
    }

    @Override
    protected void initView() {
        pager = mRootView.findViewById(R.id.view_pager);
        tabLayout = mRootView.findViewById(R.id.tablayout);
        pagerAdapter = new FragmentPagerAdapter(getChildFragmentManager());
    }

    @Override
    protected void initData() {
        // tab绑定viewpager，实现tab切换触发fragment切换
        tabLayout.setupWithViewPager(pager);

        // 设置pager切换动画
        pager.setPageTransformer(true, new DepthPageTransformer());

        // 将初始化期间从后端请求过来的数据绑定到页面上
        BaseActivity baseActivity = (BaseActivity) getActivity();
        List<VideoEntitySet> fragVideos = JSON.parseArray(baseActivity.getNavigationParams("video"), VideoEntitySet.class);
        for (VideoEntitySet cur : fragVideos)
            pagerAdapter.addFrag(new VideoPartitionFragment(cur), cur.getTag());
        pager.setAdapter(pagerAdapter);
    }


}
