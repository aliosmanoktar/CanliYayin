package net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class BroadcastEnd {
    @SerializedName("_csrftoken")
    String csrfToken;
    @SerializedName("_uuid")
    String Uuid;
    @SerializedName("_uid")
    long user;
    @SerializedName("end_after_copyright_warning")
    String copyright="False";
    public BroadcastEnd setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
        return this;
    }

    public BroadcastEnd setUuid(String uuid) {
        Uuid = uuid;
        return this;
    }

    public BroadcastEnd setUser(long user) {
        this.user = user;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
