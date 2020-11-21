package com.example.xixienglish_app.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.VideoFragmentItemAdapter;
import com.example.xixienglish_app.entity.VideoEntity;

import com.scwang.smart.refresh.layout.api.RefreshLayout;


public class VideoPartitionFragment extends BaseFragment {


  protected RecyclerView recyclerView;
  protected RefreshLayout refreshLayout;


  @Override
  protected int initLayout() {
    return R.layout.fragment_video_partition;
  }

  @Override
  protected void initView() {
    recyclerView= mRootView.findViewById(R.id.rv);
    refreshLayout = mRootView.findViewById(R.id.refreshLayout);
  }

  @Override
  protected void initData() {
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    recyclerView.setAdapter(new VideoFragmentItemAdapter(getActivity(), this));

    // 下拉刷新
    refreshLayout.setOnRefreshListener(layout ->{
      recyclerView.setAdapter(new VideoFragmentItemAdapter(getActivity(), this));
      layout.finishRefresh();
    });
    // 上拉加载
    refreshLayout.setOnLoadMoreListener(layout->{
      recyclerView.setAdapter(new VideoFragmentItemAdapter(getActivity(), this));
      layout.finishLoadMore();
    });

  }


}
