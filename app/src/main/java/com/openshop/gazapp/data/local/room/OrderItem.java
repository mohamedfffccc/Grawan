package com.openshop.gazapp.data.local.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class OrderItem {
    @PrimaryKey(autoGenerate = true)


    int id;
    public int item_id;
    public int Department_id;
    public String NameItem;
    String ImageItem;
    double  price;
    public int quantity;

    public OrderItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getDepartment_id() {
        return Department_id;
    }

    public void setDepartment_id(int department_id) {
        Department_id = department_id;
    }

    public String getNameItem() {
        return NameItem;
    }

    public void setNameItem(String nameItem) {
        NameItem = nameItem;
    }

    public String getImageItem() {
        return ImageItem;
    }

    public void setImageItem(String imageItem) {
        ImageItem = imageItem;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public OrderItem( int item_id, int department_id, String nameItem, String imageItem, double price, Integer quantity) {
        this.id = id;
        this.item_id = item_id;
        Department_id = department_id;
        NameItem = nameItem;
        ImageItem = imageItem;
        this.price = price;
        this.quantity = quantity;
    }
}
