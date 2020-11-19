package com.example.xixienglish_app.activity;



import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.xixienglish_app.R;
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
  }

  @Override
  protected void initData() {

  }

  @Override
  public void onWindowFocusChanged(boolean hasFocus) {
    super.onWindowFocusChanged(hasFocus);
    title.setText(getNavigationParams("title"));
    content.setText(getNavigationParams("content"));
    final Transformation transformation = new RoundedCornersTransformation(20, 10);
    Picasso.get().load(getNavigationParams("image")).resize((int)(image.getWidth() * 0.8), 0)
      .transform(transformation).into(image);
  }
}
