package com.example.xixienglish_app.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.ArticleFragmentItemAdapter;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntity;
import com.example.xixienglish_app.entity.ArticleEntitySet;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.HashMap;
import java.util.List;

/**
 * Article页面下具体的某一tag，比如商业/科技/体育
 */
public class ArticlePartitionFragment extends BaseFragment {

    protected RecyclerView recyclerView;
    protected RefreshLayout refreshLayout;
    private ArticleEntitySet articleEntitySet;
    private int curPageId = 1;
    // 同步锁
    boolean hasLoaded = false;

    public  ArticlePartitionFragment(ArticleEntitySet articleEntitySet){
      this.articleEntitySet = articleEntitySet;
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_article_partition;
    }

    @Override
    protected void initView() {
        recyclerView= mRootView.findViewById(R.id.rv);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ArticleFragmentItemAdapter(getActivity(), articleEntitySet.getNewsList(), this));

          // 下拉刷新(即向上滑)
          refreshLayout.setOnRefreshListener(layout -> {
            loadArticleList(false);
            while (!hasLoaded) {
              try {
                Thread.sleep(50);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
            recyclerView.setAdapter(new ArticleFragmentItemAdapter(getActivity(), articleEntitySet.getNewsList(), this));
            layout.finishRefresh();
            hasLoaded = false;
          });
          // 上拉加载(即向下滑)
          refreshLayout.setOnLoadMoreListener(layout -> {
            loadArticleList(true);
            while (!hasLoaded) {
              try {
                Thread.sleep(50);
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            }
            recyclerView.setAdapter(new ArticleFragmentItemAdapter(getActivity(), articleEntitySet.getNewsList(), this));
            recyclerView.scrollToPosition((curPageId - 1) * 20);
            layout.finishLoadMore();
            hasLoaded = false;
          });
    }

  /**
   *
   * @param down 是否向下滑
   */
    private void loadArticleList(boolean down){
      curPageId = down ? curPageId + 1 : Math.max(curPageId - 1, 1);
      String tag = articleEntitySet.getTag();
      HashMap<String, Object> hash = new HashMap<>();
      hash.put("newsType", tag);
      hash.put("pageId", curPageId);
      Api.config("/news", hash).getRequest(getActivity(), new HttpCallBack() {
        @Override
        public void onSuccess(String res) {
          List<ArticleEntity> curArticles = JSON.parseArray(res, ArticleEntity.class);
          if(!down) articleEntitySet.getNewsList().clear();
          for(ArticleEntity e : curArticles)
            articleEntitySet.getNewsList().add(e);
          hasLoaded = true;
        }
        @Override
        public void onFailure(Exception e) {
          e.printStackTrace();
        }
      });
    }
}
