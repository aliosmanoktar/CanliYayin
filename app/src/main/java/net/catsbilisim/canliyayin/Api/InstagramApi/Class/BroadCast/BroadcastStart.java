package net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class BroadcastStart {
    @SerializedName("_csrftoken")
    String csrfToken;
    @SerializedName("_uuid")
    String Uuid;
    @SerializedName("_uid")
    long user;
    @SerializedName("should_send_notifications")
    String notification="True";

    public BroadcastStart setUser(long user){
        this.user=user;
        return this;
    }
    public BroadcastStart setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public BroadcastStart setUuid(String uuid) {
        Uuid = uuid;
        return this;
    }

    public BroadcastStart setNotification(String notification) {
        this.notification = notification;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}