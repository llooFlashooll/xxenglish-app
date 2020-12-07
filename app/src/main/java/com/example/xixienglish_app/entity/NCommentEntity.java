package com.example.xixienglish_app.entity;

public class NCommentEntity {
    /**
     * 本条评论id
     */
    private String reviewId;
    /**
     * 本条评论所评论的评论的id
     */
    private String preReviewId;
    /**
     * 根评论id
     */
    private String rootReviewId;
    /**
     * 本条评论发起者id
     */
    private String userId;
    /**
     * 本条评论内容
     */
    private String content;
    /**
     * 本条评论发起者昵称
     */
    private String name;

    public String getPreContent() {
        return preContent;
    }

    public void setPreContent(String preContent) {
        this.preContent = preContent;
    }

    public String getPreUserId() {
        return preUserId;
    }

    public void setPreUserId(String preUserId) {
        this.preUserId = preUserId;
    }

    public String getPreName() {
        return preName;
    }

    public void setPreName(String preName) {
        this.preName = preName;
    }

    /**
     * 上一条评论内容
     */
    private String preContent;
    /**
     * 上一条评论的用户id
     */
    private String preUserId;
    /**
     * 上一条评论的用户昵称
     */
    private String preName;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getPreReviewId() {
        return preReviewId;
    }

    public void setPreReviewId(String preReviewId) {
        this.preReviewId = preReviewId;
    }

    public String getRootReviewId() {
        return rootReviewId;
    }

    public void setRootReviewId(String rootReviewId) {
        this.rootReviewId = rootReviewId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
