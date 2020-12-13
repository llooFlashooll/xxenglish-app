package com.example.xixienglish_app.activity;

import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.InformationResponse;
import com.example.xixienglish_app.entity.TripletEntity;
import com.example.xixienglish_app.util.XToastUtils;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ModifyInformationActivity extends BaseActivity {

    private EditText inputText;
    private TextView title;
    private BaseActivity thisActivity = this;

    @Override
    protected int initLayout() {
        return R.layout.activity_modify_information;
    }

    @Override
    protected void initView() {
        inputText = findViewById(R.id.input_text);
        title = findViewById(R.id.title);
    }

    @Override
    protected void initData() {

    }

    /**
     * 取消按钮事件触发
     * @param v
     */
    public void onCancelClick(View v) {
        finish();
    }

    /**
     * 确认按钮事件触发
     * @param v
     */
    public void onPostClick (View v) {
        String content = inputText.getText().toString();
        String modify = getNavigationParams("modify");
        String token = getValueFromSp("token");

        HashMap<String, Object> bodyInfo = new HashMap<String, Object> () {{
            put("field", modify);
            put("value", content);
        }};

        // 将参数转换为json数组方式
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        try {
            jsonObject.put("field", modify);
            jsonObject.put("value", content);
            jsonArray.put(0, jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonArray.toString());


        HttpCallBack callBack = new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                Log.e("onSuccess", res);
//              showToastSync(res);
                Gson gson = new Gson();
                TripletEntity tripletEntity = gson.fromJson(res, TripletEntity.class);
                if (tripletEntity.getCode() == 200) {
                    Looper.prepare();
                    XToastUtils.toast(tripletEntity.getMsg());
                    navigateTo(MainActivity.class);
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

            }
        };

        // 创建request, 将json发送到远端
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url("http://139.196.153.21:8888/modifyMessage")
                .addHeader("contentType", "application/json;charset=UTF-8")
                .addHeader("token", token)
                .post(body)
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