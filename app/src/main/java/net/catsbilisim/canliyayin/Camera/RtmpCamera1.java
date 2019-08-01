package net.catsbilisim.canliyayin.Camera;

import android.content.Context;
import android.media.MediaCodec;
import android.support.annotation.RequiresApi;
import android.view.SurfaceView;
import android.view.TextureView;
import com.pedro.rtplibrary.view.LightOpenGlView;
import com.pedro.rtplibrary.view.OpenGlView;

import net.ossrs.rtmp.ConnectCheckerRtmp;
import net.ossrs.rtmp.SrsFlvMuxer;

import java.nio.ByteBuffer;

public class RtmpCamera1 extends Camera1Base {
    private SrsFlvMuxer srsFlvMuxer;

    public RtmpCamera1(SurfaceView surfaceView, ConnectCheckerRtmp connectChecker) {
        super(surfaceView);
        this.srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    }

    public RtmpCamera1(TextureView textureView, ConnectCheckerRtmp connectChecker) {
        super(textureView);
        this.srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    }

    @RequiresApi(
            api = 18
    )
    public RtmpCamera1(OpenGlView openGlView, ConnectCheckerRtmp connectChecker) {
        super(openGlView);
        this.srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    }

    @RequiresApi(
            api = 18
    )
    public RtmpCamera1(LightOpenGlView lightOpenGlView, ConnectCheckerRtmp connectChecker) {
        super(lightOpenGlView);
        this.srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    }

    @RequiresApi(
            api = 18
    )
    public RtmpCamera1(Context context, ConnectCheckerRtmp connectChecker) {
        super(context);
        this.srsFlvMuxer = new SrsFlvMuxer(connectChecker);
    }

    public void setProfileIop(byte profileIop) {
        this.srsFlvMuxer.setProfileIop(profileIop);
    }

    public void setAuthorization(String user, String password) {
        this.srsFlvMuxer.setAuthorization(user, password);
    }

    protected void prepareAudioRtp(boolean isStereo, int sampleRate) {
        this.srsFlvMuxer.setIsStereo(isStereo);
        this.srsFlvMuxer.setSampleRate(sampleRate);
    }

    protected void startStreamRtp(String url) {
        if (this.videoEncoder.getRotation() != 90 && this.videoEncoder.getRotation() != 270) {
            this.srsFlvMuxer.setVideoResolution(this.videoEncoder.getWidth(), this.videoEncoder.getHeight());
        } else {
            this.srsFlvMuxer.setVideoResolution(this.videoEncoder.getHeight(), this.videoEncoder.getWidth());
        }

        this.srsFlvMuxer.start(url);
    }

    protected void stopStreamRtp() {
        this.srsFlvMuxer.stop();
    }

    protected void getAacDataRtp(ByteBuffer aacBuffer, MediaCodec.BufferInfo info) {
        this.srsFlvMuxer.sendAudio(aacBuffer, info);
    }

    protected void onSpsPpsVpsRtp(ByteBuffer sps, ByteBuffer pps, ByteBuffer vps) {
        this.srsFlvMuxer.setSpsPPs(sps, pps);
    }

    protected void getH264DataRtp(ByteBuffer h264Buffer, MediaCodec.BufferInfo info) {
        this.srsFlvMuxer.sendVideo(h264Buffer, info);
    }
    public void CloseFlash(){
        getCameraManager().disableLantern();
    }
    public void OpenFlash(){
        getCameraManager().enableLantern();
    }
    public boolean isFlashEnabled(){
        return getCameraManager().isLanternEnable();
    }
}
