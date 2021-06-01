package com.example.xixienglish_app.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.xixienglish_app.util.StringUtils;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

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

    /**
     * mParams写在参数里
     * @param callBack
     */
    public void postRequestInParams(Context context, final HttpCallBack callBack) {
        String url = getAppendUrl(requestUrl, mParams);
        RequestBody requestBodyJson =
            RequestBody.create(MediaType.parse("application/json;charset=utf-8"), new JsonObject().toString());
        SharedPreferences sp = context.getSharedPreferences("sp_xixienglish", MODE_PRIVATE);
        String token = sp.getString("token", "");
        Request request = new Request.Builder()
            .url(url)
            .addHeader("contentType", "application/json;charset=UTF-8")
            .addHeader("token", token)
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
                callBack.onSuccess(result);
            }
        });
    }

    /**
     * mParams写在body里
     * @param callBack
     */
    public void postRequest(final HttpCallBack callBack){


        // 将参数转换为json格式
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        // 如果有token加上token
        String token = "";
        if (mParams.containsKey("token")) {
            token = (String) mParams.get("token");
        }
        // 创建request, 将json发送到远端
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("contentType", "application/json;charset=UTF-8")
                .addHeader("token", token)
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

    // postRequest还有一种表单方式发送，此处不集成了
    // postRequest带token，要将activity传进来，获取context方法
    public void postRequestWithToken(Context context, final HttpCallBack callBack){

        SharedPreferences sp = context.getSharedPreferences("sp_xixienglish", MODE_PRIVATE);
        String token = sp.getString("token", "");

        // 将参数转换为json格式
        JSONObject jsonObject = new JSONObject(mParams);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8"), jsonStr);

        // 创建request, 将json发送到远端
        Request request = new Request.Builder()
                .url(requestUrl)
                .addHeader("contentType", "application/json;charset=UTF-8")
                .addHeader("token", token)
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

    public void getRequest(Context context, final HttpCallBack callback) {
        SharedPreferences sp = context.getSharedPreferences("sp_xixienglish", MODE_PRIVATE);
        String token = sp.getString("token", "");
        // 请求url = baseurl + 相对路径 + 参数
        String url = getAppendUrl(requestUrl, mParams);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("token", token)
                .get()              // 请求方法是get
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    callback.onSuccess(jsonObject.getString("data"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

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
}
