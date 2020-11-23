package com.example.xixienglish_app.activity;


import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.HashMap;
import java.util.List;


/**
 * 文章详情页
 */
public class ArticleDetailActivity extends BaseActivity {

  private Button btn_like;
  private Button btn_collect;
  private Button btn_comment;
  private TextView title;
  private TextView content;
  private ImageView image;
  private RecyclerView recyclerView;
  private ImageView back;
  private List<CommentEntity> commentEntityList;
  private BaseActivity thisActivity = this;
  public static final int SET_ADAPTER = 0x1;


  /**
   * http请求的线程中不能setAdapter，移到handler中做
   */
  private Handler handler = new Handler(Looper.getMainLooper()){
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what){
        case SET_ADAPTER:
          recyclerView.setAdapter(new CommentItemAdapter(thisActivity, commentEntityList));
          break;
      }
    }
  };

  @Override
  protected int initLayout() {
    return R.layout.activity_article_detail;
  }

  @Override
  protected void initView() {
    btn_like = findViewById(R.id.btn_like);
    btn_collect = findViewById(R.id.btn_collect);
    btn_comment = findViewById(R.id.btn_comment);
    title = findViewById(R.id.title);
    content = findViewById(R.id.content);
    image = findViewById(R.id.image);
    recyclerView = findViewById(R.id.rv);
    back = findViewById(R.id.back);
  }

  @Override
  protected void initData() {
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    // 从后端请求评论列表
    getCommentList();
    title.setText(getNavigationParams("title"));
    content.setText(getNavigationParams("content"));
    // 回退键
    back.setOnClickListener(V -> this.finish());
  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    // 待WindowFocusChanged后才能够getWidth，因此图片加载没有写在initData中
    super.onWindowFocusChanged(hasFocus);
    final Transformation transformation = new RoundedCornersTransformation(20, 10);
    Picasso.get().load(getNavigationParams("image")).resize((int)(image.getWidth()), 0)
      .transform(transformation).into(image);
  }


  private void getCommentList(){
    HashMap<String, Object> params = new HashMap<>();
    params.put("newsId", getNavigationParams("newsId"));
    Api.config("/review/root", params).getRequest(this, new HttpCallBack() {
      @Override
      public void onSuccess(String res) {
        commentEntityList = JSON.parseArray(res, CommentEntity.class);
        Message msg = new Message();
        msg.what = SET_ADAPTER;
        handler.sendMessage(msg);
      }
      @Override
      public void onFailure(Exception e) {
        e.printStackTrace();
      }
    });
  }

}
