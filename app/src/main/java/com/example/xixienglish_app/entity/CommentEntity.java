package com.example.xixienglish_app.entity;

public class CommentEntity {

    private String name = "steven";
    private String content = "从rocketmq topic的创建机制可知，一个topic对应有多个消息队列，那么我们在发送消息时，是如何选择消息队列进行发送的？";
    private String rootReviewId;

    public String getRootReviewId() {
        return rootReviewId;
    }

    public void setRootReviewId(String rootReviewId) {
        this.rootReviewId = rootReviewId;
    }

    public String getName() {
        return name;
    }

    public void setName(String username) {
        this.name = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String commentContent) {
        this.content = commentContent;
    }
}
