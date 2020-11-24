package com.example.xixienglish_app.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.example.xixienglish_app.R;

/**
 * 底部上滑栏，继承Android基类
 */
public class PhotoPopupWindow extends PopupWindow {
    private View mView;     // PopupWindow菜单布局
    private Context mContext;       // 上下文参数
    private View.OnClickListener mSelectListener;       // 相册选取的点击监听器
    private View.OnClickListener mCaptureListener;      // 拍照的点击监听器
    private Button btn_camera;
    private Button btn_select;
    private Button btn_cancel;

    // 初始化，点击逻辑于外部创建
    public PhotoPopupWindow(Activity context, View.OnClickListener selectListener, View.OnClickListener captureListener) {
        super(context);
        this.mContext = context;
        this.mSelectListener = selectListener;
        this.mCaptureListener = captureListener;
        Init();
    }

    /**
     * 设置布局及点击事件
     */
    private void Init() {
        // 正常初始化逻辑，绑定控件
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.pop_item, null);
        btn_camera = mView.findViewById(R.id.icon_btn_camera);
        btn_select = mView.findViewById(R.id.icon_btn_select);
        btn_cancel = mView.findViewById(R.id.icon_btn_cancel);

        btn_select.setOnClickListener(mSelectListener);
        btn_camera.setOnClickListener(mCaptureListener);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        // 导入布局
        this.setContentView(mView);
        // 设置动画效果
        this.setAnimationStyle(R.style.popwindow_anim_style);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        // 设置可触
        this.setFocusable(true);
        ColorDrawable dw = new ColorDrawable(0x0000000);
        this.setBackgroundDrawable(dw);

        // 单击弹出窗以外处 关闭弹出窗
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int height = mView.findViewById(R.id.ll_pop).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }
}
