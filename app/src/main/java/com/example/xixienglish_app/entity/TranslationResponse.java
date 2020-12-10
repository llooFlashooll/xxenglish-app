package com.example.xixienglish_app.entity;

import java.util.List;

/**
 * 接受百度翻译json结果
 */
public class TranslationResponse {

    private String from;
    private String to;
    private List<ResultPair> trans_result;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<ResultPair> getTrans_result() {
        return trans_result;
    }

    public void setTrans_result(List<ResultPair> trans_result) {
        this.trans_result = trans_result;
    }
}
