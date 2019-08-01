package net.catsbilisim.canliyayin.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.gson.Gson;
import com.pedro.encoder.input.video.CameraHelper;

import net.catsbilisim.canliyayin.Api.IAddRtmpURL;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.InstagramConstants;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.PeriscopeApi;
import net.catsbilisim.canliyayin.Api.UploadMedya;
import net.catsbilisim.canliyayin.Api.VideoEndEndpoint;
import net.catsbilisim.canliyayin.Api.VideoEndThread;
import net.catsbilisim.canliyayin.Api.VideoStartEndpoint;
import net.catsbilisim.canliyayin.Api.VideoStartThread;
import net.catsbilisim.canliyayin.Api.YoutubeApi.YoutubeApi;
import net.catsbilisim.canliyayin.Camera.RtmpCamera1;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.Android.AndroidDevice;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastCreateResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.User.UserRequestMessage;
import net.catsbilisim.canliyayin.Api.InstagramApi.InstagramApi;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.IPeriscopeFinish;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CheckDeviceCodeResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CreateBroadcastResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Endpoints.Connection.ConnectionCreateBroadcast;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Endpoints.Connection.ConnectionPublishBroadcast;
import net.catsbilisim.canliyayin.DataBase.InstagramUser;
import net.catsbilisim.canliyayin.DataBase.PeriscopeUser;
import net.catsbilisim.canliyayin.DataBase.YoutubeUser;
import net.catsbilisim.canliyayin.DataBase.veritabani;
import net.catsbilisim.canliyayin.Preferences.CameraPreferences;
import net.catsbilisim.canliyayin.Preferences.IAddMedya;
import net.catsbilisim.canliyayin.Preferences.IYayinKontrol;
import net.catsbilisim.canliyayin.Preferences.Instagram_yayin_start;
import net.catsbilisim.canliyayin.Preferences.SosyalMedya;
import net.catsbilisim.canliyayin.Preferences.kullanici;
import net.catsbilisim.canliyayin.R;
import net.catsbilisim.canliyayin.Preferences.preferences;
import net.catsbilisim.canliyayin.adapter.FingerPrintBottomSheet;
import net.catsbilisim.canliyayin.adapter.FingerprintUiHelper;
import net.catsbilisim.canliyayin.backgrund.AddMedya_background;
import net.catsbilisim.canliyayin.backgrund.yayinKontrol_background;
import net.ossrs.rtmp.ConnectCheckerRtmp;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ana_ekran extends AppCompatActivity implements ConnectCheckerRtmp,SurfaceHolder.Callback ,IYayinKontrol {

    public static AndroidDevice _device;
    public static UserRequestMessage _requestMessage;
    private String TAG=getClass().getName();
    private Chronometer kronometre;
    private Integer[] orientations = new Integer[] { 0, 90, 180, 270 };
    private String currentDateAndTime;
    private RtmpCamera1 rtmpCamera;
    private ImageView broad_cast;
    private ImageView camera_swap;
    private ImageView flash_swap;
    private ImageView setting;
    private SurfaceView surfaceView;
    kullanici kullanici;
    private TextView live;
    private String url ="rtmp://176.235.244.13/LiveApp/";
    private File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/CanliYayin");
    private CameraPreferences cameraPreferences;
    private Dialog dialog;
    private List<Thread> VideoEndpointThread;
    private UploadMedya medya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ana_ekran);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        camera_swap= findViewById(R.id.camera_switch);
        flash_swap = findViewById(R.id.flash);
        setting = findViewById(R.id.setting);
        broad_cast=findViewById(R.id.broadcast);
        surfaceView = findViewById(R.id.surfece_view);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(),activity_setting.class));
            }
        });
        flash_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rtmpCamera.isFlashEnabled())
                    rtmpCamera.OpenFlash();
                else rtmpCamera.CloseFlash();
            }
        });
        kronometre = findViewById(R.id.broadcast_chronometer);
        kronometre.setVisibility(View.GONE);
        rtmpCamera = new RtmpCamera1(surfaceView,this);
        rtmpCamera.setPreviewOrientation(0);
        broad_cast.setOnClickListener(start_click);
        camera_swap.setOnClickListener(swap_click);
        surfaceView.getHolder().addCallback(this);
        live=findViewById(R.id.live_text);
        kronometre.stop();
        cameraPreferences=new preferences(getBaseContext()).getCameraPreferences();
        BottomSheetDialogFragment fr = FingerPrintBottomSheet.newInstance(this, new FingerprintUiHelper.Callback() {
            @Override
            public void onAuthenticated() {

            }

            @Override
            public void onError() {

            }

            @Override
            public void cancel() {

                Log.e("Ana Ekran", "cancel: " );
                ana_ekran.super.finish();
            }
        });
        url+=new preferences(getBaseContext()).getKullanici().getUid();
        Log.e(TAG, "onCreate: PublishUrl ==> "+url );
        InstaLogin();

    }

    void InstaLogin(){
        _device=new AndroidDevice()
                .setAndroidBoardName("angler")
                .setAndroidBootloader("angler-03.52")
                .setDeviceBrand("google")
                .setDeviceModel("Nexus 6P")
                .setDeviceModelBoot("qcom")
                .setDeviceModelIdentifier("Canli Yayin Uygulamasi")
                .setFirmwareBrand("angler")
                .setFirmwareFingerprint("google/angler/angler:6.0.1/MTC19X/2960136:user/release-keys")
                .setFirmwareTags("release-keys")
                .setFirmwareType("user")
                .setHardwareManufacturer("Huawei")
                .setHardwareModel("Nexus 6P")
                .setDeviceGuid(UUID.fromString("76a237d8-3630-4458-af20-7fe3511a019a"))
                .setPhoneGuid(UUID.fromString("6fac3452-44cb-4c7d-9f5e-094212535dd6"))
                .setResolution("1440x2560")
                .setDpi("640dpi");
        _requestMessage = new UserRequestMessage()
                .setPhoneId(_device.getPhoneGuid().toString())
                .setGuid(_device.getDeviceGuid())
                .setDeviceId(_device.getDeviceId())
                .setAdId(_device.getAdId().toString());
    }

    ImageView.OnClickListener swap_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                rtmpCamera.switchCamera();
            }catch (Exception ex){
                ToastLog(ex.getMessage());
            }
        }
    };

    ImageView.OnClickListener start_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
             kullanici = new preferences(getApplicationContext()).getKullanici();
             if (!rtmpCamera.isStreaming()) {
                 dialog=new AwesomeProgressDialog(getApplicationContext())
                         .setTitle("Lütfen Bekleyiniz")
                         .setMessage("Yayın İzni Kontrol Ediliyor")
                         .setCancelable(false)
                         .show();
                 new yayinKontrol_background(ana_ekran.this).execute(String.format(preferences.CheckStream, kullanici.getUserName(), kullanici.getPassword()));
             }else StartStream();
        }
    };

    private boolean prepareEncoders() {
        return rtmpCamera.prepareVideo(cameraPreferences.getWeight(), cameraPreferences.getHeight(), cameraPreferences.getFps(),
                cameraPreferences.getBitrate() * 1024,
               false, CameraHelper.getCameraOrientation(this)) && rtmpCamera.prepareAudio();
    }

    private void ToastLog(final String s){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getBaseContext(),s,Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onConnectionSuccessRtmp() {
        ToastLog("Bağlantı Başarılı");
    }

    @Override
    public void onConnectionFailedRtmp(final String s) {
        ToastLog("Bağlantı hatası"+s);
        rtmpCamera.stopStream();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                broad_cast.setImageResource(R.drawable.ic_camera_start);
                kronometre.stop();
                live.setVisibility(View.INVISIBLE);
                kronometre.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onDisconnectRtmp() {
        ToastLog("Bağlantı Koptu");
        if (kronometre.getVisibility()==View.VISIBLE)
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                broad_cast.setImageResource(R.drawable.ic_camera_start);
                kronometre.stop();
                live.setVisibility(View.INVISIBLE);
                kronometre.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAuthErrorRtmp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                broad_cast.setImageResource(R.drawable.ic_camera_start);
                kronometre.stop();
                live.setVisibility(View.INVISIBLE);
                kronometre.setVisibility(View.GONE);
            }
        });
        ToastLog("onAuthErrorRtmp");
    }

    @Override
    public void onAuthSuccessRtmp() {
        ToastLog("onAuthSuccessRtmp");
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.e("surfaceCreated","surfaceCreated");
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        cameraPreferences = new preferences(getBaseContext()).getCameraPreferences();
        if (!rtmpCamera.isOnPreview())
            ShowPreview();
    }

    void ShowPreview(){
        Log.e("OnSurfeceChanged","OnSurfeceChanged");
        prepareEncoders();
        rtmpCamera.startPreview(cameraPreferences.getWeight(),cameraPreferences.getHeight());
    }

    Chronometer.OnChronometerTickListener kronometre_zamanlayici = new Chronometer.OnChronometerTickListener() {
        @Override
        public void onChronometerTick(Chronometer chronometer) {
            long time = SystemClock.elapsedRealtime() - chronometer.getBase();
            int h = (int) (time / 3600000);
            int m = ((int) (time - ((long) (3600000 * h)))) / 60000;
            int s = ((int) ((time - ((long) (3600000 * h))) - ((long) (60000 * m)))) / 1000;
            String hh = h < 10 ? "0" + h : h + "";
            chronometer.setText(hh + ":" + (m < 10 ? "0" + m : m + "") + ":" + (s < 10 ? "0" + s : s + ""));
            if (live.getVisibility()==View.VISIBLE)
                live.setVisibility(View.INVISIBLE);
            else live.setVisibility(View.VISIBLE);

        }
    };

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (rtmpCamera.isRecording()){
            rtmpCamera.stopRecord();
            ToastLog("Kayıt Başarılı Dosya " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath());
        }else if (rtmpCamera.isStreaming()){
            rtmpCamera.stopStream();
        }
        Log.e("Destroye","Surfece");
        if (rtmpCamera.isFlashEnabled())
            rtmpCamera.CloseFlash();
        rtmpCamera.stopPreview();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (rtmpCamera.isRecording()){
            rtmpCamera.stopRecord();
            ToastLog("Kayıt Başarılı Dosya " + currentDateAndTime + ".mp4 saved in " + folder.getAbsolutePath());
        }else if (rtmpCamera.isStreaming()){
            rtmpCamera.stopStream();
        }
        if (rtmpCamera.isFlashEnabled())
            rtmpCamera.CloseFlash();
    }

    private void Kayit(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
        currentDateAndTime = sdf.format(new Date());
        try {
            rtmpCamera.startRecord(
                    folder.getAbsolutePath() + "/" + currentDateAndTime + ".mp4");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Kayıt Başladı... ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void Kontrol(Boolean yayin) {
        dialog.dismiss();
        if (yayin)
            YayinlariAyarla();
        else
            dialog=new AwesomeErrorDialog(getBaseContext())
                    .setTitle("Hata")
                    .setMessage("Yayın izni geçersiz")
                    .setErrorButtonClick(new Closure() {
                        @Override
                        public void exec() {
                            dialog.dismiss();
                        }
                    })
                    .show();
    }

    private IAddRtmpURL addRtmpURL=new IAddRtmpURL() {
        @Override
        public void AddURl(UploadMedya.Medyas me) {
            synchronized (medya){
                medya.getMedyalar().add(me);
            }
        }
    };

    private void YayinlariAyarla(){
        dialog.dismiss();
        medya=new UploadMedya()
                .setKullaniciAdi(kullanici.getUserName())
                .setSifre(kullanici.getPassword())
                .setUid(kullanici.getUid())
                .setMedyalar(new ArrayList<UploadMedya.Medyas>());
        List<Object> yayinlar =new veritabani(getBaseContext()).getAll();
        final List<Thread> threads=new ArrayList<>();
        VideoEndpointThread=new ArrayList<>();
        for (Object yayin:yayinlar) {
            if (yayin instanceof InstagramUser) {
                InstagramApi ınstagram = new InstagramApi(_requestMessage, _device, null, this,(InstagramUser) yayin);
                VideoEndpointThread.add(new Thread(new VideoEndThread(ınstagram)));
                threads.add(new Thread(new VideoStartThread(ınstagram,addRtmpURL)));
            } else if (yayin instanceof PeriscopeUser) {
                PeriscopeApi periscope = new PeriscopeApi((PeriscopeUser)yayin);
                VideoEndpointThread.add(new Thread(new VideoEndThread(periscope)));
                threads.add(new Thread(new VideoStartThread(periscope,addRtmpURL)));
            } else if (yayin instanceof YoutubeUser) {
                YoutubeUser user = (YoutubeUser) yayin;
                YoutubeApi youtubeApi = new YoutubeApi.Builder().setAccountName(user.getName(), this).Build();
                threads.add(new Thread(new VideoStartThread(youtubeApi,addRtmpURL)));
                VideoEndpointThread.add(new Thread(new VideoEndThread(youtubeApi)));
            }
        }
        for (Thread item : threads){
            item.start();
        }
        final Dialog dialog=new AwesomeProgressDialog(ana_ekran.this)
                .setTitle("Lütfen Bekleyiniz")
                .setMessage("Sosyal Medya Yayınları Ayarlanıyor")
                .setColoredCircle(R.color.dialogInfoBackgroundColor)
                .setCancelable(false)
                .show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadCheck(threads,dialog);
            }
        }).start();

    }

    private void ThreadCheck(List<Thread> threads, final Dialog dialog){
        while (threads.size()!=0){
            for (int i=0;i<threads.size();i++){
                if (threads.get(i).getState() == Thread.State.TERMINATED){
                    threads.remove(i);
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PostMedyas();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
                StartStream();
            }
        });

    }
    private void StartStream(){
        if (!rtmpCamera.isStreaming()){
            if (rtmpCamera.isRecording() || prepareEncoders()){
                broad_cast.setImageResource(R.drawable.ic_camera_end);
                rtmpCamera.startStream(this.url);
                if (cameraPreferences.isSave())
                    Kayit();
                kronometre.setVisibility(View.VISIBLE);
                live.setVisibility(View.VISIBLE);
                kronometre.setFormat("00:%s");
                kronometre.setBase(SystemClock.elapsedRealtime());
                kronometre.start();
                kronometre.setOnChronometerTickListener(kronometre_zamanlayici);
            }
            else
                ToastLog("Stream ile ilgili hata oluştu");
        }else{
            EndAllBroadcast();
            kronometre.stop();
            kronometre.setVisibility(View.GONE);
            live.setVisibility(View.INVISIBLE);
            rtmpCamera.stopStream();
            onDisconnectRtmp();
            ToastLog("Bağlantı Sonlandırıldı");
            broad_cast.setImageResource(R.drawable.ic_camera_start);
        }
    }

    void EndAllBroadcast(){
        for (Thread item : VideoEndpointThread){
            item.start();
        }
        final Dialog dialog=new AwesomeProgressDialog(ana_ekran.this)
                .setTitle("Lütfen Bekleyiniz")
                .setMessage("Sosyal Medya Yayınları Kapatılıyor")
                .setColoredCircle(R.color.dialogInfoBackgroundColor)
                .setCancelable(false)
                .show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadCheckEnd(VideoEndpointThread,dialog);
            }
        }).start();
    }

    private void ThreadCheckEnd(List<Thread> threads, final Dialog dialog){
        while (threads.size()!=0){
            for (int i=0;i<threads.size();i++){
                Log.e(TAG, "ThreadCheck: "+i+ " ==> "+threads.get(i).getState().name());
                if (threads.get(i).getState() == Thread.State.TERMINATED){
                    threads.remove(i);
                }
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        });
    }

    private void PostMedyas(){
        OkHttpClient client=new OkHttpClient.Builder().build();
        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON, new Gson().toJson(medya));
        Request request =new Request.Builder()
                .url(preferences.addMedyas)
                .post(body)
                .build();
        String sonuc="";
        Log.e(TAG, "PostMedyas: "+new Gson().toJson(medya));
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "PostMedyas: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}