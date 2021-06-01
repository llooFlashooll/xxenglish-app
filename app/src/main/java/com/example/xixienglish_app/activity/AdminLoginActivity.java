package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.ApiConfig;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.LoginResponse;
import com.example.xixienglish_app.util.StringUtils;
import com.google.gson.Gson;

import java.util.HashMap;

public class AdminLoginActivity extends BaseActivity {
    private EditText etAccount;
    private EditText etPassword;
    private Button btnLogin;

    @Override
    protected int initLayout() {
        return R.layout.activity_admin_login;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.edit_account);
        etPassword = findViewById(R.id.edit_password);
        btnLogin = findViewById(R.id.btn_login);
    }

    @Override
    protected void initData() {
        btnLogin.setOnClickListener(v -> {
            String account = etAccount.getText().toString().trim();
            String pwd = etPassword.getText().toString().trim();
            login(account, pwd);
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

        Api.config("/adminLogin", params).postRequest(new HttpCallBack() {

            @Override
            public void onSuccess(final String res) {
                Log.e("admin login response", res);

                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);

                if (loginResponse.getCode() == 200) {
                    Looper.prepare();
                    showToast("登陆成功");
                    String token = loginResponse.getData();
                    Log.e("token", token);
                    // 应用sharedPreference存键值对
                    insertValueToSp("token", token);
                    insertValueToSp("isAdmin", "true");
                    insertValueToSp("account", account);
                    navigateToWithFlags(InitActivity.class,
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