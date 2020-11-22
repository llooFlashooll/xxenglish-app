package com.example.xixienglish_app.entity;

import java.util.List;

public class ArticleEntitySet {
  private String tag;
  private List<ArticleEntity> newsList;

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public void setNewsList(List<ArticleEntity> newsList) {
    this.newsList = newsList;
  }

  public List<ArticleEntity> getNewsList() {
    return newsList;
  }
}
