package com.example.xixienglish_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.ArticleDetailActivity;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.activity.InitActivity;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntity;
import com.example.xixienglish_app.entity.LoginResponse;
import com.example.xixienglish_app.fragment.BaseFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Article fragment某一头部分页下列表中的基本单元项
 */
public class ArticleFragmentItemAdapter extends RecyclerView.Adapter<ArticleFragmentItemAdapter.ViewHolder> {

    private Context context;
    private List<ArticleEntity> list;
    private BaseFragment parent;
    // 增加activity，用于绑定 recyclerview 所在的activity，用于页面跳转
    private BaseActivity parentActivity;

    /**
     * @param context
     * @param list    传入的列表项
     * @param parent  recyclerview所在的fragment，用于页面跳转
     */
    public ArticleFragmentItemAdapter(Context context, List<ArticleEntity> list, BaseFragment parent) {
        this.context = context;
        this.list = list;
        this.parent = parent;
    }

    public ArticleFragmentItemAdapter(Context context, List<ArticleEntity> list, BaseActivity parentActivity) {
        this.context = context;
        this.list = list;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_article_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position < 0 || position >= list.size())
            throw new RuntimeException("postion越界");
        ArticleEntity e = list.get(position);
        holder.title.setText(e.getTitle());
        holder.summary.setText(e.getSummary());
        holder.likes.setText(String.valueOf(e.getLikes()));
        holder.reviewNum.setText(String.valueOf(e.getReviewNum()));
        holder.deleteBtn.setText("删除这条新闻");
        holder.favorites.setText("收藏: " + e.getFavorites());
        holder.tag.setText(e.getTag());
        Picasso.get().load(e.getImage()).into(holder.image);
        holder.wrapper.setOnClickListener(v -> {
            HashMap<String, Object> params = new HashMap<String, Object>() {{
                put("newsId", e.getNewsId());
            }};
            Api.config("/news/specific", params).getRequest(parent.getActivity(), new HttpCallBack() {
                @Override
                public void onSuccess(String res) {
                    ArticleEntity specifcArticle = JSON.parseObject(res, ArticleEntity.class);
                    Map<String, String> hash = new HashMap<>();
                    hash.put("title", specifcArticle.getTitle());
                    hash.put("image", specifcArticle.getImage());
                    hash.put("content", specifcArticle.getContent());
                    hash.put("newsId", specifcArticle.getNewsId());

                    BaseActivity activity = (BaseActivity) parent.getActivity();
                    activity.navigateToWithParams(ArticleDetailActivity.class, hash);
                }

                @Override
                public void onFailure(Exception e) {

                }
            });

        });
        holder.deleteBtn.setOnClickListener(v -> {
            HashMap<String, Object> params = new HashMap<String, Object>() {{
                put("newsId", e.getNewsId());
            }};
            Api.config("/deleteNews", params).postRequestInParams(context, new HttpCallBack() {
                @Override
                public void onSuccess(String res) {
                    Gson gson = new Gson();
                    LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                    String msg = (loginResponse.getCode() == 200 ? "删除成功": "啊欧，后台出现了点问题，删除失败");
                    Looper.prepare();
                    parent.showToast(msg);
                    Looper.loop();
                }

                @Override
                public void onFailure(Exception e) {
                    Looper.prepare();
                    parent.showToast("服务器超时");
                    Looper.loop();
                    e.printStackTrace();
                }
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView summary;
        private TextView likes;
        private TextView reviewNum;
        private TextView favorites;
        private TextView deleteBtn;
        private ImageView image;
        private TextView tag;
        private LinearLayout wrapper;

        public ViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.title);
            summary = v.findViewById(R.id.summary);
            likes = v.findViewById(R.id.likes);
            reviewNum = v.findViewById(R.id.review_num);
            favorites = v.findViewById(R.id.favorites);
            deleteBtn = v.findViewById(R.id.delete_btn);
            if (parent.getValueFromSp("isAdmin") == null || !parent.getValueFromSp("isAdmin").equals("true")) {
                v.findViewById(R.id.delete_icon).setVisibility(View.INVISIBLE);
                deleteBtn.setVisibility(View.INVISIBLE);
            } else {
                v.findViewById(R.id.delete_icon).setVisibility(View.VISIBLE);
                deleteBtn.setVisibility(View.VISIBLE);
            }

            image = v.findViewById(R.id.image);
            tag = v.findViewById(R.id.tag);
            wrapper = v.findViewById(R.id.wrapper);
        }
    }
}
