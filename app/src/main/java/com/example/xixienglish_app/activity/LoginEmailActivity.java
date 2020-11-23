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

import java.util.HashMap;


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

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("email", email);

        Api.config(ApiConfig.APPLYLOGIN, params).postRequest(new HttpCallBack() {

            @Override
            public void onSuccess(final String res) {
                Log.e("onSuccess", res);
                showToastSync(res);

                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
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

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("account", email);
        params.put("password", verifyCode);

        Api.config(ApiConfig.LOGINBYEMAIL, params).postRequest(new HttpCallBack() {

            @Override
            public void onSuccess(final String res) {
                Log.e("onSuccess", res);
                showToastSync(res);

                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getCode() == 200) {
                    Looper.prepare();
                    showToast("登陆成功");
                    String token = loginResponse.getData();
                    Log.e("onSuccess", token);
                    // 应用sharedPreference存键值对
//                    insertVal("token", token);
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
        });
    }
}