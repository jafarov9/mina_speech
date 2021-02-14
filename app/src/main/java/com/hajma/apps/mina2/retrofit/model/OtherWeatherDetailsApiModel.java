package com.hajma.apps.mina2.retrofit.model;

public class OtherWeatherDetailsApiModel {

    private double lon;
    private double lat;
    private String base;
    private int temp;
    private double feels_like;
    private double temp_min;
    private int temp_max;
    private int timezone;
    private int id;
    private String name;
    private int cod;
    private long dt;

    public OtherWeatherDetailsApiModel(double lon, double lat, String base, int temp, double feels_like, double temp_min, int temp_max, int timezone, int id, String name, int cod, long dt) {
        this.lon = lon;
        this.lat = lat;
        this.base = base;
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.timezone = timezone;
        this.id = id;
        this.name = name;
        this.cod = cod;
        this.dt = dt;
    }

    public long getDt() {
        return dt;
    }

    public void setDt(long dt) {
        this.dt = dt;
    }

    public OtherWeatherDetailsApiModel() {
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public double getFeels_like() {
        return feels_like;
    }

    public void setFeels_like(double feels_like) {
        this.feels_like = feels_like;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public int getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(int temp_max) {
        this.temp_max = temp_max;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
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

    public int getCod() {
        return cod;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }
}
