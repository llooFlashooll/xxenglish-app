package com.example.xixienglish_app.util;

public enum IdentityEnum {
    STUDENT("user", 2), TEACHER("teacher", 1);

    /**
     * 学生/老师
     * 学生: user
     * 老师: teacher
     */
    private String authCode;
    /**
     * 主播/观众
     * 主播: 1
     * 观众: 2
     */
    private int liveCode;

    IdentityEnum(String authCode, int liveCode) {
        this.authCode = authCode;
        this.liveCode = liveCode;
    }

    public static int getLiveCode(String authCode) {
        for (IdentityEnum cur : IdentityEnum.values()) {
            if (cur.authCode.equals(authCode))
                return cur.liveCode;
        }
        return -1;
    }
}
