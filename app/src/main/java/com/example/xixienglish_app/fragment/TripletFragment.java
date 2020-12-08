package com.example.xixienglish_app.fragment;


import android.view.View;
import android.widget.LinearLayout;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.activity.InputCommentActivity;

import java.util.HashMap;
import java.util.Map;


public class TripletFragment extends BaseFragment {

    private String newsId;
    private BaseActivity parentActivity;
    private LinearLayout likes;
    private LinearLayout comment;
    private LinearLayout collect;

    public TripletFragment(String newsId, BaseActivity parentActivity) {
        this.newsId = newsId;
        this.parentActivity = parentActivity;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_triplet;
    }

    @Override
    protected void initView() {
        likes = mRootView.findViewById(R.id.likes);
        comment = mRootView.findViewById(R.id.comment);
        collect = mRootView.findViewById(R.id.collect);
    }

    @Override
    protected void initData() {
        likes.setOnClickListener(this::onLikeClick);
        comment.setOnClickListener(this::onCommentClick);
        collect.setOnClickListener(this::onCollectClick);
    }

    /**
     * 评论按钮
     * @param v
     */
    public void onCommentClick(View v) {
        Map<String, String> params = new HashMap<String, String>() {{
            put("newsId", newsId);
        }};
        parentActivity.navigateToWithParams(InputCommentActivity.class, params);
    }

    /**
     * 收藏按钮
     * @param v
     */
    public void onCollectClick(View v) {

    }

    /**
     * 点赞按钮
     * @param v
     */
    public void onLikeClick(View v) {

    }
}