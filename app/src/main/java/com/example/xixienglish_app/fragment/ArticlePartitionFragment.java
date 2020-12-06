package com.example.xixienglish_app.fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.ArticleFragmentItemAdapter;
import com.example.xixienglish_app.adapter.CommentItemAdapter;
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
    private BaseFragment thisFragment = this;
    public static final int SET_ADAPTER = 0x1;
    public static final int SET_ADAPTER_AND_SET_POS = 0x2;


    /**
     * http请求的线程中不能setAdapter，移到handler中做
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_ADAPTER:
                    recyclerView.setAdapter(new ArticleFragmentItemAdapter(getActivity(), articleEntitySet.getNewsList(), thisFragment));
                    break;
                case SET_ADAPTER_AND_SET_POS:
                    recyclerView.scrollToPosition((curPageId - 1) * 20);
                    recyclerView.setAdapter(new ArticleFragmentItemAdapter(getActivity(), articleEntitySet.getNewsList(), thisFragment));
            }
        }
    };

    public ArticlePartitionFragment(ArticleEntitySet articleEntitySet) {
        this.articleEntitySet = articleEntitySet;
    }


    @Override
    protected int initLayout() {
        return R.layout.fragment_article_partition;
    }

    @Override
    protected void initView() {
        recyclerView = mRootView.findViewById(R.id.rv);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
    }

    @Override
    protected void initData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ArticleFragmentItemAdapter(getActivity(), articleEntitySet.getNewsList(), this));

        // 下拉刷新(即向上滑)
        refreshLayout.setOnRefreshListener(layout -> {
            loadArticleList(false);
            layout.finishRefresh();
        });
        // 上拉加载(即向下滑)
        refreshLayout.setOnLoadMoreListener(layout -> {
            loadArticleList(true);
            layout.finishLoadMore();
        });
    }

    /**
     * @param down 是否向下滑
     */
    private void loadArticleList(boolean down) {
        curPageId = down ? curPageId + 1 : Math.max(curPageId - 1, 1);
        String tag = articleEntitySet.getTag();
        HashMap<String, Object> hash = new HashMap<>();
        hash.put("newsType", tag);
        hash.put("pageId", curPageId);
        Api.config("/news", hash).getRequest(getActivity(), new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                List<ArticleEntity> curArticles = JSON.parseArray(res, ArticleEntity.class);
                if (!down) articleEntitySet.getNewsList().clear();
                for (ArticleEntity e : curArticles)
                    articleEntitySet.getNewsList().add(e);
                Message msg = new Message();
                if (!down) msg.what = SET_ADAPTER;
                else msg.what = SET_ADAPTER_AND_SET_POS;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

}
