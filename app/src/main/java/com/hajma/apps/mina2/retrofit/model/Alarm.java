package com.hajma.apps.mina2.retrofit.model;

import java.util.List;

public class Alarm {

    private int id;
    private String label;
    private String ringtone;
    private String alarm_mode;
    private String time;
    private String snooze_time;
    private boolean status;
    private List<String> repeat;

    public Alarm(int id, String label, String ringtone, String alarm_mode, String time, String snooze_time, boolean status, List<String> repeat) {
        this.id = id;
        this.label = label;
        this.ringtone = ringtone;
        this.alarm_mode = alarm_mode;
        this.time = time;
        this.snooze_time = snooze_time;
        this.status = status;
        this.repeat = repeat;
    }

    public Alarm() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }

    public String getAlarm_mode() {
        return alarm_mode;
    }

    public void setAlarm_mode(String alarm_mode) {
        this.alarm_mode = alarm_mode;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSnooze_time() {
        return snooze_time;
    }

    public void setSnooze_time(String snooze_time) {
        this.snooze_time = snooze_time;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getRepeat() {
        return repeat;
    }

    public void setRepeat(List<String> repeat) {
        this.repeat = repeat;
    }
}
