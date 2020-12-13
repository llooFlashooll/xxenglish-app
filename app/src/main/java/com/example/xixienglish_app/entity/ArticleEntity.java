package com.example.xixienglish_app.entity;

import java.util.Date;

public class ArticleEntity {
  /**
   * 点赞数量
   */
  private Integer likes = 10;
  /**
   * 评论数量
   */
  private Integer comment = 15;
  /**
   * 文章摘要
   */
  private String summary = "";
  /**
   * 阅读量
   */
  private String read = "12";
  /**
   * 文章主键
   */
  private String newsId;
  /**
   * 新闻标题
   */
  private String title;
  /**
   * 新闻类别
   */
  private String tag;
  /**
   * 创建时间
   */
  private Date datetime;
  /**
   * 原文链接
   */
  private String url;
  /**
   * 封面图外链
   */
  private String image;
  /**
   * 文章内容
   */
  private String content;


  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getTag() {
    return tag;
  }

  public String getTitle() {
    return title;
  }

  public String getImage() {
    return image;
  }

  public Integer getLikes() {
    return likes;
  }

  public String getNewsId() {
    return newsId;
  }

  public void setLikes(Integer likes) {
    this.likes = likes;
  }

  public void setComment(Integer comment) {
    this.comment = comment;
  }

  public void setSummary(String summary) {
    this.summary = summary;
  }

  public void setRead(String read) {
    this.read = read;
  }

  public void setNewsId(String newsId) {
    this.newsId = newsId;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setDatetime(Date datetime) {
    this.datetime = datetime;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public Integer getComment() {
    return comment;
  }

  public String getSummary() {
    return summary;
  }

  public String getRead() {
    return read;
  }

}
