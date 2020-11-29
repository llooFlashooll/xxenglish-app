package com.example.xixienglish_app.fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.*;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.FragmentPagerAdapter;
import com.example.xixienglish_app.animation.DepthPageTransformer;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntitySet;
import com.google.android.material.tabs.TabLayout;
import java.util.HashMap;
import java.util.List;

public class ArticleFragment extends BaseFragment {

    private ViewPager pager;
    private TabLayout tabLayout;
    protected FragmentPagerAdapter pagerAdapter;
    public static final int SET_ADAPTER = 0x1;


    /**
     * http请求的线程中不能setAdapter，移到handler中做
     */
    protected Handler handler = new Handler(Looper.getMainLooper()){
      @Override
      public void handleMessage(Message msg) {
        switch (msg.what){
          case SET_ADAPTER:
            pager.setAdapter(pagerAdapter);
            break;
        }
      }
    };



    @Override
    protected int initLayout() {
        return R.layout.article_fragment;
    }

    @Override
    protected void initView() {
        pager = mRootView.findViewById(R.id.view_pager);
        tabLayout = mRootView.findViewById(R.id.tablayout);
        pagerAdapter = new FragmentPagerAdapter(getChildFragmentManager());
    }

    @Override
    protected void initData()  {
        // tab绑定viewpager，实现tab切换触发fragment切换
        tabLayout.setupWithViewPager(pager);

        // 设置pager切换动画
        pager.setPageTransformer(true, new DepthPageTransformer());

        addFragFromBackend();

    }

    // 将从后端请求过来的所有tag装载到ViewPager中
    protected void addFragFromBackend(){
      HashMap<String, Object> hash = new HashMap<>();
      // 初始请求第一页的数据
      hash.put("pageId", 1);
      Api.config("/noTagNews", hash).getRequest(getActivity(), new HttpCallBack() {
        @Override
        public void onSuccess(String res) {
          List<ArticleEntitySet> fragArticles = JSON.parseArray(res, ArticleEntitySet.class);
          for(ArticleEntitySet cur : fragArticles)
            pagerAdapter.addFrag(new ArticlePartitionFragment(cur), cur.getTag());
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
