package com.hajma.apps.mina2.retrofit.model;

public class AuthorApiModel {

    private int id;
    private String name;

    public AuthorApiModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public AuthorApiModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
