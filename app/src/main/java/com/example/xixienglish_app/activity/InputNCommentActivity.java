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

        // TODO: 待登录功能联调成功后从本地拿
        String userId = "ffe650b0-9c5c-45b9-8833-af90f4dba0d1";
        String content = inputText.getText().toString();
        HashMap<String, Object> bodyInfo = new HashMap<String, Object> () {{
            put("content", content);
            put("preReviewId", getNavigationParams("preReviewId"));
            put("rootReviewId", getNavigationParams("rootReviewId"));
            put("userId", userId);
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