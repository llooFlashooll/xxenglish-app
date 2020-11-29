package com.example.xixienglish_app.entity;

import java.util.List;

public class VideoEntitySet {
  private String tag;
  private List<VideoEntity> videoList;

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public List<VideoEntity> getVideoList() {
    return videoList;
  }

  /**
   * 用于json转换
   * @param videoList
   */
  public void setNewsList(List<VideoEntity> videoList) {
    this.videoList = videoList;
  }
}
