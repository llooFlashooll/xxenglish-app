package com.example.xixienglish_app.entity;

public class VideoEntity {
  /**
   * 视频标题
   */
  private String title;
  /**
   * 视频封面图
   */
  private String image;
  /**
   * 点赞
   */
  private Integer likes = 10;
  /**
   * 评论
   */
  private Integer comment = 15;
  /**
   * 收藏
   */
  private Integer collection = 12;

  /**
   * 对于video，content表示视频的url
   */
  private String content = "https://wasd003-1304125386.cos.ap-shanghai.myqcloud.com/video/e4c91a08-d5e5-4d4f-8d58-f0e186c7686b.mp4";


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Integer getLikes() {
    return likes;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
  }

  public Integer getComment() {
    return comment;
  }

  public void setComment(Integer comment) {
    this.comment = comment;
  }

  public Integer getCollection() {
    return collection;
  }

  public void setCollection(Integer collection) {
    this.collection = collection;
  }

}
