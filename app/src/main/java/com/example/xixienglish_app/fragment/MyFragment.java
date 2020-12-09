package com.example.xixienglish_app.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.LoginActivity;
import com.example.xixienglish_app.activity.MainActivity;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.DialogLoader;

public class MyFragment extends BaseFragment {

    private Button btn_thumbsup;
    private Button btn_collect;
    private Button btn_wordbook;
    private TextView tv_applyTeacher;
    private TextView tv_language;
    private TextView tv_opinion;
    private TextView tv_rate;
    private TextView tv_share;
    private TextView tv_logout;

    @Override
    protected int initLayout() {
        String token = getValueFromSp("token");
        Log.e("For token", token);
        if (getValueFromSp("token").equals("")) {
            navigateTo(LoginActivity.class);
        }
        else {
            return R.layout.my_fragment;
        }
        return 0;
    }

    @Override
    protected void initView() {
        btn_thumbsup = mRootView.findViewById(R.id.btn_thumbsup);
        btn_collect = mRootView.findViewById(R.id.btn_collect);
        btn_wordbook = mRootView.findViewById(R.id.btn_wordbook);
        tv_applyTeacher = mRootView.findViewById(R.id.tv_applyTeacher);
        tv_language = mRootView.findViewById(R.id.tv_language);
        tv_opinion = mRootView.findViewById(R.id.tv_opinion);
        tv_rate = mRootView.findViewById(R.id.tv_rate);
        tv_share = mRootView.findViewById(R.id.tv_share);
        tv_logout = mRootView.findViewById(R.id.tv_logout);
    }

    @Override
    protected void initData() {
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogLoader.getInstance().showConfirmDialog(
                        getContext(),
                        "是否确认退出?",
                        "是",
                        (dialog, which) -> {
                            dialog.dismiss();
                            showToast("退出登录~");
                            removeValueFromSp("token");
                            navigateTo(MainActivity.class);
                        },
                        "否",
                        (dialog, which) -> dialog.dismiss()
                );
            }
        });
    }
}