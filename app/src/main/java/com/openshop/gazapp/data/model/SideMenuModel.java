package com.openshop.gazapp.data.model;

import android.graphics.drawable.Drawable;

/**
 * Created by mdev3 on 9/6/17.
 */

public class SideMenuModel {


    public SideMenuModel(int image, String title, boolean isSelected) {
        this.image = image;
        this.title = title;
        this.isSelected = isSelected;
    }

    private String title;
    private int image;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected = false;
}
