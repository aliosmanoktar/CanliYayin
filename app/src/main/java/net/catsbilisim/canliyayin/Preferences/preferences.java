package net.catsbilisim.canliyayin.Preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;
import com.google.gson.Gson;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastCreateResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.User.UserRequestMessage;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CheckDeviceCodeResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CreateBroadcastResponse;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class preferences {
    String TAG = getClass().getName();
    public static final String addMedyas="http://176.235.244.2:80/api/MedyaYayin";
    public static final String loginUrl="http://176.235.244.2:80/api/Kullanici?name=%s&sifre=%s&telID=%s";
    public static final String CheckStream="http://176.235.244.2:80/api/YayinKontrol?name=%s&sifre=%s";
    public static final String AddYayin="http://176.235.244.2:80/api/MedyaYayin?url=%s&uid=%s&name=%s";
    public static final String DeleteYayin="http://176.235.244.2:80/api/MedyaYayin?id=%d&name=%s&sifre=%s&uid=%s";
    public static final String UpdateYayin="http://176.235.244.2:80/api/MedyaYayin?id=%d&Kname=%s&sifre=%s&uid=%s&name=%s&link=%s";
    public static final String InstagramYayin="http://176.235.244.2:80/api/InstagramYayin";
    public static final String InstagramYayinJson="{" +
            "  \"url\": \"%s\"," +
            "  \"YayinUid\": \"%s\"," +
            "  \"BroadCastid\": \"%s\"" +
            "}";
    private final String _cozunurluk="cozunurluk";
    private final String _fps = "fps";
    private final String _bitrate="birate";
    private final String _name="preferences";
    private final String _save="stream";
    private final String _kullanici="kullanici";
    private final String _SosyalMedya="ActivitySosyalMedya";
    private final String _Instagram_User="InstagramUser";
    private final String _PerisCopeToke="PerisCopeToken";
    private final String _PerisCopeBroadCast="PerisCopeBroadCast";
    private final String _youtubeUser="YoutubeUser";
    private String _path;
    private Context _context;
    public preferences(Context context){
        this._context=context;
    }
    public String getCozunurluk() {
        SharedPreferences preferences = _context.getSharedPreferences(_name,Context.MODE_PRIVATE);
        Camera.Size size = new RtmpCamera1(_context,null).getResolutionsBack().get(0);
        return preferences.getString(_cozunurluk,size.width+" X "+size.height);
    }
    public CameraPreferences getCameraPreferences(){
        String[] temp = getCozunurluk().split(" X ");
        return new CameraPreferences(Parse(getFps()),Parse(temp[1]),Parse(temp[0]),Parse(getBitrate()),getSave());
    }
    private int Parse(String s){
        return Integer.parseInt(s);
    }
    public String getFps() {
        SharedPreferences preferences = _context.getSharedPreferences(_name,Context.MODE_PRIVATE);
        return preferences.getString(_fps,"25");
    }

    public String getBitrate() {
        SharedPreferences preferences = _context.getSharedPreferences(_name,Context.MODE_PRIVATE);
        return preferences.getString(_bitrate,"2500");
    }

    public boolean getSave(){
        SharedPreferences preferences=_context.getSharedPreferences(_name,Context.MODE_PRIVATE);
        return preferences.getBoolean(_save,false);
    }

    public String getPath() {
        return Environment.getExternalStorageDirectory() + "/CanliYayin/";
    }

    public void setCozunurluk(String cozunurluk){
        SharedPreferences.Editor edit = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).edit();
        edit.putString(_cozunurluk,cozunurluk);
        edit.commit();
    }

    public void setFps(String fps){
        SharedPreferences.Editor edit = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).edit();
        edit.putString(_fps,fps);
        edit.commit();
    }

    public void setBitrate(String bitrate){
        SharedPreferences.Editor edit = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).edit();
        edit.putString(_bitrate,bitrate);
        edit.commit();
    }

    public void setSave(boolean save){
        SharedPreferences.Editor edit = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).edit();
        edit.putBoolean(_save,save);
        edit.commit();
    }

    public void DeleteKullanici(){
        SharedPreferences.Editor edit = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).edit();
        edit.putString(_kullanici,"");
        edit.commit();
    }

    public void saveKullanici(kullanici kullanici){
        String str=new Gson().toJson(kullanici);
        SharedPreferences.Editor edit = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).edit();
        edit.putString(_kullanici,str);
        Log.e(TAG, "saveKullanici: "+str );
        edit.commit();
    }

    public kullanici getKullanici(){
        String str = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).getString(_kullanici,"");
        return str.length()==0 ? null : new Gson().fromJson(str,kullanici.class);
    }

    public void DeleteCookie(){
        String[] files = new File(_context.getFilesDir().getParent()+"shared_prefs/").list();
        for (String str : files) {
            DeleteCookieFile(str);
        }
    }
    public boolean DeleteCookieFile(String name){
        return new File(_context.getFilesDir().getParent() + "/shared_prefs/"+name).delete();
    }
    /**
    public static String getInstagramAddUrl(Context context,BroadcastCreateResponse response){
        String sonuc= String.format(InstagramYayinJson,response.UploadUrl.replace("rtmps","rtmp").replace(":443",":80"),new preferences(context).getKullanici().Uid,""+response.BroadcastId);
        Log.e("Sorgu", "getInstagramAddUrl: "+sonuc );
        return sonuc;
    }

    public void SavePerisCopeBroadcast(CreateBroadcastResponse response){
        SharedPreferences.Editor edit = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).edit();
        edit.putString(_PerisCopeBroadCast,new Gson().toJson(response));
        edit.commit();
    }
    public CreateBroadcastResponse getPeriscopeBroadcat(){
        String str = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).getString(_PerisCopeBroadCast,"");
        return str.length()==0 ? null : new Gson().fromJson(str,CreateBroadcastResponse.class);
    }
    public void SavePeriscopeToken(CheckDeviceCodeResponse response){
        SharedPreferences.Editor edit = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).edit();
        edit.putString(_PerisCopeToke,new Gson().toJson(response));
        edit.commit();
    }
    public CheckDeviceCodeResponse getPeriscopeToken(){
        String str = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).getString(_PerisCopeToke,"");
        return str.length()==0 ? null : new Gson().fromJson(str,CheckDeviceCodeResponse.class);
    }
    public String getGoogleUser(){
        String str=_context.getSharedPreferences(_name,Context.MODE_PRIVATE).getString(_youtubeUser,null);
        return str;
    }
    public void setGoogleUser(String userName){
        SharedPreferences.Editor edit = _context.getSharedPreferences(_name,Context.MODE_PRIVATE).edit();
        edit.putString(_youtubeUser,userName);
        edit.commit();
    }*/
}
