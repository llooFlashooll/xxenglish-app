package com.example.xixienglish_app.adapter;

import android.content.Context;
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
import com.example.xixienglish_app.activity.VideoDetailActivity;
import com.example.xixienglish_app.animation.RoundedCornersTransformation;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntity;
import com.example.xixienglish_app.entity.VideoEntity;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * zzy用于写点赞/评论/收藏页面的Adapter
 * 点赞/收藏/评论接口会把文章和视频混在一起返回
 * 文章的tag不带v，视频的tag带v
 *
 * 该Adapter集成了ArticleFragmentItemAdapter和VideoFragmentItemAdapter
 */
public class ArticleVideoItemAdapter extends RecyclerView.Adapter<ArticleVideoItemAdapter.ViewHolder> {

    private Context context;
    private List<ArticleEntity> list;
    private List<VideoEntity> videoEntityList;
    // 增加activity，用于绑定 recyclerview 所在的activity，用于页面跳转
    private BaseActivity parentActivity;

    // 传参时判断是文章还是视频，0为文章，1为视频
    private int tag;


    public ArticleVideoItemAdapter(Context context, List<ArticleEntity> list, BaseActivity parentActivity, int tag){
        this.context = context;
        this.list = list;
        this.parentActivity = parentActivity;
        this.tag = tag;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (tag == 0) {
            return new ArticleVideoItemAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_article_item,parent,false));
        }
        return new ArticleVideoItemAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_video_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleVideoItemAdapter.ViewHolder holder, int position) {
        if(position < 0 || position >= list.size())
            throw new RuntimeException("postion越界");

        ArticleEntity e = list.get(position);
        holder.title.setText(e.getTitle());
        // 若为视频
        if (String.valueOf(e.getTag().charAt(0)).equals("v")) {
            holder.likes.setText("点赞: " + e.getLikes());
            holder.reviewNum.setText("评论: " + e.getReviewNum());
            holder.favorites.setText("收藏: " + e.getFavorites());

            final Transformation transformation = new RoundedCornersTransformation(20, 10);
            // 设置宽度为无穷大，自动填充父结点
            Picasso.get().load(e.getImage()).resize(1200, 0)
                    .transform(transformation).into(holder.image);

            holder.wrapper.setOnClickListener(v -> {
                Map<String, String> params = new HashMap<String, String>() {{
                    put("content", e.getContent());
                    put("newsId", e.getNewsId());
                }};
                parentActivity.navigateToWithParams(VideoDetailActivity.class, params);
            });

            // 获取下一个item的 tag
            if((position+1) >= list.size()) {
                return;
            }
            ArticleEntity tmp = list.get(position + 1);
            if (String.valueOf(tmp.getTag().charAt(0)).equals("v")) {
                tag = 1;
            }
            else {
                tag = 0;
            }

        }
        // 若为文章
        else {
            holder.summary.setText(e.getSummary());
            holder.likes.setText(String.valueOf(e.getLikes()));
            holder.reviewNum.setText(String.valueOf(e.getReviewNum()));
            holder.favorites.setText("收藏: " + e.getFavorites());
            holder.tag.setText(e.getTag());

            Picasso.get().load(e.getImage()).into(holder.image);
            holder.wrapper.setOnClickListener(v->{
                HashMap<String, Object> params = new HashMap<String, Object>(){{put("newsId", e.getNewsId());}};
                Api.config("/news/specific", params).getRequest(parentActivity, new HttpCallBack() {
                    @Override
                    public void onSuccess(String res) {
                        ArticleEntity specifcArticle = JSON.parseObject(res, ArticleEntity.class);
                        Map<String, String> hash = new HashMap<>();
                        hash.put("title", specifcArticle.getTitle());
                        hash.put("image", specifcArticle.getImage());
                        hash.put("content", specifcArticle.getContent());
                        hash.put("newsId", specifcArticle.getNewsId());
                        parentActivity.navigateToWithParams(ArticleDetailActivity.class, hash);
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });

            });

            // 获取下一个item的 tag
            if((position+1) >= list.size()) {
                return;
            }
            System.out.println(position);
            ArticleEntity tmp = list.get(position + 1);
            if (String.valueOf(tmp.getTag().charAt(0)).equals("v")) {
                tag = 1;
            }
            else {
                tag = 0;
            }
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // 集成文章和视频板块的 viewHolder
    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView title;
        private TextView summary;
        private TextView likes;
        private TextView reviewNum;
        private TextView favorites;
        private ImageView image;
        private TextView tag;
        private LinearLayout wrapper;
        public ViewHolder(View v){
            super(v);
            title = v.findViewById(R.id.title);
            summary = v.findViewById(R.id.summary);
            likes = v.findViewById(R.id.likes);
            image = v.findViewById(R.id.image);
            reviewNum = v.findViewById(R.id.review_num);
            tag = v.findViewById(R.id.tag);
            favorites = v.findViewById(R.id.favorites);
            wrapper = v.findViewById(R.id.wrapper);
        }
    }
}
