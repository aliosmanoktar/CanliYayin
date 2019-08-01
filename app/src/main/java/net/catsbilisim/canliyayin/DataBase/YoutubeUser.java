package net.catsbilisim.canliyayin.DataBase;

public class YoutubeUser {
    private String name;
    private int ID;

    public YoutubeUser setName(String name) {
        this.name = name;
        return this;
    }

    public YoutubeUser setID(int ID){
        this.ID=ID;
        return this;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }
}
