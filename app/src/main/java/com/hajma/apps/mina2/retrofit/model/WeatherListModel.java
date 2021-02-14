package com.hajma.apps.mina2.retrofit.model;

public class WeatherListModel {

    private String date;
    private String icon;
    private double temp;
    private long dateLong;




    public WeatherListModel(String date, String icon, Double temp) {
        this.date = date;
        this.icon = icon;
        this.temp = temp;
    }

    public WeatherListModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public long getDateLong() {
        return dateLong;
    }

    public void setDateLong(long dateLong) {
        this.dateLong = dateLong;
    }
}
