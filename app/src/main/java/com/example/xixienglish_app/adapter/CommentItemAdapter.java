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

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.ArticleDetailActivity;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.activity.CommentDetailActivity;
import com.example.xixienglish_app.activity.InputNCommentActivity;
import com.example.xixienglish_app.entity.ArticleEntity;
import com.example.xixienglish_app.entity.CommentEntity;
import com.example.xixienglish_app.fragment.BaseFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 一级评论/根评论
 */
public class CommentItemAdapter extends RecyclerView.Adapter<CommentItemAdapter.ViewHolder>{
  private Context context;
  private List<CommentEntity> list;
  private BaseFragment parent;


  public CommentItemAdapter(Context context, List<CommentEntity> list, BaseFragment parent){
    this.context = context;
    this.list = list;
    this.parent = parent;
  }


  @NonNull
  @Override
  public CommentItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new CommentItemAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_comment_item,parent,false));
  }

  @Override
  public void onBindViewHolder(@NonNull CommentItemAdapter.ViewHolder holder, int position) {
    if(position < 0 || position >= list.size())
      throw new RuntimeException("postion越界");
    CommentEntity e = list.get(position);
    holder.username.setText(e.getName());
    holder.commentContent.setText(e.getContent());
    holder.wrapper.setOnClickListener(v -> {
        Map<String, String> params = new HashMap<String, String>(){{
           put("rootReviewId", e.getRootReviewId());
        }};
        BaseActivity baseActivity = (BaseActivity) parent.getActivity();
        baseActivity.navigateToWithParams(CommentDetailActivity.class, params);
    });
    holder.wrapper.setOnLongClickListener(v -> {
        Map<String, String> params = new HashMap<String, String>(){{
            put("preReviewId", e.getRootReviewId());
            put("rootReviewId", e.getRootReviewId());
        }};
        BaseActivity baseActivity = (BaseActivity) parent.getActivity();
        baseActivity.navigateToWithParams(InputNCommentActivity.class, params);
        return false;
    });
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder{
    private TextView username;
    private TextView commentContent;
    private LinearLayout wrapper;
    public ViewHolder(View v){
      super(v);
      username = v.findViewById(R.id.username);
      commentContent = v.findViewById(R.id.comment_content);
      wrapper = v.findViewById(R.id.wrapper);
    }
  }
}
