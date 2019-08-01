package net.catsbilisim.canliyayin.Api.InstagramApi.Class.User;

import com.google.gson.annotations.SerializedName;

public class InstaUserShortResponse{
    @SerializedName("username")
    public String UserName;

    @SerializedName("profile_pic_url")
    public String ProfilePicture;

    @SerializedName("profile_pic_id")
    public String ProfilePictureId = "unknown";

    @SerializedName("full_name")
    public String FullName;

    @SerializedName("is_verified")
    public boolean IsVerified;

    @SerializedName("is_private")
    public boolean IsPrivate;

    @SerializedName("pk")
    public long Pk;
}
