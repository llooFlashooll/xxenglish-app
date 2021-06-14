package com.example.xixienglish_app.activity;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.CommentItemAdapter;
import com.example.xixienglish_app.animation.RoundedCornersTransformation;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntitySet;
import com.example.xixienglish_app.entity.CommentEntity;
import com.example.xixienglish_app.entity.LoginResponse;
import com.example.xixienglish_app.entity.TranslationResponse;
import com.example.xixienglish_app.entity.TripletEntity;
import com.example.xixienglish_app.fragment.ArticlePartitionFragment;
import com.example.xixienglish_app.fragment.CommentFragment;
import com.example.xixienglish_app.fragment.TripletFragment;
import com.example.xixienglish_app.util.StringUtils;
import com.example.xixienglish_app.util.XToastUtils;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;
import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.popupwindow.bar.CookieBar;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.xuexiang.xui.XUI.getContext;


/**
 * 文章详情页
 */
public class ArticleDetailActivity extends BaseActivity {

    private TextView title;
    private TextView content;
    private ImageView image;
    private XUIPopup mNormalPopup;
    private Activity mActivity = this;
    public static final int TRANS = 0x1;
    private String english;
    private String chinese;

    @Override
    protected int initLayout() {
        return R.layout.activity_article_detail;
    }

    @Override
    protected void initView() {
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        image = findViewById(R.id.image);
    }

    @Override
    protected void initData() {
        title.setText(getNavigationParams("title"), TextView.BufferType.SPANNABLE);
        content.setText(getNavigationParams("content"), TextView.BufferType.SPANNABLE);

        getEachWord(title);
        title.setMovementMethod(LinkMovementMethod.getInstance());
        getEachWord(content);
        content.setMovementMethod(LinkMovementMethod.getInstance());


        // 添加底部三连区和评论区
        TripletFragment tripletFragment = new TripletFragment(getNavigationParams("newsId"), this);
        CommentFragment commentFragment = new CommentFragment(getNavigationParams("newsId"), this);
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.triplet_fragment, tripletFragment)
            .add(R.id.comment_fragment, commentFragment)
            .commit();
        // 阅读数量增加
        plusCnt("reading");
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // 待WindowFocusChanged后才能够getWidth，因此图片加载没有写在initData中
        super.onWindowFocusChanged(hasFocus);
        final Transformation transformation = new RoundedCornersTransformation(20, 10);
        Picasso.get().load(getNavigationParams("image")).resize((int) (image.getWidth()), 0)
            .transform(transformation).into(image);
    }


    /**
     * 回退按钮
     */
    public void onBackClick(View v) {
        finish();
    }

    /*
    **********       以下为分词并响应逻辑       **********
     */

    /**
     * http请求的线程中不能setAdapter，移到handler中做
     */
    protected Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case TRANS:
                    chinese = (String)msg.obj;
                    /**
                     * **********       以下接入弹出窗逻辑        **********
                     */
                    CookieBar.builder(mActivity)
                            .setTitle(english)
                            .setMessage(chinese)
                            .setDuration(3000)
                            .setBackgroundColor(R.color.colorPrimaryDark)
                            .setActionColor(android.R.color.white)
                            .setTitleColor(android.R.color.white)
                            .setAction("加入生词本", view -> {

                                addToWordBook(english, chinese);
                            })
                            .show();

                    showToast("翻译" + english);
                    break;
            }
        }

    };

    public void getEachWord(TextView textView){
        Spannable spans = (Spannable)textView.getText();

        String content = textView.getText().toString().trim().replaceAll("[\\n\\r]", " ");

        Integer[] indices = getIndices(
                content, ' ');
        int start = 0;
        int end = 0;
        // to cater last/only word loop will run equal to the length of indices.length
        for (int i = 0; i <= indices.length; i++) {
            ClickableSpan clickSpan = getClickableSpan();

            // to cater last/only word，适应最后的单词
            end = (i < indices.length ? indices[i] : spans.length());
            spans.setSpan(clickSpan, start, end,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            start = end + 1;
        }
        // 改变选中文本的高亮颜色
        textView.setHighlightColor(Color.BLUE);
    }

    private ClickableSpan getClickableSpan(){
        return new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView tv = (TextView) widget;
                String s = tv
                        .getText()
                        .subSequence(tv.getSelectionStart(),
                                tv.getSelectionEnd()).toString();
                Log.d("tapped on:", s);
                english = s;

                /**
                 * 翻译逻辑
                 */
                getTranslation(s);

            }
            @Override
            public void updateDrawState(TextPaint ds) {
//                ds.setColor(Color.BLACK);
                ds.setUnderlineText(false);
            }
        };
    }

    public static Integer[] getIndices(String s, char c) {
        int pos = s.indexOf(c, 0);
        List<Integer> indices = new ArrayList<Integer>();
        while (pos != -1) {
            indices.add(pos);
            pos = s.indexOf(c, pos + 1);
        }
        return (Integer[]) indices.toArray(new Integer[0]);
    }

    public void getTranslation(String word) {

        /**
         * MD5加密
         */
        String mix = "20201210000643977" + word + "123456" + "k7nyCImNOwOosUeGvYzU";
        byte[] md5 = new byte[0];
        try {
            md5 = MessageDigest.getInstance("MD5").digest(mix.getBytes());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        StringBuilder hex = new StringBuilder(md5.length * 2);
        for (byte b: md5) {
            if ((b & 0xFF) < 0x10) hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        String sign = hex.toString();


        /**
         * 此处重写回调方法
         */
        HttpCallBack callBack = new HttpCallBack() {

            @Override
            public void onSuccess(String res) {
                Log.e("onSuccess", res);
//                showToastSync(res);

                Gson gson = new Gson();
                TranslationResponse translationResponse = gson.fromJson(res, TranslationResponse.class);
                // 取出translationResult中的译文
                String translation = translationResponse.getTrans_result().get(0).getDst();
                Message msg = handler.obtainMessage();
                msg.what = TRANS;
                msg.obj = translation;
                handler.sendMessage(msg);

            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        HashMap<String, Object> hash = new HashMap<>();

        hash.put("q", word);
        hash.put("from", "en");
        hash.put("to", "zh");
        hash.put("appid", "20201210000643977");
        hash.put("salt", "123456");
        hash.put("sign", sign);

        OkHttpClient client = new OkHttpClient.Builder().build();
        String url = "http://api.fanyi.baidu.com/api/trans/vip/translate";
        String requestUrl = getAppendUrl(url, hash);
        Request request = new Request.Builder()
                .url(requestUrl)
                .get()              // 请求方法是get
                .build();
        Call call = client.newCall(request);


        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.d("onSuccess","---Success---");
                callBack.onSuccess(result);
            }
        });

    }


    // 拼接url，用于带params发送get请求
    private String getAppendUrl(String url, Map<String, Object> map) {
        if (map != null && !map.isEmpty()) {
            StringBuffer buffer = new StringBuffer();
            Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> entry = iterator.next();
                if (StringUtils.isEmpty(buffer.toString())) {
                    buffer.append("?");
                } else {
                    buffer.append("&");
                }
                buffer.append(entry.getKey()).append("=").append(entry.getValue());
            }
            url += buffer.toString();
        }
        return url;
    }

    /**
     * 加入生词本逻辑
     */
    public void addToWordBook(String englishword, String chineseword) {
        DialogLoader.getInstance().showConfirmDialog(
            mContext,
            "是否确认加入生词本?",
            "是",
            (dialog, which) -> {
                dialog.dismiss();
                HashMap<String, Object> bodyInfo = new HashMap<String, Object> () {{
                    put("english", englishword);
                    put("chinese",chineseword);
                }};
                System.out.println(bodyInfo);
                Api.config("/add/glossary", bodyInfo).postRequestWithToken(mActivity, new HttpCallBack() {
                    @Override
                    public void onSuccess(String res) {
//                        showToastSync(res);

                        Gson gson = new Gson();
                        TripletEntity tripletEntity = gson.fromJson(res, TripletEntity.class);
                        if (tripletEntity.getCode() == 200) {
                            Looper.prepare();
                            XToastUtils.toast(tripletEntity.getMsg());
                            Looper.loop();
                        }
                        else {
                            Looper.prepare();
                            XToastUtils.toast(tripletEntity.getMsg());
                            Looper.loop();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }
                });

            },
            "否",
            (dialog, which) -> dialog.dismiss()
            );
    }
}
