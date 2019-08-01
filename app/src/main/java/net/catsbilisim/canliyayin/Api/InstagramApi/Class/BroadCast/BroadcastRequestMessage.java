package net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast;

import com.google.gson.Gson;

public class BroadcastRequestMessage {
    long _uid;
    public String _csrftoken;
    public String _uuid;
    int preview_height;
    int preview_width;
    String broadcast_message;
    String broadcat_type="RTMP";
    int internal_only=0;
    public BroadcastRequestMessage(){

    };
    public BroadcastRequestMessage(long _uid, String _csrftoken, String _uuid, int preview_height, int preview_width, String broadcast_message) {
        this._uid = _uid;
        this._csrftoken = _csrftoken;
        this._uuid = _uuid;
        this.preview_height = preview_height;
        this.preview_width = preview_width;
        this.broadcast_message = broadcast_message;
    }

    public BroadcastRequestMessage set_uid(long _uid) {
        this._uid = _uid;
        return this;
    }

    public BroadcastRequestMessage set_csrftoken(String _csrftoken) {
        this._csrftoken = _csrftoken;
        return this;
    }

    public BroadcastRequestMessage set_uuid(String _uuid) {
        this._uuid = _uuid;
        return this;
    }

    public BroadcastRequestMessage setPreview_height(int preview_height) {
        this.preview_height = preview_height;
        return this;
    }

    public BroadcastRequestMessage setPreview_width(int preview_width) {
        this.preview_width = preview_width;
        return this;
    }

    public BroadcastRequestMessage setBroadcast_message(String broadcast_message) {
        this.broadcast_message = broadcast_message;
        return this;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
