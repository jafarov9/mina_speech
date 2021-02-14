package com.hajma.apps.mina2.retrofit.model;

public class TodayNamazTimeApiModel {

    private String date, hicri, imsak,
            subh, zohr, esr, sham, xiften,
            midnight, sunrise, sunset;

    public TodayNamazTimeApiModel(String date, String hicri, String imsak, String subh, String zohr, String esr, String sham, String xiften, String midnight, String sunrise, String sunset) {
        this.date = date;
        this.hicri = hicri;
        this.imsak = imsak;
        this.subh = subh;
        this.zohr = zohr;
        this.esr = esr;
        this.sham = sham;
        this.xiften = xiften;
        this.midnight = midnight;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public TodayNamazTimeApiModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHicri() {
        return hicri;
    }

    public void setHicri(String hicri) {
        this.hicri = hicri;
    }

    public String getImsak() {
        return imsak;
    }

    public void setImsak(String imsak) {
        this.imsak = imsak;
    }

    public String getSubh() {
        return subh;
    }

    public void setSubh(String subh) {
        this.subh = subh;
    }

    public String getZohr() {
        return zohr;
    }

    public void setZohr(String zohr) {
        this.zohr = zohr;
    }

    public String getEsr() {
        return esr;
    }

    public void setEsr(String esr) {
        this.esr = esr;
    }

    public String getSham() {
        return sham;
    }

    public void setSham(String sham) {
        this.sham = sham;
    }

    public String getXiften() {
        return xiften;
    }

    public void setXiften(String xiften) {
        this.xiften = xiften;
    }

    public String getMidnight() {
        return midnight;
    }

    public void setMidnight(String midnight) {
        this.midnight = midnight;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
