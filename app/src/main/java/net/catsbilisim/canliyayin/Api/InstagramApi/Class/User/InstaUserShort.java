package net.catsbilisim.canliyayin.Api.InstagramApi.Class.User;

import java.io.Serializable;

public class InstaUserShort implements Serializable {

    public boolean IsVerified;
    public boolean IsPrivate;
    public Long Pk;
    public String ProfilePicture;
    public String ProfilePicUrl;
    public String ProfilePictureId = "unknown";
    public String UserName;
    public String FullName;

    public static InstaUserShort Empty = new InstaUserShort().setFullName("").setUserName("");

    public boolean Equals(InstaUserShort user)
    {
        return Pk == user.Pk;
    }

    public boolean Equals(Object obj)
    {
        return Equals(obj instanceof InstaUserShort);
    }

    public int GetHashCode()
    {
        return Pk.hashCode();
    }

    public InstaUserShort setUserName(String userName) {
        UserName = userName;
        return this;
    }

    public InstaUserShort setFullName(String fullName) {
        FullName = fullName;
        return this;
    }

}