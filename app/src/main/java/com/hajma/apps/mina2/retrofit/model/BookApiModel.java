package com.hajma.apps.mina2.retrofit.model;

public class BookApiModel {

    private int id;
    private int page_count;
    private int year;
    private String price;
    private int sound_count;
    private String cover;
    private String name;

    public BookApiModel(int id, int page_count, int year, String price, int sound_count, String cover, String name) {
        this.id = id;
        this.page_count = page_count;
        this.year = year;
        this.price = price;
        this.sound_count = sound_count;
        this.cover = cover;
        this.name = name;
    }

    public BookApiModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPage_count() {
        return page_count;
    }

    public void setPage_count(int page_count) {
        this.page_count = page_count;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getSound_count() {
        return sound_count;
    }

    public void setSound_count(int sound_count) {
        this.sound_count = sound_count;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
