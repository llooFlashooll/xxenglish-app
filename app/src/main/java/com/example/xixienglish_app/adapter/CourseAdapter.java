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
import com.example.xixienglish_app.entity.CommentEntity;
import com.example.xixienglish_app.entity.CourseEntity;
import com.example.xixienglish_app.fragment.BaseFragment;

import java.util.HashMap;
import java.util.List;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private Context context;
    private List<CourseEntity> list;
    private BaseFragment parent;

    public CourseAdapter(List<CourseEntity> list, Context context, BaseFragment parent) {
        this.context = context;
        this.list = list;
        this.parent = parent;
    }

    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CourseAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.course_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {
        CourseEntity e = list.get(position);
        holder.courseName.setText(e.getCourseName());
        holder.courseDescription.setText(e.getDescription());
        holder.date.setText(e.getStartDate() + "~" + e.getEndDate());
        holder.time.setText("每周" + e.getWeekday() + " " + e.getStartTime() + "-" + e.getEndTime());
        holder.price.setText("价格: " + e.getPrice());
        holder.buy.setOnClickListener(v -> {
            parent.getValueFromSp("token");
            HashMap<String, Object> params = new HashMap<String, Object>(){{put("courseId", e.getCourseId());}};
            Api.config("/order/new", params).getRequest(parent.getActivity(), new HttpCallBack() {
                @Override
                public void onSuccess(String res) {
                    BaseActivity baseActivity = (BaseActivity) parent.getActivity();
                    JSONObject jsonObject = JSON.parseObject(res);
                    baseActivity.navigateToUrl((String) jsonObject.get("url"));
                }

                @Override
                public void onFailure(Exception e) { e.printStackTrace();}
            });
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
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
