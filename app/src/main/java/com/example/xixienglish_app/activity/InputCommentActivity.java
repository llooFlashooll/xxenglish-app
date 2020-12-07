package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.EditText;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.xuexiang.xui.widget.edittext.MultiLineEditText;
import com.xuexiang.xui.widget.toast.XToast;

import java.util.HashMap;

import static com.xuexiang.xui.XUI.getContext;

public class InputCommentActivity extends BaseActivity {

    private EditText inputText;

    @Override
    protected int initLayout() {
        return R.layout.activity_input_comment;
    }

    @Override
    protected void initView() {
        inputText = findViewById(R.id.input_text);
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
     * 发表按钮事件触发
     */
    public void onPostClick (View v) {
        // TODO: 待登录功能联调成功后从本地拿
        String userId = "ffe650b0-9c5c-45b9-8833-af90f4dba0d1";
        String content = inputText.getText().toString();
        System.out.println(content);
        String newsId = getNavigationParams("newsId");
        HashMap<String, Object> bodyInfo = new HashMap<String, Object> () {{
                put("userId", userId);
                put("content", content);
                put("newsId", newsId);
            }
        };
        Api.config("/review/root", bodyInfo).postRequest(new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                finish();
            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }

}