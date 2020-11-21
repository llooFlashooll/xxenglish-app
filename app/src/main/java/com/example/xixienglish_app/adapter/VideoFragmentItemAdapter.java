package com.example.xixienglish_app.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.entity.VideoEntity;
import com.example.xixienglish_app.fragment.BaseFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


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
    Uri uri = Uri.parse(e.getUrl());
    holder.videoView.setVideoURI(uri);
    holder.title.setText(e.getTitle());
    holder.wrapper.setOnClickListener(v->{
      MediaController mediaController = new MediaController(parent.getActivity());
      holder.videoView.setMediaController(mediaController);
      holder.videoView.start();
    });
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  class ViewHolder extends RecyclerView.ViewHolder{
    private LinearLayout wrapper;
    private VideoView videoView;
    private TextView title;
    public ViewHolder(View v){
      super(v);
      wrapper = v.findViewById(R.id.wrapper);
      videoView = v.findViewById(R.id.video_view);
      title = v.findViewById(R.id.title);
    }
  }


}
