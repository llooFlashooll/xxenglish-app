package com.example.xixienglish_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.CourseEntity;
import com.example.xixienglish_app.fragment.BaseFragment;

import java.util.HashMap;
import java.util.List;

/**
 * RecyclerView使用样例
 */
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    /**
     * 关于context是干什么的我也不太理解qaq
     */
    private Context context;
    /**
     * 实体数组
     */
    private List<CourseEntity> list;
    /**
     * 以下两个就是为了调用navigateTo传进来的fragment和activity
     * 之所以有两个是因为MyCourseActivity和ClassFragment共用这一个adapter，
     * 一个传进来的是BaseActivity类型，一个传进来的是BaseFragment类型
     */
    private BaseFragment parentFragment;
    private BaseActivity parentActivity;
    /**
     * 0表示全部课程页面，1表示我的课程页面
     * 用于区分当前adapter是为MyCourseActivity服务还是ClassFragment
     */
    private int tag;

    public CourseAdapter(List<CourseEntity> list, Context context, int tag) {
        this.list = list;
        this.context = context;
        this.tag = tag;
    }

    public CourseAdapter(List<CourseEntity> list, Context context, int tag, BaseFragment parentFragment) {
        this(list, context, tag);
        this.parentFragment = parentFragment;
    }

    public CourseAdapter(List<CourseEntity> list, Context context, int tag, BaseActivity parentActivity) {
        this(list, context, tag);
        this.parentActivity = parentActivity;
    }

    /**
     * 这个地方的写法是固定的，返回具体每一个item的layout
     * 由于MyCourseActivity和ClassFragment的item样式不一样，所以这里要稍微区分一下
     */
    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (tag == 0)
            return new CourseAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.course_item,parent,false));
        return new CourseAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.my_course_item,parent,false));
    }

    /**
     * 数据绑定到样式上
     * 如果是单纯的显示直接setText
     * 如果是按钮之类就写事件触发
     */
    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {
        CourseEntity e = list.get(position);
        holder.courseName.setText(e.getCourseName());
        holder.courseDescription.setText(e.getDescription());
        holder.date.setText(e.getStartDate() + "~" + e.getEndDate());
        holder.time.setText("每周" + e.getWeekday() + " " + e.getStartTime() + "-" + e.getEndTime());
        if (holder.price != null) holder.price.setText("价格: " + e.getPrice());
        if (holder.buy != null) {
            holder.buy.setOnClickListener(v -> {
                HashMap<String, Object> params = new HashMap<String, Object>(){{put("courseId", e.getCourseId());}};
                BaseActivity baseActivity = (parentActivity == null ?
                    (BaseActivity) parentFragment.getActivity() : parentActivity);
                Api.config("/order/new", params).getRequest(baseActivity, new HttpCallBack() {
                    @Override
                    public void onSuccess(String res) {
                        JSONObject jsonObject = JSON.parseObject(res);
                        /**
                         * 这里需要点击按钮跳转，要用到BaseActivity中封装的各种navigate
                         * 这就是为什么Adapter的构造函数中要把BaseActivity/BaseFragment传进来
                         */
                        baseActivity.navigateToUrl((String) jsonObject.get("url"));
                    }

                    @Override
                    public void onFailure(Exception e) { e.printStackTrace();}
                });
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * ViewHolder用来保存每一个item中的ui组件，在onBindViewHolder的时候完成数据绑定到这些组件的操作
     */
    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView courseName;
        private TextView courseDescription;
        private TextView date;
        private TextView time;
        private TextView price;
        private LinearLayout buy;
        public ViewHolder(View v){
            super(v);
            courseName = v.findViewById(R.id.course_name);
            courseDescription = v.findViewById(R.id.course_description);
            date = v.findViewById(R.id.date);
            time = v.findViewById(R.id.time);
            price = v.findViewById(R.id.price);
            buy = v.findViewById(R.id.buy);
        }
    }
}
