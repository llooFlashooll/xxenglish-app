package com.example.xixienglish_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.activity.VideoDetailActivity;
import com.example.xixienglish_app.animation.RoundedCornersTransformation;
import com.example.xixienglish_app.entity.VideoEntity;
import com.example.xixienglish_app.fragment.BaseFragment;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VideoFragmentItemAdapter extends RecyclerView.Adapter<VideoFragmentItemAdapter.ViewHolder>{
  private Context context;
  private List<VideoEntity> list;
  private BaseFragment parent;

  /**
   *
   * @param context
   * @param list 传入的列表项
   * @param parent recyclerview所在的fragment，用于页面跳转
   */
  public VideoFragmentItemAdapter(Context context, List<VideoEntity> list, BaseFragment parent){
    this.context = context;
    this.list = list;
    this.parent = parent;
  }
  // todo: 该构造器仅用于接入后端前测试，接入后删除
  public VideoFragmentItemAdapter(Context context, BaseFragment parent){
    this.context = context;
    list = new ArrayList<>();
    for(int i = 0; i < 2; i++)
      list.add(new VideoEntity());
    this.parent = parent;
  }

  @NonNull
  @Override
  public VideoFragmentItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new VideoFragmentItemAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_video_item,parent,false));
  }

  @Override
  public void onBindViewHolder(@NonNull VideoFragmentItemAdapter.ViewHolder holder, int position) {
    if(position < 0 || position >= list.size())
      throw new RuntimeException("postion越界");
    VideoEntity e = list.get(position);
    holder.title.setText(e.getTitle());
    holder.likes.setText("点赞: " + e.getLikes());
    holder.comment.setText("评论: " + e.getComment());
    holder.collect.setText("收藏: " + e.getCollection());
    // 在fragment中获取组件width的方法如下所示，如果直接getWidth返回的是0
    ViewTreeObserver vto = holder.image.getViewTreeObserver();
    vto.addOnGlobalLayoutListener(()->{
      final Transformation transformation = new RoundedCornersTransformation(20, 10);
      Picasso.get().load(e.getImage()).resize(holder.image.getWidth(), 0)
        .transform(transformation).into(holder.image);
    });
    holder.wrapper.setOnClickListener(v->{
      BaseActivity activity = (BaseActivity)parent.getActivity();
      Map<String, String> hash = new HashMap<>();
      hash.put("url", e.getUrl());
      activity.navigateToWithParams(VideoDetailActivity.class, hash);
    });
  }


  @Override
  public int getItemCount() {
    return list.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder{
    private LinearLayout wrapper;
    private ImageView image;
    private TextView title;
    private TextView likes;
    private TextView comment;
    private TextView collect;
    public ViewHolder(View v){
      super(v);
      wrapper = v.findViewById(R.id.wrapper);
      image = v.findViewById(R.id.image);
      title = v.findViewById(R.id.title);
      likes = v.findViewById(R.id.likes);
      comment = v.findViewById(R.id.comment);
      collect = v.findViewById(R.id.collect);
    }
  }


}
