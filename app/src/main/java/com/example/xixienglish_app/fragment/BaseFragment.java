package com.example.xixienglish_app.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xixienglish_app.R;
import com.xuexiang.xui.widget.toast.XToast;


public abstract class BaseFragment extends Fragment {

    // 把Fragment共有的方法集成此处
    protected View mRootView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(initLayout(), container, false);
            initView();
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     * 绑定xml
     *
     * @return
     */
    protected abstract int initLayout();

    /**
     * 绑定组件
     */
    protected abstract void initView();

    /**
     * 组件交互逻辑
     */
    protected abstract void initData();

    public void navigateTo(Class cls) {
        Intent in = new Intent(getActivity(), cls);
        startActivity(in);
    }

    // 存token
    protected void insertValueToSp(String key, String val) {
        SharedPreferences sp = getContext().getSharedPreferences("sp_xixienglish", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, val);
        editor.commit();
    }

    // 取token
    public String getValueFromSp(String key) {
        SharedPreferences sp = getContext().getSharedPreferences("sp_xixienglish", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    // 删除token数据
    protected void removeValueFromSp(String key) {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sp_xixienglish", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public void showToast(String msg) {
        XToast.info(getContext(), msg).show();
    }

    public void showToastSync(String msg) {
        Looper.prepare();
        XToast.info(getContext(), msg).show();
        Looper.loop();
    }

}
