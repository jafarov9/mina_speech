package com.hajma.apps.mina2.retrofit.model;

public class ProfileApiModel {

    private String name;
    private String email;
    private String mobile;
    private String username;
    private boolean verified;
    private String profile;
    private int gender;

    public ProfileApiModel(String name, String email, String mobile, String username, boolean verified, String profile, int gender) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.username = username;
        this.verified = verified;
        this.profile = profile;
        this.gender = gender;
    }

    public ProfileApiModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
