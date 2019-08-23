package net.catsbilisim.canliyayin.Preferences;

public interface UpdateListener {
    void start();
    void UpdateSize(String size);
    void UpdateLoad(long ...params);
    void Finish();
}
