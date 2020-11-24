package com.example.xixienglish_app.entity;

public class VideoEntity {
  private String title = "title";
  private String image = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1604833119244&di=d9e4955ad03e5c75ec8305ee62cf7853&imgtype=0&src=http%3A%2F%2Fy2.ifengimg.com%2Fcmpp%2F2014%2F04%2F24%2F08%2Fff58c12f-56f7-4818-84be-eca9dd5c8c94.jpg";
  private Integer likes = 10;
  private Integer comment = 15;
  private Integer collection = 12;
  private String url = "http://vfx.mtime.cn/Video/2019/03/14/mp4/190314223540373995.mp4";

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

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}