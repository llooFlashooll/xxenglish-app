package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.xixienglish_app.R;

public class WordBookActivity extends BaseActivity {


    @Override
    protected int initLayout() {
        return R.layout.activity_word_book;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    public void onBackClick(View v) {finish();}

}