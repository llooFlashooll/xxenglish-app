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
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntity;
import com.example.xixienglish_app.fragment.BaseFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
   *
   * @param context
   * @param list 传入的列表项
   * @param parent recyclerview所在的fragment，用于页面跳转
   */
    public ArticleFragmentItemAdapter(Context context, List<ArticleEntity> list, BaseFragment parent){
        this.context = context;
        this.list = list;
        this.parent = parent;
    }

    public ArticleFragmentItemAdapter(Context context, List<ArticleEntity> list, BaseActivity parentActivity){
        this.context = context;
        this.list = list;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_article_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position < 0 || position >= list.size())
            throw new RuntimeException("postion越界");
        ArticleEntity e = list.get(position);
        holder.title.setText(e.getTitle());
        holder.summary.setText(e.getSummary());
        holder.likes.setText(String.valueOf(e.getLikes()));
        holder.comment.setText(String.valueOf(e.getComment()));
        // todo： 为了便于调试暂时把阅读量写作页码，第3次迭代改回来
        holder.read.setText("阅读量: " + position);
        holder.tag.setText(e.getTag());
        Picasso.get().load(e.getImage()).into(holder.image);
        holder.wrapper.setOnClickListener(v->{
          HashMap<String, Object> params = new HashMap<String, Object>(){{put("newsId", e.getNewsId());}};
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
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
      private TextView title;
      private TextView summary;
      private TextView likes;
      private TextView comment;
      private ImageView image;
      private TextView read;
      private TextView tag;
      private LinearLayout wrapper;
        public ViewHolder(View v){
            super(v);
            title = v.findViewById(R.id.title);
            summary = v.findViewById(R.id.summary);
            likes = v.findViewById(R.id.likes);
            image = v.findViewById(R.id.image);
            comment = v.findViewById(R.id.comment);
            read = v.findViewById(R.id.read);
            tag = v.findViewById(R.id.tag);
            wrapper = v.findViewById(R.id.wrapper);
        }
    }
}
