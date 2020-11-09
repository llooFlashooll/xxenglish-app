package com.example.xixienglish_app.api;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Api {
    private static OkHttpClient client;
    private static String requestUrl;
    private static HashMap<String, Object> mParams;

    public static Api api = new Api();

    public Api() {

    }

    public static Api config(String url, HashMap<String, Object> params) {
        client = new OkHttpClient.Builder()
                .build();
        requestUrl = ApiConfig.BASE_URl + url;
        mParams = params;
        return api;
    }

    public void postRequest(final HttpCallBack callBack){
        /*SharedPreferences sp = context.getSharedPreferences("sp_ttit", MODE_PRIVATE);
        String token = sp.getString("token", "");*/

        // 将参数转换为json格式
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        // 创建request, 将json发送到远端
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("contentType", "application/json;charset=UTF-8")
//                .addHeader("token", token)
                .post(requestBodyJson)
                .build();

        // 创建call回调对象
        final Call call = client.newCall(request);
        // 发起请求
        call.enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
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
