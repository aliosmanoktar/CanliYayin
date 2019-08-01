package net.catsbilisim.canliyayin.Api;

public class VideoEndThread implements Runnable{
    VideoEndEndpoint videoEnd;

    public VideoEndThread(VideoEndEndpoint videoEnd) {
        this.videoEnd = videoEnd;
    }

    @Override
    public void run() {
        videoEnd.EndBroadcast();
    }
}
