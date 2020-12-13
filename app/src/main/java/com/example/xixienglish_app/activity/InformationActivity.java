package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dueeeke.videoplayer.util.L;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.ArticleVideoItemAdapter;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntity;
import com.example.xixienglish_app.entity.InformationEntity;
import com.example.xixienglish_app.entity.InformationResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InformationActivity extends BaseActivity {

    private TextView tv_account;
    private TextView tv_name;
    private TextView tv_city;
    private TextView tv_email;
    private TextView tv_gender;
    private TextView tv_auth;
    private TextView tv_phone;
    private ImageView iv_modifyName;
    private ImageView iv_modifyEmail;
    private ImageView iv_modifyGender;
    private ImageView iv_modifyCity;
    private ImageView iv_modifyPhone;
    private BaseActivity thisActivity = this;
    InformationResponse informationResponse;
    public static final int SET_ADAPTER = 0x1;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_ADAPTER:
                    // 更新信息
                    tv_account.setText(informationResponse.getData().getAccount());
                    tv_name.setText(informationResponse.getData().getName());
                    tv_city.setText(informationResponse.getData().getCity());
                    tv_email.setText(informationResponse.getData().getEmail());
                    tv_gender.setText(informationResponse.getData().getGender());
                    tv_auth.setText(informationResponse.getData().getIdentify());
                    tv_phone.setText(informationResponse.getData().getPhoneNumber());
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_information;
    }

    @Override
    protected void initView() {
        tv_account = findViewById(R.id.tv_account);
        tv_name = findViewById(R.id.tv_name);
        tv_city = findViewById(R.id.tv_city);
        tv_email = findViewById(R.id.tv_email);
        tv_gender = findViewById(R.id.tv_gender);
        tv_auth = findViewById(R.id.tv_auth);
        tv_phone = findViewById(R.id.tv_phone);
        iv_modifyName = findViewById(R.id.iv_modifyName);
        iv_modifyEmail = findViewById(R.id.iv_modifyEmail);
        iv_modifyGender = findViewById(R.id.iv_modifyGender);
        iv_modifyCity = findViewById(R.id.iv_modifyCity);
        iv_modifyPhone = findViewById(R.id.iv_modifyPhone);
        iv_modifyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>() {{
                    put("modify", "name");
                }};
                thisActivity.navigateToWithParams(ModifyInformationActivity.class, params);
            }
        });
        iv_modifyEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>() {{
                    put("modify", "email");
                }};
                thisActivity.navigateToWithParams(ModifyInformationActivity.class, params);
            }
        });
        iv_modifyGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>() {{
                    put("modify", "gender");
                }};
                thisActivity.navigateToWithParams(ModifyInformationActivity.class, params);
            }
        });
        iv_modifyCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>() {{
                    put("modify", "city");
                }};
                thisActivity.navigateToWithParams(ModifyInformationActivity.class, params);
            }
        });
        iv_modifyPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<String, String>() {{
                    put("modify", "phone");
                }};
                thisActivity.navigateToWithParams(ModifyInformationActivity.class, params);
            }
        });
    }

    @Override
    protected void initData() {
        String token = getValueFromSp("token");
        if (!token.equals("")) {
            HttpCallBack callBack = new HttpCallBack() {
                @Override
                public void onSuccess(String res) {
                    Log.e("onSuccess", res);
//                    showToastSync(res);
                    Gson gson = new Gson();
                    informationResponse = gson.fromJson(res, InformationResponse.class);

                    Message msg = new Message();
                    msg.what = SET_ADAPTER;
                    handler.sendMessage(msg);
                }

                @Override
                public void onFailure(Exception e) {

                }
            };

            OkHttpClient client = new OkHttpClient.Builder().build();
            String url = "http://139.196.153.21:8888/personalMessage";
            Request request = new Request.Builder()
                    .url(url)
                    .get()              // 请求方法是get
                    .addHeader("token", token)
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

    }

    public void onBackClick(View v) {finish();}

}