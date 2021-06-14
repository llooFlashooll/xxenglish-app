package com.example.xixienglish_app.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.xixienglish_app.R;


public class MyGoalActivity extends BaseActivity {
    private Button backBtn;

    private TextView readingCnt;
    private TextView listeningCnt;
    private TextView wordCnt;


    @Override
    protected int initLayout() {
        return R.layout.activity_my_goal;
    }

    @Override
    protected void initView() {
        backBtn = findViewById(R.id.back);
        readingCnt = findViewById(R.id.reading_cnt);
        listeningCnt = findViewById(R.id.listening_cnt);
        wordCnt = findViewById(R.id.word_cnt);
    }

    @Override
    protected void initData() {
        backBtn.setOnClickListener(v -> finish());
        readingCnt.setText(get(readingCnt.getText().toString(), getGoalCnt("reading")));
        listeningCnt.setText(get(listeningCnt.getText().toString(), getGoalCnt("listening")));
        wordCnt.setText(get(wordCnt.getText().toString(), getGoalCnt("word")));
    }

    /**
     * 从sp中获得阅读/视频/背单词数量
     * @param type
     * @return
     */
    int getGoalCnt(String type) {
        String readingCnt = getValueFromSp(type);
        if (readingCnt == null || readingCnt.isEmpty()) return 0;
        return Integer.parseInt(readingCnt);
    }
    /**
     * 将今日xx目标: x/y中的x替换为实际值
     * @param origin
     * @param cnt
     * @return
     */
    private String get(String origin, int cnt) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < origin.length(); i ++ ) {
            strBuilder.append(origin.charAt(i));
            if (origin.charAt(i) == ' ') {
                break;
            }
        }
        strBuilder.append(cnt);
        for (int i = origin.indexOf('/'); i < origin.length(); i ++ )
            strBuilder.append(origin.charAt(i));
        return strBuilder.toString();

    }
}