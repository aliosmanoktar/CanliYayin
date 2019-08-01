package net.catsbilisim.canliyayin.Api.InstagramApi.Class.User;

import java.io.Serializable;

public class UserSessionData implements Serializable {

    public String UserName;
    public String Password;
    public InstaUserShort LoggedInUser;
    public String RankToken;
    public String CsrfToken;
    public String FacebookUserId = "";
    public String FacebookAccessToken  = "";
    public static UserSessionData Empty= new UserSessionData();
    public static UserSessionData ForUsername(String username)
    {
       return new UserSessionData().setUserName(username);
    }

    public UserSessionData WithPassword(String password)
    {
        Password = password;
        return this;
    }

    public String getUserName() {
        return UserName;
    }

    public UserSessionData setUserName(String userName) {
        UserName = userName;
        return this;
    }


}
