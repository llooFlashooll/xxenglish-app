package com.example.xixienglish_app.activity;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.CommentItemAdapter;
import com.example.xixienglish_app.animation.RoundedCornersTransformation;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.CommentEntity;
import com.example.xixienglish_app.fragment.CommentFragment;
import com.example.xixienglish_app.fragment.TripletFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 文章详情页
 */
public class ArticleDetailActivity extends BaseActivity {

    private TextView title;
    private TextView content;
    private ImageView image;


    @Override
    protected int initLayout() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        image = findViewById(R.id.image);
    }

    @Override
    protected void initData() {
        title.setText(getNavigationParams("title"));
        content.setText(getNavigationParams("content"));
        // 添加底部三连区和评论区
        TripletFragment tripletFragment = new TripletFragment(getNavigationParams("newsId"), this);
        CommentFragment commentFragment = new CommentFragment(getNavigationParams("newsId"), this);
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.triplet_fragment, tripletFragment)
            .add(R.id.comment_fragment, commentFragment)
            .commit();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // 待WindowFocusChanged后才能够getWidth，因此图片加载没有写在initData中
        super.onWindowFocusChanged(hasFocus);
        final Transformation transformation = new RoundedCornersTransformation(20, 10);
        Picasso.get().load(getNavigationParams("image")).resize((int) (image.getWidth()), 0)
            .transform(transformation).into(image);
    }


    /**
     * 回退按钮
     */
    public void onBackClick(View v) {
        finish();
    }
}
