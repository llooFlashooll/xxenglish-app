package com.example.xixienglish_app.fragment;


import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.activity.InputCommentActivity;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.LoginResponse;
import com.example.xixienglish_app.entity.TripletEntity;
import com.example.xixienglish_app.util.XToastUtils;
import com.google.gson.Gson;

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
        HashMap<String, Object> bodyInfo = new HashMap<String, Object> () {{
            put("newsId", newsId);
        }};

        Api.config("/add/favorites", bodyInfo).postRequestWithToken(parentActivity, new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
//                showToastSync(res);
                Gson gson = new Gson();
                TripletEntity tripletEntity = gson.fromJson(res, TripletEntity.class);
                if (tripletEntity.getCode() == 200) {
                    Looper.prepare();
                    XToastUtils.toast(tripletEntity.getMsg());
                    Looper.loop();
                }
                else {
                    Looper.prepare();
                    XToastUtils.toast(tripletEntity.getMsg());
                    Looper.loop();
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 点赞按钮
     * @param v
     */
    public void onLikeClick(View v) {
        HashMap<String, Object> bodyInfo = new HashMap<String, Object> () {{
            put("newsId", newsId);
        }};

        Api.config("/news/likes", bodyInfo).postRequestWithToken(parentActivity, new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
//                showToastSync(res);
                Gson gson = new Gson();
                TripletEntity tripletEntity = gson.fromJson(res, TripletEntity.class);
                if (tripletEntity.getCode() == 200) {
                    Looper.prepare();
                    XToastUtils.toast(tripletEntity.getMsg());
                    Looper.loop();
                }
                else {
                    Looper.prepare();
                    XToastUtils.toast(tripletEntity.getMsg());
                    Looper.loop();
                }
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }
}