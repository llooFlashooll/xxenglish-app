package com.example.xixienglish_app.adapter;

import android.content.Context;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.TripletEntity;
import com.example.xixienglish_app.entity.WordbookEntity;
import com.example.xixienglish_app.util.XToastUtils;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;

/**
 * 生词本数据适配器
 */
public class WordBookAdapter extends RecyclerView.Adapter<WordBookAdapter.ViewHolder> {

    private Context context;
    private List<WordbookEntity> list;
    private BaseActivity parentActivity;

    public WordBookAdapter(List<WordbookEntity> list, Context context, BaseActivity parentActivity) {
        this.list = list;
        this.context = context;
        this.parentActivity = parentActivity;
    }

    @NonNull
    @Override
    public WordBookAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WordBookAdapter.ViewHolder(LayoutInflater.from(context).inflate(R.layout.wordbook_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull WordBookAdapter.ViewHolder holder, int position) {
        WordbookEntity e = list.get(position);
        holder.english.setText(e.getEnglish());
        holder.chinese.setText(e.getChinese());
        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> params = new HashMap<String, Object>();
                params.put("english", e.getEnglish());
                params.put("chinese", e.getChinese());
                Api.config("/delete/glossary", params).postRequestWithToken(parentActivity, new HttpCallBack() {
                    @Override
                    public void onSuccess(String res) {

                        Gson gson = new Gson();
                        TripletEntity tripletEntity = gson.fromJson(res, TripletEntity.class);
                        if (tripletEntity.getCode() == 200) {
                            Looper.prepare();
                            XToastUtils.toast(tripletEntity.getMsg());
                            Looper.loop();
                        }
                        else {
                            Looper.prepare();
                            XToastUtils.toast(tripletEntity.getMsg());
                            Looper.loop();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) { e.printStackTrace();}
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView english;
        private TextView chinese;
        private Button btn_delete;
        public ViewHolder(View v){
            super(v);
            english = v.findViewById(R.id.english);
            chinese = v.findViewById(R.id.chinese);
            btn_delete = v.findViewById(R.id.btn_delete);
        }
    }
}
