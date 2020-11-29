package com.example.xixienglish_app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.ArticleFragmentItemAdapter;
import com.example.xixienglish_app.adapter.VideoFragmentItemAdapter;
import com.example.xixienglish_app.animation.RoundedCornersTransformation;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntity;
import com.example.xixienglish_app.entity.ArticleEntitySet;
import com.example.xixienglish_app.entity.VideoEntity;

import com.example.xixienglish_app.entity.VideoEntitySet;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;


public class VideoPartitionFragment extends BaseFragment {


  protected RecyclerView recyclerView;
  protected RefreshLayout refreshLayout;
  private VideoEntitySet videoEntitySet;
  private int curPageId = 1;
  private BaseFragment thisFragment = this;
  private static final int SET_VIDEO_ADAPTER = 0x3;
  private static final int SET_VIDEO_ADAPTER_AND_SET_POS = 0x4;

  public VideoPartitionFragment(VideoEntitySet videoEntitySet){
    this.videoEntitySet = videoEntitySet;
  }

  /**
   * http请求的线程中不能setAdapter，移到handler中做
   */
  private Handler handler = new Handler(Looper.getMainLooper()){
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what){
        case SET_VIDEO_ADAPTER:
          recyclerView.setAdapter(new VideoFragmentItemAdapter(getActivity(), videoEntitySet.getVideoList(), thisFragment));
          break;
        case SET_VIDEO_ADAPTER_AND_SET_POS:
          recyclerView.scrollToPosition((curPageId - 1) * 20);
          recyclerView.setAdapter(new VideoFragmentItemAdapter(getActivity(), videoEntitySet.getVideoList(), thisFragment));
      }
    }
  };

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
    recyclerView.setAdapter(new VideoFragmentItemAdapter(getActivity(), videoEntitySet.getVideoList(), this));

    // 下拉刷新(即向上滑)
    refreshLayout.setOnRefreshListener(layout -> {
      loadVideoList(false);
      layout.finishRefresh();
    });
    // 上拉加载(即向下滑)
    refreshLayout.setOnLoadMoreListener(layout -> {
      loadVideoList(true);
      layout.finishLoadMore();
    });
  }

  /**
   *
   * @param down 是否向下滑
   */
  private void loadVideoList(boolean down){
    // 因为后端视频数量有限，调整页码切换策略
    // 向下滑 + 1，向上滑回到起始页
    curPageId = down ? curPageId + 1 : 1;
    String tag = videoEntitySet.getTag();
    HashMap<String, Object> hash = new HashMap<>();
    hash.put("videosType", tag);
    hash.put("pageId", curPageId);
    Api.config("/videos", hash).getRequest(getActivity(), new HttpCallBack() {
      @Override
      public void onSuccess(String res) {
        List<VideoEntity> curVideos = JSON.parseArray(res, VideoEntity.class);
        if(!down) videoEntitySet.getVideoList().clear();
        for(VideoEntity e : curVideos)
          videoEntitySet.getVideoList().add(e);
        Message msg = new Message();
        if(!down) msg.what = SET_VIDEO_ADAPTER;
        else msg.what = SET_VIDEO_ADAPTER_AND_SET_POS;
        handler.sendMessage(msg);
      }
      @Override
      public void onFailure(Exception e) {
        e.printStackTrace();
      }
    });
  }

}

