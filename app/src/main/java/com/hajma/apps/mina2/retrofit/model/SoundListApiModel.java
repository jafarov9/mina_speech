package com.hajma.apps.mina2.retrofit.model;

public class SoundListApiModel {

    private int id;
    private String title;
    private String sound;

    public SoundListApiModel(int id, String title, String sound) {
        this.id = id;
        this.title = title;
        this.sound = sound;
    }

    public SoundListApiModel() {
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

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }
}
