package com.openshop.gazapp.data.model;

public class NotificationModel {
    public String title;
    public int image;
    public  String date;

    public NotificationModel(String title, int image, String date) {
        this.title = title;
        this.image = image;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
