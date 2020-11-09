package com.example.xixienglish_app.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.xixienglish_app.R;


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
                String pwd = etVerifyCode.getText().toString().trim();
//                login(email, pwd);
            }
        });

        getVerifyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
}