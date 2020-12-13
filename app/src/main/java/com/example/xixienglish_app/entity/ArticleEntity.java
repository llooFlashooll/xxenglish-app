package com.example.xixienglish_app.entity;

import java.util.Date;

public class ArticleEntity {
    /**
     * 点赞数量
     */
    private Integer likes = 0;
    /**
     * 评论数量
     */
    private Integer reviewNum = 0;
    /**
     *  收藏数量
     */
    private Integer favorites = 0;
    /**
     * 文章摘要
     */
    private String summary = "";
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

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getReviewNum() {
        return reviewNum;
    }

    public void setReviewNum(Integer reviewNum) {
        this.reviewNum = reviewNum;
    }

    public Integer getFavorites() {
        return favorites;
    }

    public void setFavorites(Integer favorites) {
        this.favorites = favorites;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
