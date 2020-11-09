package com.example.xixienglish_app.entity;

public class LoginResponse {
    /**
     * code: 200
     * data: eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI0MjI0MzNiOC1jOGIyLTQxMmItYWMyYi00OGY2MTRiMzE5NmMifQ.l2nDHZB3-7jUnKg7jmrglp0alrqD7MHpfRxnjxidXog
     */
    private int code;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
