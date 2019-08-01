package net.catsbilisim.canliyayin.Api;

public class VideoStartThread implements Runnable {
    VideoStartEndpoint endpoint;
    IAddRtmpURL addRtmpURL;
    public VideoStartThread(VideoStartEndpoint endpoint,IAddRtmpURL addRtmpURL){
        this.endpoint=endpoint;
        this.addRtmpURL=addRtmpURL;
    }
    @Override
    public void run() {
        endpoint.StartBroadcast(addRtmpURL);
    }
}
