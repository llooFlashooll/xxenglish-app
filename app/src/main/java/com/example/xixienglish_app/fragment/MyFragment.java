package com.example.xixienglish_app.fragment;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.AllGoalActivity;
import com.example.xixienglish_app.activity.CollectionDetailActivity;
import com.example.xixienglish_app.activity.InformationActivity;
import com.example.xixienglish_app.activity.InitActivity;
import com.example.xixienglish_app.activity.LoginActivity;
import com.example.xixienglish_app.activity.MyCourseActivity;
import com.example.xixienglish_app.activity.MyGoalActivity;
import com.example.xixienglish_app.activity.ThumbsupDetailActivity;
import com.example.xixienglish_app.activity.WordBookActivity;
import com.example.xixienglish_app.util.ApplyTeacherDialog;
import com.xuexiang.xui.widget.dialog.DialogLoader;

public class MyFragment extends BaseFragment {

    private Button btn_thumbsup;
    private Button btn_collect;
    private Button btn_wordbook;
    private TextView tv_account;
    private TextView tv_applyTeacher;
    private TextView tv_information;
    private TextView tv_class;
    private TextView tv_allgoals;
    private TextView tv_mygoal;
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
        tv_account = mRootView.findViewById(R.id.tv_account);
        String account = getValueFromSp("account");
        if (account != null && !account.isEmpty()) {
            tv_account.setText(account);
        }
        tv_applyTeacher = mRootView.findViewById(R.id.tv_applyTeacher);
        tv_information = mRootView.findViewById(R.id.tv_information);
        tv_class = mRootView.findViewById(R.id.tv_class);
        tv_mygoal = mRootView.findViewById(R.id.my_goal);
        tv_allgoals = mRootView.findViewById(R.id.all_goals);
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

        tv_mygoal.setOnClickListener(v -> navigateTo(MyGoalActivity.class));

        tv_allgoals.setOnClickListener(v -> navigateTo(AllGoalActivity.class));

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
                            removeValueFromSp("isAdmin");
                            navigateTo(InitActivity.class);
                        },
                        "否",
                        (dialog, which) -> dialog.dismiss()
                );
            }
        });
    }
}