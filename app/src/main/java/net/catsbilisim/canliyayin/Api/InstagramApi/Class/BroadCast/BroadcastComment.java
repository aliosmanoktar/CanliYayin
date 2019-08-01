package net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import java.util.UUID;

public class BroadcastComment {

    @SerializedName("user_breadcrumb")
    String user_breadcrumb;

    @SerializedName("idempotence_token")
    String token;

    @SerializedName("text")
    String comment_text;

    @SerializedName("live_or_vod")
    String live_or_vod="1";

    @SerializedName("offset_to_video_start")
    String video_start=" 0";

    @SerializedName("_csrftoken")
    String csrf_token;

    @SerializedName("_uuid")
    String Uuid;

    @SerializedName("_uid")
    String uid;

    public BroadcastComment(String user_breadcrumb, String token, String comment_text, String csrf_token, String uuid, String uid) {
        this.user_breadcrumb = user_breadcrumb;
        this.token = token;
        this.comment_text = comment_text;
        this.csrf_token = "";
        this.Uuid = uuid;
        this.uid = uid;
    }
    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    public static class Builder{
        String csrf_token;
        String Uuid;
        String uid;
        String comment_text;

        public BroadcastComment Build(){
            return new BroadcastComment(comment_text,UUID.randomUUID().toString(),comment_text,csrf_token,Uuid,uid);
        }
        public Builder setCsrf_token(String csrf_token) {
            this.csrf_token = csrf_token;
            return this;
        }

        public Builder setUuid(String uuid) {
            Uuid = uuid;
            return this;
        }

        public Builder setUid(String uid) {
            this.uid = uid;
            return this;
        }

        public Builder setComment_text(String comment_text) {
            this.comment_text = comment_text;
            return this;
        }
    }
}
