package com.example.xixienglish_app.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.xixienglish_app.R;
import com.example.xixienglish_app.api.Api;
import com.example.xixienglish_app.api.HttpCallBack;
import com.example.xixienglish_app.entity.TripletEntity;
import com.google.gson.Gson;

import java.util.HashMap;

public class ApplyTeacherDialog extends Dialog {

    /**
     * 上下文对象 *
     */
    Activity context;
    public EditText et_idCardNumber;
    public EditText et_realName;
    public EditText et_teacherCardNumber;
    private Button btn_submit;
    private Button btn_cancel;
    private Dialog applyTeacherDialog = this;


    public ApplyTeacherDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        this.setContentView(R.layout.apply_teacher_dialog);

        et_idCardNumber = (EditText) findViewById(R.id.et_idCardNumber);
        et_realName = (EditText) findViewById(R.id.et_realName);
        et_teacherCardNumber = (EditText) findViewById(R.id.et_teacherCardNumber);

        /*
         * 获取圣诞框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
//        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.8
//        dialogWindow.setAttributes(p);

        // 根据id在布局中找到控件对象
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        // 为按钮绑定点击事件监听器
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idCardNumber = et_idCardNumber.getText().toString().trim();
                String realName = et_realName.getText().toString().trim();
                String teacherCardNumber = et_teacherCardNumber.getText().toString().trim();
                if (idCardNumber.equals("") || realName.equals("") || teacherCardNumber.equals("")) {
                    XToastUtils.toast("请填写完整信息");
                }

                HashMap<String, Object> bodyInfo = new HashMap<String, Object> () {{
                    put("idCardNumber", idCardNumber);
                    put("realName", realName);
                    put("teacherCardNumber", teacherCardNumber);
                }};

                Api.config("/applyTeacher", bodyInfo).postRequestWithToken(context, new HttpCallBack() {
                    @Override
                    public void onSuccess(String res) {

                        Gson gson = new Gson();
                        TripletEntity tripletEntity = gson.fromJson(res, TripletEntity.class);
                        if (tripletEntity.getCode() == 200) {
                            Looper.prepare();
                            XToastUtils.toast(tripletEntity.getMsg());
                            applyTeacherDialog.dismiss();
                            Looper.loop();
                        }
                        else {
                            Looper.prepare();
                            XToastUtils.toast(tripletEntity.getMsg());
                            Looper.loop();
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }
                });

            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyTeacherDialog.dismiss();
            }
        });

        this.setCancelable(true);
    }
}
