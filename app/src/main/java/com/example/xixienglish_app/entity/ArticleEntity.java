package com.example.xixienglish_app.entity;

public class ArticleEntity {
    private String title = "title";
    private String image = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1604833119244&di=d9e4955ad03e5c75ec8305ee62cf7853&imgtype=0&src=http%3A%2F%2Fy2.ifengimg.com%2Fcmpp%2F2014%2F04%2F24%2F08%2Fff58c12f-56f7-4818-84be-eca9dd5c8c94.jpg";
    private Integer likes = 10;
    private Integer reviews = 15;
    private String content = "拜登NB";

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }

    public Integer getReviews() {
        return reviews;
    }

    public void setReviews(Integer reviews) {
        this.reviews = reviews;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
