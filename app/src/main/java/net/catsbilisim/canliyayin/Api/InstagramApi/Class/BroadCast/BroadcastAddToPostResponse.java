package net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class BroadcastAddToPostResponse {
    @SerializedName("_csrftoken")
    String csrfToken;
    @SerializedName("_uuid")
    String Uuid;
    @SerializedName("_uid")
    long user;

    public BroadcastAddToPostResponse setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public BroadcastAddToPostResponse setUuid(String uuid) {
        Uuid = uuid;
        return this;
    }

    public BroadcastAddToPostResponse setUser(long user) {
        this.user = user;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
