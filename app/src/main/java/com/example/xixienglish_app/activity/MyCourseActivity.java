package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.adapter.CourseAdapter;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.CourseEntity;
import com.example.xixienglish_app.fragment.BaseFragment;

import java.util.HashMap;
import java.util.List;

/**
 * RecyclerView使用样例
 */
public class MyCourseActivity extends BaseActivity {
    /**
     * recyclerView就是用来承装列表项的容器
     */
    private RecyclerView recyclerView;
    /**
     * 从后端请求过来的课程数组
     */
    private List<CourseEntity> courseEntityList;
    /**
     * 这里设置thisActivity的目的是为了传入adapter
     * 之所以要向adapter传入是为了在adapter中可以调用navigate方法跳转到其他页面(到CourseAdapter中看一下就懂了)
     * 生词本/点赞夹/收藏夹貌似不需要跳转到其他页面，所以应该不需要传它
     */
    private BaseActivity thisActivity = this;

    private static final int SET_ADAPTER = 0X1;
    /**
     * 请求数据完成后发送msg给handle，由handle完成setAdapter操作，也是就是将courseEntityList中的数据渲染到recyclerView上
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SET_ADAPTER:
                    // 这一句固定写法
                    recyclerView.setLayoutManager(new LinearLayoutManager(thisActivity));
                    // 比如：setAdapter(new WordbookAdapter(...))
                    recyclerView.setAdapter(new CourseAdapter(courseEntityList, thisActivity, 1, thisActivity));
                    break;
            }
        }
    };

    @Override
    protected int initLayout() {
        return R.layout.activity_my_course;
    }

    @Override
    protected void initView() {
        recyclerView = findViewById(R.id.rv);
    }

    @Override
    protected void initData() {
        Api.config("/course/my", new HashMap<>()).getRequest(thisActivity, new HttpCallBack() {
            @Override
            public void onSuccess(String res) {
                /**
                 * 1. 请求数据并把数据反序列化为实体数组
                 */
                courseEntityList = JSON.parseArray(res, CourseEntity.class);
                /**
                 * 2. 发送消息交由handler处理
                 */
                Message msg = new Message();
                msg.what = SET_ADAPTER;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(Exception e) { e.printStackTrace();}
        });
    }

    /**
     * 回退按钮
     */
    public void onBackClick(View v) { finish();}
}