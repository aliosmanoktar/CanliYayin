package net.catsbilisim.canliyayin.Api.InstagramApi.Class.User;

import com.google.gson.annotations.SerializedName;

public class InstaLoginResponse {
    @SerializedName("status")
    public String Status;

    @SerializedName("logged_in_user")
    public InstaUserShortResponse User;
}
