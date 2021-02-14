package com.hajma.apps.mina2.eventbus;

import java.util.ArrayList;

public class DataEvent {

    public static class DuaModelClass {

        int response;

        int id;
        int day;
        String title;
        String content;

        public DuaModelClass(int response) {
            this.response = response;
        }

        public DuaModelClass(int response, int id, int day, String title, String content) {
            this.response = response;
            this.id = id;
            this.day = day;
            this.title = title;
            this.content = content;
        }

        public int getResponse() {
            return response;
        }

        public void setResponse(int response) {
            this.response = response;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getDay() {
            return day;
        }

        public void setDay(int day) {
            this.day = day;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class SubTutorialEvent {
        private String key;
        private int response;

        public SubTutorialEvent(String key, int response) {
            this.key = key;
            this.response = response;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getResponse() {
            return response;
        }

        public void setResponse(int response) {
            this.response = response;
        }
    }

    public static class TutorialRequestData {
        private int drawableID;
        private String function;
        private int response;

        public TutorialRequestData(int drawableID, String function, int response) {
            this.drawableID = drawableID;
            this.function = function;
            this.response = response;
        }

        public int getDrawableID() {
            return drawableID;
        }

        public void setDrawableID(int drawableID) {
            this.drawableID = drawableID;
        }

        public String getFunction() {
            return function;
        }

        public void setFunction(String function) {
            this.function = function;
        }

        public int getResponse() {
            return response;
        }

        public void setResponse(int response) {
            this.response = response;
        }
    }

    public static class ProfileImageChange {
        int response;

        public ProfileImageChange(int response) {
            this.response = response;
        }

        public int getResponse() {
            return response;
        }

        public void setResponse(int response) {
            this.response = response;
        }
    }


    public static class ChangeAlarmMusicText {

        private int response;
        private String title;

        public ChangeAlarmMusicText(int response, String title) {
            this.response = response;
            this.title = title;
        }

        public int getResponse() {
            return response;
        }

        public void setResponse(int response) {
            this.response = response;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    public static class ChangeAlarmRepeat {

        int response;
        ArrayList<String> repeatList;

        public ChangeAlarmRepeat(int response, ArrayList<String> repeatList) {
            this.response = response;
            this.repeatList = repeatList;
        }

        public int getResponse() {
            return response;
        }

        public void setResponse(int response) {
            this.response = response;
        }

        public ArrayList<String> getRepeatList() {
            return repeatList;
        }

        public void setRepeatList(ArrayList<String> repeatList) {
            this.repeatList = repeatList;
        }
    }

}
