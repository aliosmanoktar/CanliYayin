package net.catsbilisim.canliyayin.Preferences;

public class CameraPreferences {
    int fps;
    int height;
    int weight;
    int bitrate;
    boolean save;

    public CameraPreferences(int fps, int height, int weight, int bitrate,boolean save) {
        this.fps = fps;
        this.height = height;
        this.weight = weight;
        this.bitrate = bitrate;
        this.save=save;
    }

    public int getFps() {
        return fps;
    }

    public void setFps(int fps) {
        this.fps = fps;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBitrate() {
        return bitrate;
    }

    public void setBitrate(int bitrate) {
        this.bitrate = bitrate;
    }

    public boolean isSave() {
        return save;
    }

    public void setSave(boolean save) {
        this.save = save;
    }
}
