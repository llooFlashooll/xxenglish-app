package com.example.xixienglish_app.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.CollectionDetailActivity;
import com.example.xixienglish_app.activity.InformationActivity;
import com.example.xixienglish_app.activity.LoginActivity;
import com.example.xixienglish_app.activity.MainActivity;
import com.example.xixienglish_app.activity.MyCourseActivity;
import com.example.xixienglish_app.activity.ThumbsupDetailActivity;
import com.example.xixienglish_app.activity.WordBookActivity;
import com.example.xixienglish_app.util.ApplyTeacherDialog;
import com.example.xixienglish_app.util.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.dialog.DialogLoader;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.dialog.strategy.InputInfo;

public class MyFragment extends BaseFragment {

    private Button btn_thumbsup;
    private Button btn_collect;
    private Button btn_wordbook;
    private TextView tv_name;
    private TextView tv_applyTeacher;
    private TextView tv_information;
    private TextView tv_class;
    private TextView tv_rate;
    private TextView tv_share;
    private TextView tv_logout;

    @Override
    protected int initLayout() {
        // 此处的跳转有点问题***，但不影响整个项目
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
        tv_name = mRootView.findViewById(R.id.tv_name);
        if (!getValueFromSp("name").equals("")) {
            tv_name.setText(getValueFromSp("name"));
        }
        tv_applyTeacher = mRootView.findViewById(R.id.tv_applyTeacher);
        tv_information = mRootView.findViewById(R.id.tv_information);
        tv_class = mRootView.findViewById(R.id.tv_class);
        tv_rate = mRootView.findViewById(R.id.tv_rate);
        tv_share = mRootView.findViewById(R.id.tv_share);
        tv_logout = mRootView.findViewById(R.id.tv_logout);
    }

    @Override
    protected void initData() {
        btn_thumbsup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(ThumbsupDetailActivity.class);
            }
        });

        btn_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(CollectionDetailActivity.class);
            }
        });

        btn_wordbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(WordBookActivity.class);
            }
        });

        tv_applyTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplyTeacherDialog applyTeacherDialog = new ApplyTeacherDialog(getActivity());
                applyTeacherDialog.show();
            }
        });

        tv_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(InformationActivity.class);
            }
        });

        tv_class.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(MyCourseActivity.class);
            }
        });

        tv_rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("I think it have to be top rate");
            }
        });

        tv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Share please");
            }
        });

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
                            removeValueFromSp("name");
                            navigateTo(MainActivity.class);
                        },
                        "否",
                        (dialog, which) -> dialog.dismiss()
                );
            }
        });
    }
}