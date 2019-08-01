package net.catsbilisim.canliyayin.DataBase;

public class InstagramUser {
    private long UserID;
    private String name;
    private int ID;
    private String password;
    private String login_userName;
    public InstagramUser setUserID(long userID) {
        this.UserID = userID;
        return this;

    }

    public InstagramUser setName(String name) {
        this.name = name;
        return this;
    }

    public InstagramUser setID(int ID) {
        this.ID = ID;
        return this;
    }

    public InstagramUser setPassword(String password) {
        this.password = password;
        return this;
    }

    public InstagramUser setLogin_userName(String login_userName) {
        this.login_userName = login_userName;
        return this;
    }

    public String getLogin_userName() {
        return login_userName;
    }

    public String getPassword() {
        return password;
    }

    public int getID() {
        return ID;
    }

    public long getUserID() {
        return UserID;
    }

    public String getName() {
        return name;
    }
}