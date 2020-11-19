package com.example.xixienglish_app.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.ArticleFragmentItemAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

/**
 * Article页面下具体的某一tag，比如商业/科技/体育
 */
public class ArticlePartitionFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;


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
        recyclerView.setAdapter(new ArticleFragmentItemAdapter(getActivity(), this));
        // 下拉刷新
        refreshLayout.setOnRefreshListener(layout ->{
          recyclerView.setAdapter(new ArticleFragmentItemAdapter(getActivity(), this));
          layout.finishRefresh();
        });
        // 上拉加载
        refreshLayout.setOnLoadMoreListener(layout->{
          recyclerView.setAdapter(new ArticleFragmentItemAdapter(getActivity(), this));
          layout.finishLoadMore();
        });

    }
}
