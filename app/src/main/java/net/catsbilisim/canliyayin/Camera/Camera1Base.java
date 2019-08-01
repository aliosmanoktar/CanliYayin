package net.catsbilisim.canliyayin.Camera;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.TextureView;

import com.pedro.encoder.audio.AudioEncoder;
import com.pedro.encoder.audio.GetAacData;
import com.pedro.encoder.input.audio.GetMicrophoneData;
import com.pedro.encoder.input.audio.MicrophoneManager;
import com.pedro.encoder.input.video.Camera1ApiManager;
import com.pedro.encoder.input.video.CameraHelper;
import com.pedro.encoder.input.video.CameraOpenException;
import com.pedro.encoder.input.video.Frame;
import com.pedro.encoder.input.video.GetCameraData;
import com.pedro.encoder.utils.CodecUtil;
import com.pedro.encoder.video.FormatVideoEncoder;
import com.pedro.encoder.video.GetVideoData;
import com.pedro.encoder.video.VideoEncoder;
import com.pedro.rtplibrary.view.GlInterface;
import com.pedro.rtplibrary.view.LightOpenGlView;
import com.pedro.rtplibrary.view.OffScreenGlThread;
import com.pedro.rtplibrary.view.OpenGlView;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

public abstract class Camera1Base implements GetAacData, GetCameraData, GetVideoData, GetMicrophoneData {

    private static final String TAG = "Camera1Base";
    private Context context;
    private Camera1ApiManager cameraManager;
    protected VideoEncoder videoEncoder;
    private MicrophoneManager microphoneManager;
    private AudioEncoder audioEncoder;
    private GlInterface glInterface;
    private boolean streaming = false;
    private boolean videoEnabled = true;
    private MediaMuxer mediaMuxer;
    private int videoTrack = -1;
    private int audioTrack = -1;
    private boolean recording = false;
    private boolean canRecord = false;
    private boolean onPreview = false;
    private MediaFormat videoFormat;
    private MediaFormat audioFormat;

    public Camera1Base(SurfaceView surfaceView) {
        this.context = surfaceView.getContext();
        this.cameraManager = new Camera1ApiManager(surfaceView, this);
        this.init();
    }

    public Camera1Base(TextureView textureView) {
        this.context = textureView.getContext();
        this.cameraManager = new Camera1ApiManager(textureView, this);
        this.init();
    }

    @RequiresApi(
            api = 18
    )
    public Camera1Base(OpenGlView openGlView) {
        this.context = openGlView.getContext();
        this.glInterface = openGlView;
        this.glInterface.init();
        this.cameraManager = new Camera1ApiManager(this.glInterface.getSurfaceTexture());
        this.init();
    }

    @RequiresApi(
            api = 18
    )
    public Camera1Base(LightOpenGlView lightOpenGlView) {
        this.context = lightOpenGlView.getContext();
        this.glInterface = lightOpenGlView;
        this.glInterface.init();
        this.cameraManager = new Camera1ApiManager(this.glInterface.getSurfaceTexture());
        this.init();
    }

    @RequiresApi(
            api = 18
    )
    public Camera1Base(Context context) {
        this.context = context;
        this.glInterface = new OffScreenGlThread(context);
        this.glInterface.init();
        this.cameraManager = new Camera1ApiManager(this.glInterface.getSurfaceTexture());
        this.init();
    }

    private void init() {
        this.videoEncoder = new VideoEncoder(this);
        this.microphoneManager = new MicrophoneManager(this);
        this.audioEncoder = new AudioEncoder(this);
    }

    public void enableFaceDetection(Camera1ApiManager.FaceDetectorCallback faceDetectorCallback) {
        this.cameraManager.enableFaceDetection(faceDetectorCallback);
    }

    public void disableFaceDetection() {
        this.cameraManager.disableFaceDetection();
    }

    public boolean isFaceDetectionEnabled() {
        return this.cameraManager.isFaceDetectionEnabled();
    }
    public Camera1ApiManager getCameraManager(){
        return this.cameraManager;
    }
    public boolean isFrontCamera() {
        return this.cameraManager.isFrontCamera();
    }

    public abstract void setAuthorization(String var1, String var2);

    public boolean prepareVideo(int width, int height, int fps, int bitrate, boolean hardwareRotation, int iFrameInterval, int rotation) {
        if (this.onPreview) {
            this.stopPreview();
            this.onPreview = true;
        }

        FormatVideoEncoder formatVideoEncoder = this.glInterface == null ? FormatVideoEncoder.YUV420Dynamical : FormatVideoEncoder.SURFACE;
        return this.videoEncoder.prepareVideoEncoder(width, height, fps, bitrate, rotation, hardwareRotation, iFrameInterval, formatVideoEncoder);
    }

    public boolean prepareVideo(int width, int height, int fps, int bitrate, boolean hardwareRotation, int rotation) {
        return this.prepareVideo(width, height, fps, bitrate, hardwareRotation, 2, rotation);
    }

    protected abstract void prepareAudioRtp(boolean var1, int var2);

    public boolean prepareAudio(int bitrate, int sampleRate, boolean isStereo, boolean echoCanceler, boolean noiseSuppressor) {
        this.microphoneManager.createMicrophone(sampleRate, isStereo, echoCanceler, noiseSuppressor);
        this.prepareAudioRtp(isStereo, sampleRate);
        return this.audioEncoder.prepareAudioEncoder(bitrate, sampleRate, isStereo);
    }

    public boolean prepareVideo() {
        int rotation = CameraHelper.getCameraOrientation(this.context);
        return this.prepareVideo(640, 480, 30, 1228800, false, rotation);
    }

    public boolean prepareAudio() {
        return this.prepareAudio(65536, 32000, true, false, false);
    }

    public void setForce(CodecUtil.Force forceVideo, CodecUtil.Force forceAudio) {
        this.videoEncoder.setForce(forceVideo);
        this.audioEncoder.setForce(forceAudio);
    }

    @RequiresApi(
            api = 18
    )
    public void startRecord(String path) throws IOException {
        this.mediaMuxer = new MediaMuxer(path, 0);
        this.recording = true;
        if (!this.streaming) {
            this.startEncoders();
        } else if (this.videoEncoder.isRunning()) {
            this.resetVideoEncoder();
        }

    }

    @RequiresApi(
            api = 18
    )
    public void stopRecord() {
        this.recording = false;
        if (this.mediaMuxer != null) {
            if (this.canRecord) {
                this.mediaMuxer.stop();
                this.mediaMuxer.release();
                this.canRecord = false;
            }

            this.mediaMuxer = null;
        }

        this.videoTrack = -1;
        this.audioTrack = -1;
        if (!this.streaming) {
            this.stopStream();
        }

    }

    public void startPreview(CameraHelper.Facing cameraFacing, int width, int height, int rotation) {
        if (!this.isStreaming() && !this.onPreview && !(this.glInterface instanceof OffScreenGlThread)) {
            if (this.glInterface != null && Build.VERSION.SDK_INT >= 18) {
                boolean isPortrait = this.context.getResources().getConfiguration().orientation == 1;
                if (isPortrait) {
                    this.glInterface.setEncoderSize(height, width);
                } else {
                    this.glInterface.setEncoderSize(width, height);
                }

                this.glInterface.setRotation(0);
                this.glInterface.start();
                this.cameraManager.setSurfaceTexture(this.glInterface.getSurfaceTexture());
            }

            this.cameraManager.setRotation(rotation);
            this.cameraManager.start(cameraFacing, width, height, this.videoEncoder.getFps());
            this.onPreview = true;
        } else {
            Log.e("Camera1Base", "Streaming or preview started, ignored");
        }

    }

    public void startPreview(CameraHelper.Facing cameraFacing, int width, int height) {
        this.startPreview(cameraFacing, width, height, CameraHelper.getCameraOrientation(this.context));
    }

    public void startPreview(CameraHelper.Facing cameraFacing) {
        this.startPreview(cameraFacing, 640, 480);
    }

    public void startPreview(int width, int height) {
        this.startPreview(CameraHelper.Facing.BACK, width, height);
    }

    public void startPreview() {
        this.startPreview(CameraHelper.Facing.BACK);
    }

    public void stopPreview() {
        if (!this.isStreaming() && this.onPreview && !(this.glInterface instanceof OffScreenGlThread)) {
            if (this.glInterface != null && Build.VERSION.SDK_INT >= 18) {
                this.glInterface.stop();
            }

            this.cameraManager.stop();
            this.onPreview = false;
        } else {
            Log.e("Camera1Base", "Streaming or preview stopped, ignored");
        }

    }

    public void setPreviewOrientation(int orientation) {
        this.cameraManager.setPreviewOrientation(orientation);
    }

    public void setZoom(MotionEvent event) {
        this.cameraManager.setZoom(event);
    }

    protected abstract void startStreamRtp(String var1);

    public void startStream(String url) {
        this.streaming = true;
        if (!this.recording) {
            this.startEncoders();
        } else {
            this.resetVideoEncoder();
        }

        this.startStreamRtp(url);
        this.onPreview = true;
    }

    private void startEncoders() {
        this.prepareGlView();
        this.videoEncoder.start();
        this.audioEncoder.start();
        this.microphoneManager.start();
        this.cameraManager.setRotation(this.videoEncoder.getRotation());
        this.cameraManager.start(this.videoEncoder.getWidth(), this.videoEncoder.getHeight(), this.videoEncoder.getFps());
        this.onPreview = true;
    }

    private void resetVideoEncoder() {
        if (this.glInterface != null && Build.VERSION.SDK_INT >= 18) {
            this.glInterface.removeMediaCodecSurface();
        }

        this.videoEncoder.reset();
        if (this.glInterface != null && Build.VERSION.SDK_INT >= 18) {
            this.glInterface.addMediaCodecSurface(this.videoEncoder.getInputSurface());
        }

    }

    private void prepareGlView() {
        if (this.glInterface != null && Build.VERSION.SDK_INT >= 18) {
            if (this.glInterface instanceof OffScreenGlThread) {
                this.glInterface = new OffScreenGlThread(this.context);
                ((OffScreenGlThread)this.glInterface).setFps(this.videoEncoder.getFps());
            }

            this.glInterface.init();
            if (this.videoEncoder.getRotation() != 90 && this.videoEncoder.getRotation() != 270) {
                this.glInterface.setEncoderSize(this.videoEncoder.getWidth(), this.videoEncoder.getHeight());
            } else {
                this.glInterface.setEncoderSize(this.videoEncoder.getHeight(), this.videoEncoder.getWidth());
            }

            this.glInterface.start();
            if (this.videoEncoder.getInputSurface() != null) {
                this.glInterface.addMediaCodecSurface(this.videoEncoder.getInputSurface());
            }

            this.cameraManager.setSurfaceTexture(this.glInterface.getSurfaceTexture());
        }

    }

    protected abstract void stopStreamRtp();

    public void stopStream() {
        if (this.streaming) {
            this.streaming = false;
            this.stopStreamRtp();
        }

        if (!this.recording) {
            this.microphoneManager.stop();
            if (this.glInterface != null && Build.VERSION.SDK_INT >= 18) {
                this.glInterface.removeMediaCodecSurface();
                if (this.glInterface instanceof OffScreenGlThread) {
                    this.glInterface.stop();
                    this.cameraManager.stop();
                }
            }

            this.videoEncoder.stop();
            this.audioEncoder.stop();
            this.videoFormat = null;
            this.audioFormat = null;
        }

    }

    public List<Camera.Size> getResolutionsBack() {
        return this.cameraManager.getPreviewSizeBack();
    }

    public List<Camera.Size> getResolutionsFront() {
        return this.cameraManager.getPreviewSizeFront();
    }

    public void disableAudio() {
        this.microphoneManager.mute();
    }

    public void enableAudio() {
        this.microphoneManager.unMute();
    }

    public boolean isAudioMuted() {
        return this.microphoneManager.isMuted();
    }

    public boolean isVideoEnabled() {
        return this.videoEnabled;
    }

    public void disableVideo() {
        this.videoEncoder.startSendBlackImage();
        this.videoEnabled = false;
    }

    public void enableVideo() {
        this.videoEncoder.stopSendBlackImage();
        this.videoEnabled = true;
    }

    public int getBitrate() {
        return this.videoEncoder.getBitRate();
    }

    public int getResolutionValue() {
        return this.videoEncoder.getWidth() * this.videoEncoder.getHeight();
    }

    public int getStreamWidth() {
        return this.videoEncoder.getWidth();
    }

    public int getStreamHeight() {
        return this.videoEncoder.getHeight();
    }

    public void switchCamera() throws CameraOpenException {
        if (this.isStreaming() || this.onPreview) {
            this.cameraManager.switchCamera();
        }

    }

    public GlInterface getGlInterface() {
        if (this.glInterface != null) {
            return this.glInterface;
        } else {
            throw new RuntimeException("You can't do it. You are not using Opengl");
        }
    }

    @RequiresApi(
            api = 19
    )
    public void setVideoBitrateOnFly(int bitrate) {
        this.videoEncoder.setVideoBitrateOnFly(bitrate);
    }

    public void setLimitFPSOnFly(int fps) {
        this.videoEncoder.setFps(fps);
    }

    public boolean isStreaming() {
        return this.streaming;
    }

    public boolean isOnPreview() {
        return this.onPreview;
    }

    public boolean isRecording() {
        return this.recording;
    }

    protected abstract void getAacDataRtp(ByteBuffer var1, MediaCodec.BufferInfo var2);

    public void getAacData(ByteBuffer aacBuffer, MediaCodec.BufferInfo info) {
        if (Build.VERSION.SDK_INT >= 18 && this.recording && this.canRecord) {
            this.mediaMuxer.writeSampleData(this.audioTrack, aacBuffer, info);
        }

        if (this.streaming) {
            this.getAacDataRtp(aacBuffer, info);
        }

    }

    protected abstract void onSpsPpsVpsRtp(ByteBuffer var1, ByteBuffer var2, ByteBuffer var3);

    public void onSpsPps(ByteBuffer sps, ByteBuffer pps) {
        if (this.streaming) {
            this.onSpsPpsVpsRtp(sps, pps, (ByteBuffer)null);
        }

    }

    public void onSpsPpsVps(ByteBuffer sps, ByteBuffer pps, ByteBuffer vps) {
        if (this.streaming) {
            this.onSpsPpsVpsRtp(sps, pps, vps);
        }

    }

    protected abstract void getH264DataRtp(ByteBuffer var1, MediaCodec.BufferInfo var2);

    public void getVideoData(ByteBuffer h264Buffer, MediaCodec.BufferInfo info) {
        if (Build.VERSION.SDK_INT >= 18 && this.recording) {
            if (info.flags == 1 && !this.canRecord && this.videoFormat != null && this.audioFormat != null) {
                this.videoTrack = this.mediaMuxer.addTrack(this.videoFormat);
                this.audioTrack = this.mediaMuxer.addTrack(this.audioFormat);
                this.mediaMuxer.start();
                this.canRecord = true;
            }

            if (this.canRecord) {
                this.mediaMuxer.writeSampleData(this.videoTrack, h264Buffer, info);
            }
        }

        if (this.streaming) {
            this.getH264DataRtp(h264Buffer, info);
        }

    }

    public void inputPCMData(byte[] buffer, int size) {
        this.audioEncoder.inputPCMData(buffer, size);
    }

    public void inputYUVData(Frame frame) {
        this.videoEncoder.inputYUVData(frame);
    }

    public void onVideoFormat(MediaFormat mediaFormat) {
        this.videoFormat = mediaFormat;
    }

    public void onAudioFormat(MediaFormat mediaFormat) {
        this.audioFormat = mediaFormat;
    }
}
