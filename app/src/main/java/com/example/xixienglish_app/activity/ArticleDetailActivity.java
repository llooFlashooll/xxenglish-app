package com.example.xixienglish_app.activity;



import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.ArticleFragmentItemAdapter;
import com.example.xixienglish_app.adapter.CommentItemAdapter;
import com.example.xixienglish_app.animation.CircleTransform;
import com.example.xixienglish_app.animation.RoundedCornersTransformation;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;


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
    recyclerView.setAdapter(new CommentItemAdapter(this));
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
}
