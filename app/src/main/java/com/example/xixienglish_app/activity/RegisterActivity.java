package com.example.xixienglish_app.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.ApiConfig;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.ArticleEntitySet;
import com.example.xixienglish_app.entity.LoginResponse;
import com.example.xixienglish_app.fragment.ArticlePartitionFragment;
import com.example.xixienglish_app.util.StringUtils;
import com.google.gson.Gson;
import com.xuexiang.xui.widget.imageview.crop.Handle;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
    private EditText etName;
    private EditText etPassword;
    private EditText confirmPwd;
    private Button btnRegister;
    private TextView userProtocol;
    private TextView privProtocol;

    private final int MSG_NAME = 0;

    @Override
    protected int initLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        etAccount = findViewById(R.id.edit_account);
        etName = findViewById(R.id.edit_name);
        etPassword = findViewById(R.id.edit_password);
        confirmPwd = findViewById(R.id.edit_confirm_password);
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
                String name = etName.getText().toString().trim();
                String pwd = etPassword.getText().toString().trim();
                String confirm = confirmPwd.getText().toString().trim();
                register(account, name, pwd, confirm);
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

    private void register(String account, String name, String pwd, String confirm) {
        if (StringUtils.isEmpty(account)) {
            showToast("请输入账号");
            return;
        }

        if (StringUtils.isEmpty(name)) {
            showToast("请输入昵称");
            return;
        }

        if (StringUtils.isEmpty(pwd)) {
            showToast("请输入密码");
            return;
        }
        if (!pwd.equals(confirm)) {
            showToast("请保证输入密码与确认密码一致");
            return;
        }

        HashMap<String, Object> params = new HashMap<String, Object>();
        params.put("account", account);
        params.put("name", name);
        params.put("password", pwd);

        Api.config(ApiConfig.REGISTER, params).postRequest(new HttpCallBack() {

            @Override
            public void onSuccess(final String res) {
                Log.e("onSuccess", res);


                Gson gson = new Gson();
                LoginResponse loginResponse = gson.fromJson(res, LoginResponse.class);
                if (loginResponse.getCode() == 200) {
                    Looper.prepare();
                    showToast(loginResponse.getMsg());

                    navigateToWithFlags(LoginActivity.class,
                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Looper.loop();

                } else {
                    Looper.prepare();
                    showToast(loginResponse.getMsg());
                    showToast("请重新注册");
//                    navigateToWithFlags(LoginActivity.class,
//                            Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    Looper.loop();
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }
}