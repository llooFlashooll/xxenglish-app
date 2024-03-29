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

    protected EditText inputText;

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
        String token = getValueFromSp("token");
        if (token == null || token.isEmpty()) {
            showToast("请先登录");
            return;
        }
        String content = inputText.getText().toString();
        String newsId = getNavigationParams("newsId");
        HashMap<String, Object> bodyInfo = new HashMap<String, Object> () {{
                put("content", content);
                put("newsId", newsId);
                put("token", token);
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