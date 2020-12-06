package com.example.xixienglish_app.fragment;


import android.content.Intent;
import android.net.Uri;
import android.widget.Button;
import com.example.xixienglish_app.R;
import com.example.xixienglish_app.activity.BaseActivity;
import com.example.xixienglish_app.activity.LiveActivity;
import java.util.HashMap;
import java.util.Map;

public class ClassFragment extends BaseFragment {

    private Button pay;
    private Button audience;
    private Button broadcaster;

    @Override
    protected int initLayout() {
        return R.layout.class_fragment;
    }

    @Override
    protected void initView() {
        pay = mRootView.findViewById(R.id.btn_pay);
        audience = mRootView.findViewById(R.id.btn_live_audience);
        broadcaster = mRootView.findViewById(R.id.btn_live_broadcaster);
    }

    @Override
    protected void initData() {
        pay.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://admin.xunhuweb.com/alipaycashier?mchid=a4adb52d1e1f40479be53d115fb29a88&out_trade_no=srthyjtutrtrgtrhyg&total_fee=1346&body=www&notify_url=www.baidu.com&redirect_url=www.baidu.com&type=alipay&nonce_str=dawewrfgetht4h&sign=F35825E0E95E19637A4E43E8A1F222B5");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });

        audience.setOnClickListener(v -> {
            Map<String, String> params = new HashMap<String, String>(){{
                put("role", "2");
                put("roomNumber", "test_room");
                put("userName", "audience");
                put("token", "006459509d3bd1c4b73b38a25b99c60d28aIABPPuLJpWCl00jcpZhp+xTyp6tJ2fwHzKZ3Wq6mnHOpMlR75ncAAAAAEACVypXNHdTNXwEAAQAc1M1f");
            }};
            BaseActivity parent = (BaseActivity)getActivity();
            parent.navigateToWithParams(LiveActivity.class, params);
        });

        broadcaster.setOnClickListener(v -> {
            Map<String, String> params = new HashMap<String, String>(){{
                put("role", "1");
                put("roomNumber", "test_room");
                put("userName", "broadcaster");
                put("token", "006459509d3bd1c4b73b38a25b99c60d28aIABPPuLJpWCl00jcpZhp+xTyp6tJ2fwHzKZ3Wq6mnHOpMlR75ncAAAAAEACVypXNHdTNXwEAAQAc1M1f");
            }};
            BaseActivity parent = (BaseActivity)getActivity();
            parent.navigateToWithParams(LiveActivity.class, params);
        });
    }
}
