package com.example.xixienglish_app.fragment;


import com.example.xixienglish_app.R;

/**
 * 由于VideoFragment与ArticleFragment的分页方式完全相同，只是具体某一页填充的内容不同
 * 因此直接令VideoFragment继承ArticleFragment
 */
public class VideoFragment extends ArticleFragment {

    @Override
    protected int initLayout() {
        return R.layout.video_fragment;
    }

    @Override
    protected void addFragFromBackend() {
      pagerAdapter.addFrag(new VideoPartitionFragment(), "Tab1");
      pagerAdapter.addFrag(new VideoPartitionFragment(), "Tab2");
      pagerAdapter.addFrag(new VideoPartitionFragment(), "Tab3");
      pagerAdapter.addFrag(new VideoPartitionFragment(), "Tab4");
    }
}
