package net.catsbilisim.canliyayin.Preferences;

public class SosyalMedya {
    int id;
    String Name;
    String Link;

    public SosyalMedya(int id,String name, String link) {
        Name = name;
        Link = link;
        this.id=id;
    }
    public SosyalMedya(String name,String Link){
        this.Name=name;
        this.Link=Link;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }

}