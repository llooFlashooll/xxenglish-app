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


import java.util.HashMap;

public class LoginActivity extends BaseActivity {

    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin;
    private TextView otherLogin;
    private TextView tvRegister;
    private TextView userProtocol;
    private TextView privProtocol;

    @Override
    protected int initLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.edit_account);
        etPassword = findViewById(R.id.edit_password);
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
                String account = etAccount.getText().toString().trim();
                String pwd = etPassword.getText().toString().trim();
                login(account, pwd);
            }
        });

        otherLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(LoginEmailActivity.class);
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

    private void login(String account, String pwd) {
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

        Log.e("account", account);
        Log.e("password", pwd);

        Api.config(ApiConfig.LOGIN, params).postRequest(new HttpCallBack() {

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

    }
}