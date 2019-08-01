package net.catsbilisim.canliyayin.Api.InstagramApi.Class.User;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class UserRequestMessage {
    @SerializedName("phone_id")
    private String PhoneId ;
    @SerializedName("username")
    private String Username ;
    @SerializedName("adid")
    private String AdId ;
    @SerializedName("guid")
    private UUID Guid ;
    @SerializedName("device_id")
    private String DeviceId ;
    @SerializedName("_uuid")
    private String Uuid;
    @SerializedName("google_tokens")
    private String GoogleTokens  = "[]";
    @SerializedName("password")
    private String Password ;
    @SerializedName("login_attempt_count")
    private String LoginAttemptCount  = "0";
    @SerializedName("_csrftoken")
    private String csrftoken;

    public UserRequestMessage setCsrftoken(String csrftoken) {
        this.csrftoken = csrftoken;
        return this;
    }

    public UserRequestMessage setPhoneId(String phoneId) {
        PhoneId = phoneId;
        return this;
    }

    public UserRequestMessage setUsername(String username) {
        Username = username;
        return this;
    }

    public UserRequestMessage setAdId(String adId) {
        AdId = adId;
        return this;
    }

    public UserRequestMessage setGuid(UUID guid) {
        Guid = guid;
        Uuid=guid.toString();
        return this;
    }

    public UserRequestMessage setDeviceId(String deviceId) {
        DeviceId = deviceId;
        return this;
    }

    public UserRequestMessage setUuid(String uuid) {
        Uuid = uuid;
        return this;
    }

    public UserRequestMessage setGoogleTokens(String googleTokens) {
        GoogleTokens = googleTokens;
        return this;
    }

    public UserRequestMessage setPassword(String password) {
        Password = password;
        return this;
    }

    public UserRequestMessage setLoginAttemptCount(String loginAttemptCount) {
        LoginAttemptCount = loginAttemptCount;
        return this;
    }

    public String getPhoneId() {
        return PhoneId;
    }

    public String getUsername() {
        return Username;
    }

    public String getAdId() {
        return AdId;
    }

    public UUID getGuid() {
        return Guid;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public String getUuid() {
        return Uuid;
    }

    public String getGoogleTokens() {
        return GoogleTokens;
    }

    public String getPassword() {
        return Password;
    }

    public String getLoginAttemptCount() {
        return LoginAttemptCount;
    }

    public String getCsrftoken() {
        return csrftoken;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
