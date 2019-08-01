package net.catsbilisim.canliyayin.DataBase;

public class PeriscopeUser {

    private String UserName;
    private String AccesToken;
    private String TokenType;
    private int ID;
    public PeriscopeUser setAccesToken(String accesToken) {
        AccesToken = accesToken;
        return this;
    }

    public PeriscopeUser setTokenType(String tokenType) {
        TokenType = tokenType;
        return this;
    }


    public PeriscopeUser setID(int ID) {
        this.ID = ID;
        return this;
    }

    public PeriscopeUser setUserName(String UserName){
        this.UserName=UserName;
        return this;
    }

    public String getUserName() {
        return UserName;
    }

    public int getID() {
        return ID;
    }

    public String getAccesToken() {
        return AccesToken;
    }

    public String getTokenType() {
        return TokenType;
    }
}
