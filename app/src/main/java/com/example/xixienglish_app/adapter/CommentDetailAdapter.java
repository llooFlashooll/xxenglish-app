package com.example.xixienglish_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.activity.InputCommentActivity;
import com.example.xixienglish_app.activity.InputNCommentActivity;
import com.example.xixienglish_app.entity.NCommentEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论桶里的评论，即2~*级评论
 */
public class CommentDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NCommentEntity> nCommentEntityList;
    private Context context;
    private BaseActivity parent;

    public CommentDetailAdapter(List<NCommentEntity> nCommentEntityList, Context context, BaseActivity parent) {
        this.nCommentEntityList = nCommentEntityList;
        this.context = context;
        this.parent = parent;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 0)
            return new NonPreVh(LayoutInflater.from(context).inflate(R.layout.fragment_second_comment,parent,false));
        return new PreVH(LayoutInflater.from(context).inflate(R.layout.fragment_n_comment,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        NCommentEntity e = nCommentEntityList.get(position);
        if (holder instanceof NonPreVh) {
            NonPreVh nonPreVh = (NonPreVh) holder;
            nonPreVh.commentContent.setText(e.getContent());
            nonPreVh.username.setText(e.getName());
            nonPreVh.wrapper.setOnClickListener(v -> onCommentClick(e));
        } else {
            PreVH preVh = (PreVH) holder;
            preVh.commentContent.setText(e.getContent());
            preVh.username.setText(e.getName());
            preVh.preCommentContent.setText(e.getPreContent());
            preVh.preUsername.setText(e.getPreName());
            preVh.wrapper.setOnClickListener(v -> onCommentClick(e));
        }
    }

    @Override
    public int getItemCount() {
        return nCommentEntityList.size();
    }


    /**
     * 用于区分返回哪一种item
     * @param  position
     * @return 2级评论(NonPre)返回0，否则返回1
     */
    @Override
    public int getItemViewType(int position) {
        NCommentEntity e = nCommentEntityList.get(position);
        return e.getPreReviewId().equals(e.getRootReviewId())? 0 : 1;
    }

    class PreVH extends RecyclerView.ViewHolder {
        private TextView preUsername;
        private TextView preCommentContent;
        private TextView username;
        private TextView commentContent;
        private LinearLayout wrapper;
        public PreVH(View v) {
            super(v);
            preUsername = v.findViewById(R.id.pre_username);
            preCommentContent = v.findViewById(R.id.pre_comment_content);
            username = v.findViewById(R.id.username);
            commentContent = v.findViewById(R.id.comment_content);
            wrapper = v.findViewById(R.id.wrapper);
        }
    }

    class NonPreVh extends RecyclerView.ViewHolder {
        private TextView username;
        private TextView commentContent;
        private LinearLayout wrapper;
        public NonPreVh(View v) {
            super(v);
            username = v.findViewById(R.id.username);
            commentContent = v.findViewById(R.id.comment_content);
            wrapper = v.findViewById(R.id.wrapper);
        }
    }

    public void onCommentClick(NCommentEntity e) {
        Map<String, String> params = new HashMap<String, String>() {{
           put("preReviewId", e.getReviewId());
           put("rootReviewId", e.getRootReviewId());
        }};
        parent.navigateToWithParams(InputNCommentActivity.class, params);
    }
}