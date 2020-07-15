package com.openshop.gazapp.data.model;

import com.google.gson.annotations.SerializedName;

public class DeliveryCost {
    @SerializedName("status")
    public  int status;
   @ SerializedName("msg")
        public String msg;
   @SerializedName("data")
    public String data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
