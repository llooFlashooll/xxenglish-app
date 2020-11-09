package com.example.xixienglish_app.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.ApiConfig;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.LoginResponse;
import com.example.xixienglish_app.util.StringUtils;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends BaseActivity {

    private EditText etAccount;
    private EditText etPassword;
    private Button btnRegister;
    private TextView userProtocol;
    private TextView privProtocol;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.edit_account);
        etPassword = findViewById(R.id.edit_password);
        btnRegister = findViewById(R.id.btn_register);
        userProtocol = findViewById(R.id.tv_user_protocol);
        privProtocol = findViewById(R.id.tv_privacy_protocol);
    }

    @Override
    protected void initData() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String account = etAccount.getText().toString().trim();
                String pwd = etPassword.getText().toString().trim();
                register(account, pwd);
            }
        });

        userProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("用户协议");
            }
        });

        privProtocol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("隐私政策");
            }
        });
    }

    private void register(String account, String pwd) {
        if (StringUtils.isEmpty(account)) {
            showToast("请输入账号");
            return;
        }
        if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("account", account);
        params.put("password", pwd);

        Api.config(ApiConfig.REGISTER, params).postRequest(new HttpCallBack() {

            @Override
            public void onSuccess(final String res) {
                Log.e("onSuccess", res);
                showToastSync(res);

                // Gson库封装拿token
                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getCode() == 200) {
                    String token = loginResponse.getData();
                    showToastSync("token为:" + token);
                    System.out.println("token为: " + token);
                    // 应用sharedPreference存键值对
                    insertVal("token", token);
                    // 登陆成功跳转至首页
//                    navigateTo(HomeActivity.class);
                    navigateToWithFlags(MainActivity.class,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    showToastSync("登录成功");
                } else {
                    showToastSync("登录失败");
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });

        //网络部分
        //第一步创建OKHttpClient
        OkHttpClient client = new OkHttpClient.Builder()
                .build();
        Map m = new HashMap();
        m.put("account", account);
        m.put("password", pwd);
        JSONObject jsonObject = new JSONObject(m);
        String jsonStr = jsonObject.toString();
        RequestBody requestBodyJson =
                RequestBody.create(MediaType.parse("application/json;charset=utf-8")
                        , jsonStr);
        //第三步创建Request
        Request request = new Request.Builder()
                .url("http://www.wasd003.cn:8888/register")
                .addHeader("contentType", "application/json;charset=utf-8")
                .post(requestBodyJson)
                .build();
        //第四步创建call回调对象
        final Call call = client.newCall(request);
        //第五步发起请求
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                //不在主线程执行
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast(result);
                    }
                });
            }
        });
    }
}