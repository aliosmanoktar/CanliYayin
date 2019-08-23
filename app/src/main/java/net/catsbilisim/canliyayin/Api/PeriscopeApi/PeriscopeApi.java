package net.catsbilisim.canliyayin.Api.PeriscopeApi;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.catsbilisim.canliyayin.Api.ApiResultSingle;
import net.catsbilisim.canliyayin.Api.IAddRtmpURL;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.IPeriscopeFinish;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CheckDeviceCodeResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CreateBroadcastResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CreateDeviceCodeResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.PublishBroadcastResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Endpoints.Connection.ConnectionCreateDeviceCode;
import net.catsbilisim.canliyayin.Api.UploadMedya;
import net.catsbilisim.canliyayin.Api.VideoEndEndpoint;
import net.catsbilisim.canliyayin.Api.VideoStartEndpoint;
import net.catsbilisim.canliyayin.DataBase.PeriscopeUser;

import java.io.IOException;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class PeriscopeApi implements VideoStartEndpoint, VideoEndEndpoint {

    private String RegionKey;
    private OkHttpClient client;
    private MediaType mediaType;
    private final String TAG = getClass().getName();
    private CheckDeviceCodeResponse CodeResponse;
    private PeriscopeUser user;
    public PeriscopeApi(PeriscopeUser user){
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        mediaType=MediaType.parse(PeriscopeConstant.TEXT_PLAIN);
        client=new OkHttpClient.Builder().addInterceptor(logging).build();
        this.user=user;
    }
    private ApiResultSingle<CreateDeviceCodeResponse> Superfinish;
    public CheckDeviceCodeResponse CheckDevice(String deviceCode){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(PeriscopeConstant.CLIENT_ID_KEY, PeriscopeConstant.CLIENT_ID);
        jsonObject.addProperty(PeriscopeConstant.DEVICE_CODE_KEY, deviceCode);
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request =new Request.Builder()
                .url(PeriscopeConstant.CheckDevice_URL)
                .post(body)
                .addHeader("Content-Type","application/json")
                .addHeader("User-Agent",PeriscopeConstant.USER_AGENT)
                .build();
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "CHECKDEVİCECODE:"+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(sonuc, CheckDeviceCodeResponse.class);
    }

    public String GetRegion(){
        OkHttpClient client=new OkHttpClient.Builder().build();
        Request request =new Request.Builder()
                .url(PeriscopeConstant.GETREGION_URL)
                .get()
                .addHeader("Content-Type","application/json")
                .addHeader("User-Agent",PeriscopeConstant.USER_AGENT)
                .addHeader("Authorization",getAuthorization())
                .build();
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "doInBackground: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonElement jelement = new JsonParser().parse(sonuc);
        return jelement.getAsJsonObject().get("region").getAsString();
    }

    public CreateBroadcastResponse CreateBroadcast(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(PeriscopeConstant.REGION_KEY, RegionKey);
        jsonObject.addProperty(PeriscopeConstant.IS_360_KEY, false);
        jsonObject.addProperty(PeriscopeConstant.IS_LOW_LATENCY, false);
        Log.e(TAG, "doInBackground: "+jsonObject.toString() );
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request =new Request.Builder()
                .url(PeriscopeConstant.CreateBroadcast_URL)
                .post(body)
                .addHeader("Content-Type","application/json")
                .addHeader("User-Agent",PeriscopeConstant.USER_AGENT)
                .addHeader("Authorization",getAuthorization())
                .build();
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "doInBackground: Sonuc "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(sonuc, CreateBroadcastResponse.class);
    }

    public CreateDeviceCodeResponse CreateDeviceCode(){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(PeriscopeConstant.CLIENT_ID_KEY, PeriscopeConstant.CLIENT_ID);
        Log.i(TAG, "doInBackground: "+jsonObject.toString());
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request =new Request.Builder()
                .url(PeriscopeConstant.CreateDevice_URL)
                .post(body)
                .addHeader("Content-Type","application/json")
                .addHeader("User-Agent",PeriscopeConstant.USER_AGENT)
                .build();
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "doInBackground: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(sonuc, CreateDeviceCodeResponse.class);
    }

    public PublishBroadcastResponse PublishBroadcast(String BroadcastID){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(PeriscopeConstant.BROADCAST_ID_KEY,BroadcastID);
        jsonObject.addProperty(PeriscopeConstant.TITLE_KEY, "test");
        jsonObject.addProperty(PeriscopeConstant.SHOULD_NOT_TWEET_KEY, true);
        jsonObject.addProperty(PeriscopeConstant.LOCALE_KEY, getLocale());
        jsonObject.addProperty(PeriscopeConstant.ENABLE_SUPER_HEARTS, true);
        System.out.println(jsonObject.toString());
        RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
        Request request =new Request.Builder()
                .url(PeriscopeConstant.PublishBroadcas_URL)
                .post(body)
                .addHeader("Content-Type","application/json")
                .addHeader("User-Agent",PeriscopeConstant.USER_AGENT)
                .addHeader("Authorization",getAuthorization())
                .build();
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "doInBackground: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(sonuc, PublishBroadcastResponse.class);
    }

    private String getLocale(){
        return new Locale("tr", "TR").toString();
    }

    private String getAuthorization(){
        Log.e(TAG, "getAuthorization: "+user.getTokenType()+" "+user.getAccesToken());
        return user.getTokenType()+" "+user.getAccesToken();
    }

    public PeriscopeApi setCodeResponse(CheckDeviceCodeResponse codeResponse) {
        CodeResponse = codeResponse;
        return this;
    }

    @Override
    public void EndBroadcast() {

    }


    public PeriscopeApi setSuperfinish(ApiResultSingle<CreateDeviceCodeResponse> superfinish) {
        Superfinish = superfinish;
        return this;
    }
    public void GetDeviceKey(){
        new ConnectionCreateDeviceCode(new ApiResultSingle<CreateDeviceCodeResponse>() {
            @Override
            public void Finish(CreateDeviceCodeResponse value) {
               Superfinish.Finish(value);
            }
        }).execute();
    }
    public void setUser(PeriscopeUser user) {
        this.user = user;
    }
    @Override
    public void StartBroadcast(IAddRtmpURL url) {
        CreateBroadcastResponse broadcastResponse=CreateBroadcast();

        PublishBroadcastResponse response=PublishBroadcast(broadcastResponse.broadcast.id);
        Log.e(TAG, "StartBroadcast: ==> BroadcastResponse"+new Gson().toJson(broadcastResponse));
        Log.e(TAG, "StartBroadcast: ==> Response "+new Gson().toJson(response) );
        url.AddURl(new UploadMedya.Medyas().setLink(broadcastResponse.encoder.rtmp_url+"/"+broadcastResponse.encoder.stream_key).setType("Periscope").setYayinName("Periscope"));
    }

    static class Builder{
        private PeriscopeUser user;
        public PeriscopeApi Build(){
            return new PeriscopeApi(user);
        }

        public Builder setUser(PeriscopeUser user) {
            this.user = user;
            return this;
        }
    }
}