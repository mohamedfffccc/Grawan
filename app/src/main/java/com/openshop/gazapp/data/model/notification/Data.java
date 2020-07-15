
package com.openshop.gazapp.data.model.notification;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("notifications")
    @Expose
    private List<NotificationData> notifications = null;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<NotificationData> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationData> notifications) {
        this.notifications = notifications;
    }

}
