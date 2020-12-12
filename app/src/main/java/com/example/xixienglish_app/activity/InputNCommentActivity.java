package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.NCommentEntity;

import java.util.HashMap;

public class InputNCommentActivity extends InputCommentActivity {

    @Override
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
        HashMap<String, Object> bodyInfo = new HashMap<String, Object> () {{
            put("content", content);
            put("preReviewId", getNavigationParams("preReviewId"));
            put("rootReviewId", getNavigationParams("rootReviewId"));
            put("token", token);
            }
        };
        Api.config("/review/second", bodyInfo).postRequest(new HttpCallBack() {
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