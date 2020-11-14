package com.example.xixienglish_app.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.entity.ArticleEntity;
import com.example.xixienglish_app.fragment.ArticleFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Article fragment某一头部分页下列表中的基本单元项
 */
public class ArticleFragmentItemAdapter extends RecyclerView.Adapter<ArticleFragmentItemAdapter.ViewHolder> {

    private Context context;
    private List<ArticleEntity> list;

    public ArticleFragmentItemAdapter(Context context, List<ArticleEntity> list){
        this.context = context;
        this.list = list;
    }
    // todo: 该构造器仅用于接入后端前测试，接入后删除
    public ArticleFragmentItemAdapter(Context context){
        this.context = context;
        list = new ArrayList<>();
        for(int i = 0; i < 30; i++)
            list.add(new ArticleEntity());
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
        holder.read.setText("阅读量: " + String.valueOf(e.getRead()));
        holder.tag.setText(e.getTag());
        Picasso.get().load(e.getImage()).into(holder.image);
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
        public ViewHolder(View v){
            super(v);
            title = v.findViewById(R.id.title);
            summary = v.findViewById(R.id.summary);
            likes = v.findViewById(R.id.likes);
            image = v.findViewById(R.id.image);
            comment = v.findViewById(R.id.comment);
            read = v.findViewById(R.id.read);
            tag = v.findViewById(R.id.tag);
        }
    }
}
