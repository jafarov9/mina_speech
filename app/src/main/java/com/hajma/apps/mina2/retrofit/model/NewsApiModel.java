package com.hajma.apps.mina2.retrofit.model;

public class NewsApiModel {

    private int id;
    private String title;
    private String cover;
    private String sound;
    private String video_link;
    private int news_type_id;
    private int category_id;
    private boolean comment_available;
    private int view_count;
    private String publish_date;
    private int user_id;
    private String content;
    private String news_type_name;
    private String category_name;
    private String images;

    public NewsApiModel(int id, String title, String cover, String sound, String video_link, int news_type_id, int category_id, boolean comment_available, int view_count, String publish_date, int user_id, String content, String news_type_name, String category_name, String images) {
        this.id = id;
        this.title = title;
        this.cover = cover;
        this.sound = sound;
        this.video_link = video_link;
        this.news_type_id = news_type_id;
        this.category_id = category_id;
        this.comment_available = comment_available;
        this.view_count = view_count;
        this.publish_date = publish_date;
        this.user_id = user_id;
        this.content = content;
        this.news_type_name = news_type_name;
        this.category_name = category_name;
        this.images = images;
    }

    public NewsApiModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getVideo_link() {
        return video_link;
    }

    public void setVideo_link(String video_link) {
        this.video_link = video_link;
    }

    public int getNews_type_id() {
        return news_type_id;
    }

    public void setNews_type_id(int news_type_id) {
        this.news_type_id = news_type_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public boolean isComment_available() {
        return comment_available;
    }

    public void setComment_available(boolean comment_available) {
        this.comment_available = comment_available;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public String getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(String publish_date) {
        this.publish_date = publish_date;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNews_type_name() {
        return news_type_name;
    }

    public void setNews_type_name(String news_type_name) {
        this.news_type_name = news_type_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
