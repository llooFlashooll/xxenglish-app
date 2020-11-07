package com.example.xixienglish_app.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.xixienglish_app.R;

/**
 * Article页面下具体的某一tag，比如商业/科技/体育
 */
public class ArticlePartitionFragment extends Fragment {


    public ArticlePartitionFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_article_partition, container, false);
    }
}