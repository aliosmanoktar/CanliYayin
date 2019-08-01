package net.catsbilisim.canliyayin.Preferences;

public class kullanici {
    String password;
    String userName;
    String Uid;
    String TelID;

    public kullanici(String password, String userName, String uid,String TelID) {
        this.password = password;
        this.userName = userName;
        Uid = uid;
        this.TelID=TelID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUid() {
        return Uid.replaceAll("\"","");
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getTelID() {
        return TelID;
    }

    public void setTelID(String telID) {
        TelID = telID;
    }

}
