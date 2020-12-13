package com.example.xixienglish_app.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.xixienglish_app.R;
import io.agora.rtc.Constants;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;


/**
 * 传入该activity的参数解释
 * role: 用户的身份，只有1和2两种可取值，1为主播，2为观众
 * roomNumber: 房间号
 * roomName: 房间名称
 * userName: 用户名
 * token: 后端传入,用于标识用户角色和权限
 */
public class LiveActivity extends BaseActivity {

    private RtcEngine rtcEngine;
    private Integer role;

    /**
     * UI控件
     */
    private TextView roomName;
    private TextView userName;
    private ImageView switchAudio;
    private ImageView switchVideo;

    /**
     * 本地视图
     */
    private FrameLayout localContainer;
    private SurfaceView localView;

    /**
     * 远端视图
     */
    private FrameLayout remoteContainer;
    private SurfaceView remoteView;


    /**
     * 用于运行时确认麦克风和摄像头设备的使用权限
     */
    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


    private final IRtcEngineEventHandler rtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(() -> Log.i("live", "进入频道成功, uid为" + (uid & 0xFFFFFFFFL)));
        }

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {
            runOnUiThread(() -> {
                Log.i("live", "接收到远端视频流, uid为" + (uid & 0xFFFFFFFFL));
                setupRemoteVideo(uid);
            });
        }

        @Override
        // 远端主播离开频道或掉线时，会触发该回调。
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(() -> {
                Log.i("live", "远端用户掉线, uid为" + (uid & 0xFFFFFFFFL));
                removeRemoteVideo(uid); });
        }
    };


    @Override
    protected int initLayout() {
        role = Integer.parseInt(getNavigationParams("role"));
        if (role == Constants.CLIENT_ROLE_BROADCASTER)
            return R.layout.broadcaster_layout;
        return R.layout.audience_layout;
    }

    @Override
    protected void initView() {
        localContainer = findViewById(R.id.local);
        remoteContainer = findViewById(R.id.remote);
        roomName = findViewById(R.id.room_name);
        userName = findViewById(R.id.user_name);
        switchAudio = findViewById(R.id.live_btn_mute_audio);
        switchVideo = findViewById(R.id.live_btn_mute_video);
    }

    @Override
    protected void initData() {
        // 获取权限后，初始化 RtcEngine，并加入频道。
        if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
            checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
            checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
            initEngineAndJoinChannel();
        }
        roomName.setText(getNavigationParams("roomName"));
        userName.setText(getNavigationParams("userName"));
    }

    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }
        return true;
    }

    private void initEngineAndJoinChannel() {
        initializeEngine();
        // 主播默认开启音视频，观众默认关闭音视频
        switchVideo.setActivated(role == Constants.CLIENT_ROLE_BROADCASTER);
        switchAudio.setActivated(role == Constants.CLIENT_ROLE_BROADCASTER);
        if (role == Constants.CLIENT_ROLE_BROADCASTER) setupLocalVideo();
        // 加入房间前需要先输入房间号，appid和房间号都相同的用户会进入相同的房间
        rtcEngine.joinChannel(getNavigationParams("token"),
                                getNavigationParams("roomNumber"), "", 0);
    }


    /**
     * 初始化 RtcEngine 对象
     */
    private void initializeEngine() {
        try {
            rtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), rtcEventHandler);
            // 场景设置为直播
            rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING);
            rtcEngine.setClientRole(role);
            // 启用视频模块
            rtcEngine.enableVideo();
        } catch (Exception e) {
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    /**
     * 设置本地视图
     */
    private void setupLocalVideo() {
        localView = RtcEngine.CreateRendererView(getBaseContext());
        localView.setZOrderMediaOverlay(true);
        localContainer.addView(localView);
        // uid用来唯一标识房间内的用户，设置为0系统会自动分配一个并在onJoinChannelSuccess中报告
        rtcEngine.setupLocalVideo(new VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }

    /**
     * 设置远端视图，远端视图需要设置远端用户的uid
     * 触发时机: 当SDK接收到第一帧远端视频并成功解码时触发
     *
     * @param uid
     */
    private void setupRemoteVideo(int uid) {
        remoteView = RtcEngine.CreateRendererView(getBaseContext());
        remoteContainer.addView(remoteView);
        rtcEngine.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
    }

    /**
     * 移除远端视图
     * @param uid
     */
    private void removeRemoteVideo(int uid) {
        rtcEngine.setupRemoteVideo(new VideoCanvas(null, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        remoteContainer.removeAllViews();
    }

    /**
     * 离开频道时关闭RTCEngine
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        rtcEngine.leaveChannel();
        RtcEngine.destroy();
    }

    /**
     * 音频权限切换按钮事件触发
     * @param view
     */
    public void onMuteAudioClicked(View view) {
        // 如果视频是关闭的，说明没有推流，此时音频也发不过去，只能处于关闭状态
        if (!switchVideo.isActivated()) return;
        rtcEngine.muteLocalAudioStream(view.isActivated());
        view.setActivated(!view.isActivated());
    }

    /**
     * 视频权限切换按钮事件触发
     * @param view
     */
    public void onMuteVideoClicked(View view) {
        if (view.isActivated()) {
            // 1. 关闭本地视频流及画面
            rtcEngine.setupLocalVideo(null);
            localContainer.removeAllViews();
            // 2. 停止推流
            rtcEngine.setClientRole(Constants.CLIENT_ROLE_AUDIENCE);
            // 3. 音频图标一起关闭
            switchAudio.setActivated(false);
        } else {
            // 1. 开启本地视频流及画面
            setupLocalVideo();
            // 2. 开始推流
            rtcEngine.setClientRole(Constants.CLIENT_ROLE_BROADCASTER);
            // 3. 音频图标一起打开
            switchAudio.setActivated(true);
        }
        view.setActivated(!view.isActivated());
    }

    /**
     * 前后置摄像头切换按钮事件触发
     * @param view
     */
    public void onSwitchCameraClicked(View view) {
        rtcEngine.switchCamera();
    }

    /**
     * 返回按钮事件触发
     * @param view
     */
    public void onLeaveClicked(View view) {
        finish();
    }
}
