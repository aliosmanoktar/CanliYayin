package net.catsbilisim.canliyayin.Preferences;

public abstract class DownloadSingleInterface<T> {
    public void Start() {}
    public abstract void Complete(T item);
}
