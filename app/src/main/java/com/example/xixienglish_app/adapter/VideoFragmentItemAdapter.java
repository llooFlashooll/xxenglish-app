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

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class VideoFragmentItemAdapter extends RecyclerView.Adapter<VideoFragmentItemAdapter.ViewHolder> {
    private Context context;
    private List<VideoEntity> list;
    private BaseFragment parent;

    /**
     * @param context
     * @param list    传入的列表项
     * @param parent  recyclerview所在的fragment，用于页面跳转
     */
    public VideoFragmentItemAdapter(Context context, List<VideoEntity> list, BaseFragment parent) {
        this.context = context;
        this.list = list;
        this.parent = parent;
    }

    @NonNull
    @Override
    public VideoFragmentItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VideoFragmentItemAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_video_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VideoFragmentItemAdapter.ViewHolder holder, int position) {
        if (position < 0 || position >= list.size())
            throw new RuntimeException("postion越界");
        VideoEntity e = list.get(position);
        holder.title.setText(e.getTitle());
        holder.likes.setText("点赞: " + e.getLikes());
        holder.comment.setText("评论: " + e.getComment());
        // todo: 为了便于调试暂时把收藏的部分写作页码，第3次迭代换回来
        // holder.collect.setText("收藏: " + e.getCollection());
        holder.collect.setText("页码: " + position);

        final Transformation transformation = new RoundedCornersTransformation(20, 10);
        // 设置宽度为无穷大，自动填充父结点
        Picasso.get().load(e.getImage()).resize(1200, 0)
            .transform(transformation).into(holder.image);

        holder.wrapper.setOnClickListener(v -> {
            BaseActivity activity = (BaseActivity) parent.getActivity();
            Map<String, String> params = new HashMap<String, String>() {{
                put("content", e.getContent());
                put("newsId", e.getNewsId());
            }};
            activity.navigateToWithParams(VideoDetailActivity.class, params);
        });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout wrapper;
        private ImageView image;
        private TextView title;
        private TextView likes;
        private TextView comment;
        private TextView collect;

        public ViewHolder(View v) {
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
