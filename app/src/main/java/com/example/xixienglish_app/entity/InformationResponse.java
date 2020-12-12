package com.example.xixienglish_app.entity;

/**
 * 接受个人信息页
 */
public class InformationResponse {
    private String code;
    private InformationEntity data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public InformationEntity getData() {
        return data;
    }

    public void setData(InformationEntity data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
