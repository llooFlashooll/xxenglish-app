package com.example.xixienglish_app.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videocontroller.component.CompleteView;
import com.dueeeke.videocontroller.component.ErrorView;
import com.dueeeke.videocontroller.component.GestureView;
import com.dueeeke.videocontroller.component.VodControlView;
import com.dueeeke.videoplayer.player.VideoView;
import com.dueeeke.videoplayer.util.L;
import com.example.xixienglish_app.adapter.CommentItemAdapter;
import com.example.xixienglish_app.fragment.CommentFragment;
import com.example.xixienglish_app.fragment.TripletFragment;
import com.example.xixienglish_app.util.ProgressManagerImpl;

import com.example.xixienglish_app.R;

public class VideoDetailActivity extends BaseActivity {

    private VideoView videoView;

    @Override
    protected int initLayout() {
        return R.layout.activity_video_detail;
    }

    @Override
    protected void initView() {
        videoView = findViewById(R.id.player);
    }

    @Override
    protected void initData() {
        initVideoPlayer();
        // 添加底部三连区和评论区
        TripletFragment tripletFragment = new TripletFragment(getNavigationParams("newsId"), this);
        CommentFragment commentFragment = new CommentFragment(getNavigationParams("newsId"), this);
        getSupportFragmentManager()
            .beginTransaction()
            .add(R.id.triplet_fragment, tripletFragment)
            .add(R.id.comment_fragment, commentFragment)
            .commit();

        // 阅读数量增加
        plusCnt("listening");

        videoView.setUrl(getNavigationParams("content"));
        videoView.start();
    }


    private void initVideoPlayer() {
        StandardVideoController controller = new StandardVideoController(this);
        //根据屏幕方向自动进入/退出全屏
        controller.setEnableOrientation(true);

        controller.addControlComponent(new CompleteView(this));//自动完成播放界面

        controller.addControlComponent(new ErrorView(this));//错误界面

        VodControlView vodControlView = new VodControlView(this);//点播控制条
        controller.addControlComponent(vodControlView);

        GestureView gestureControlView = new GestureView(this);//滑动控制视图
        controller.addControlComponent(gestureControlView);


        // 可调节进度
        controller.setCanChangePosition(true);

        //保存播放进度
        videoView.setProgressManager(new ProgressManagerImpl());

        //    //播放状态监听
        videoView.addOnStateChangeListener(mOnStateChangeListener);

        videoView.setVideoController(controller);
    }


    private VideoView.OnStateChangeListener mOnStateChangeListener = new VideoView.SimpleOnStateChangeListener() {
        @Override
        public void onPlayerStateChanged(int playerState) {
            switch (playerState) {
                case VideoView.PLAYER_NORMAL://小屏
                    break;
                case VideoView.PLAYER_FULL_SCREEN://全屏
                    break;
            }
        }

        @Override
        public void onPlayStateChanged(int playState) {
            switch (playState) {
                case VideoView.STATE_IDLE:
                    break;
                case VideoView.STATE_PREPARING:
                    //在STATE_PREPARING时设置setMute(true)可实现静音播放
//                    mVideoView.setMute(true);
                    break;
                case VideoView.STATE_PREPARED:
                    break;
                case VideoView.STATE_PLAYING:
                    //需在此时获取视频宽高
                    int[] videoSize = videoView.getVideoSize();
                    L.d("视频宽：" + videoSize[0]);
                    L.d("视频高：" + videoSize[1]);
                    break;
                case VideoView.STATE_PAUSED:
                    break;
                case VideoView.STATE_BUFFERING:
                    break;
                case VideoView.STATE_BUFFERED:
                    break;
                case VideoView.STATE_PLAYBACK_COMPLETED:
                    break;
                case VideoView.STATE_ERROR:
                    break;
            }
        }
    };

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
    }


    public void onBackClick(View v) {
        videoView.pause();
        finish();
    }
}
