package com.example.xixienglish_app.entity;

public class CommentEntity {
  private String username = "steven";
  private String commentContent = "从rocketmq topic的创建机制可知，一个topic对应有多个消息队列，那么我们在发送消息时，是如何选择消息队列进行发送的？";

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getCommentContent() {
    return commentContent;
  }

  public void setCommentContent(String commentContent) {
    this.commentContent = commentContent;
  }
}
