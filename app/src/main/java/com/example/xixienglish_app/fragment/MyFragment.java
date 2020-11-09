package com.example.xixienglish_app.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.LoginActivity;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;

public class MyFragment extends BaseFragment {

    private Button btn;

    @Override
    protected int initLayout() {
        return R.layout.my_fragment;
    }

    @Override
    protected void initView() {
        btn = mRootView.findViewById(R.id.button);
    }

    @Override
    protected void initData() {
        btn.setOnClickListener(v->{
            navigateTo(LoginActivity.class);
        });
    }
}