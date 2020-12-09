package com.example.xixienglish_app.activity;

import android.content.Intent;
import android.os.Looper;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class LoginEmailActivity extends BaseActivity {

    private EditText etEmail;
    private EditText etVerifyCode;
    private Button getVerifyCode;
    private Button btnLogin;
    private TextView otherLogin;
    private TextView tvRegister;
    private TextView userProtocol;
    private TextView privProtocol;

    @Override
    protected int initLayout() {
        return R.layout.activity_login_email;
    }

    @Override
    protected void initView() {
        etEmail = findViewById(R.id.edit_email);
        etVerifyCode = findViewById(R.id.edit_verify_code);
        getVerifyCode = findViewById(R.id.btn_get_verify_code);
        btnLogin = findViewById(R.id.btn_login);
        otherLogin = findViewById(R.id.tv_other_login);
        tvRegister = findViewById(R.id.tv_register);
        userProtocol = findViewById(R.id.tv_user_protocol);
        privProtocol = findViewById(R.id.tv_privacy_protocol);
    }

    @Override
    protected void initData() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                String verifyCode = etVerifyCode.getText().toString().trim();
                login(email, verifyCode);
            }
        });

        getVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                applyVerifyCode(email);

            }
        });

        otherLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(LoginActivity.class);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(RegisterActivity.class);
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

    private void applyVerifyCode(String email) {
        if (StringUtils.isEmpty(email)) {
            showToast("请输入邮箱");
            return;
        }

        /**********    PostByFormBody     **********/
        // 此处重写回调方法
        HttpCallBack callBack = new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                Log.e("onSuccess", res);
//                showToastSync(res);

                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                Log.e("For Email", res);

                if (loginResponse.getCode() == 200) {
                    Looper.prepare();
                    showToast("获取验证码成功");
//                    navigateToWithFlags(MainActivity.class,
//                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Looper.loop();
                } else {
                    Looper.prepare();
                    showToast(loginResponse.getMsg());
                    Looper.loop();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        // 此处postRequest通过发送表单实现
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("email", email).build();

        Request request = new Request.Builder()
                .post(formBody)
                .url("http://139.196.153.21:8888/applyToLogin")
                .build();
        // 调用okHttpClient对象实现CallBack方法
        Call call = okHttpClient.newCall(request);
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

    private void login (String email, String verifyCode) {
        if (StringUtils.isEmpty(email)) {
            showToast("请输入邮箱");
            return;
        }
        if (StringUtils.isEmpty(verifyCode)) {
            showToast("请输入验证码");
            return;
        }

        /**********    PostByFormBody     **********/
        // 此处重写回调方法
        HttpCallBack callBack = new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                Log.e("onSuccess", res);
//                showToastSync(res);

                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);

                if (loginResponse.getCode() == 200) {
                    Looper.prepare();
                    showToast("登陆成功");
                    String token = loginResponse.getData();
                    Log.e("onSuccess", token);
                    // 应用sharedPreference存键值对
                    insertValueToSp("token", token);
                    navigateToWithFlags(MainActivity.class,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Looper.loop();
                } else {
                    Looper.prepare();
                    showToast(loginResponse.getMsg());
                    Looper.loop();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };

        // 此处postRequest通过发送表单实现
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        FormBody formBody = new FormBody.Builder()
                .add("email", email).add("verifyCode", verifyCode).build();

        Request request = new Request.Builder()
                .post(formBody)
                .url("http://139.196.153.21:8888/loginByEmail")
                .build();
        // 调用okHttpClient对象实现CallBack方法
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure", e.getMessage());
                callBack.onFailure(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String result = response.body().string();
                Log.d("onSuccess", "---Success---");
                callBack.onSuccess(result);
            }
        });

    }
}