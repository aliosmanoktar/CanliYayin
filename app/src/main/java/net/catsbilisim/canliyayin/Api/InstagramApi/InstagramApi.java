package net.catsbilisim.canliyayin.Api.InstagramApi;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
//import com.crashlytics.android.Crashlytics;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.catsbilisim.canliyayin.Api.IAddRtmpURL;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.Android.AndroidDevice;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BaseRequest;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastAddToPostResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastComment;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastCreateResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastEnd;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastRequestMessage;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastStart;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.InstagramConstants;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.InstagramHashUtil;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.Interface.ILoginResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.User.InstaLoginResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.User.UserRequestMessage;
import net.catsbilisim.canliyayin.Api.InstagramApi.Connection.LoginRequest;
import net.catsbilisim.canliyayin.Api.UploadMedya;
import net.catsbilisim.canliyayin.Api.VideoEndEndpoint;
import net.catsbilisim.canliyayin.Api.VideoStartEndpoint;
import net.catsbilisim.canliyayin.DataBase.InstagramUser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class InstagramApi implements VideoStartEndpoint, VideoEndEndpoint {
    private InstagramUser user;
    private Long id;
    private UserRequestMessage _request_message;
    private AndroidDevice _device;
    private BroadcastCreateResponse broadcastCreateResponse;
    private InstaLoginResponse loginResponse;
    public UserRequestMessage get_request_message() {
        return _request_message;
    }
    private String TAG=getClass().getName();
    private OkHttpClient client;
    private CallBack callBack;
    private Context context;
    private Long BrodcastId;
    SharedPrefsCookiePersistor cookiePersistor;
    public AndroidDevice get_device() {
        return _device;
    }
    public InstagramApi setUser(InstagramUser user) {
        this.user = user;
        return this;
    }
    public InstagramApi(final UserRequestMessage _request_message, AndroidDevice _device, CallBack callBack, Context context) {
        this._request_message = _request_message;
        this._device = _device;
        this.context=context;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        cookiePersistor=new SharedPrefsCookiePersistor(context);
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(),cookiePersistor);
        client=new OkHttpClient.Builder().cookieJar(cookieJar).addInterceptor(logging).build();
        _request_message.setDeviceId(InstagramHashUtil.generateDeviceId(_request_message.getUsername(),_request_message.getPassword()));
        this.callBack=callBack;
    }
    public InstagramApi(final UserRequestMessage _request_message, AndroidDevice _device, CallBack callBack, Context context,InstagramUser user) {
        this._request_message = _request_message;
        this._device = _device;
        this.context=context;
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        cookiePersistor=new SharedPrefsCookiePersistor(context,user.getName());
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), cookiePersistor);
        client=new OkHttpClient.Builder().cookieJar(cookieJar).addInterceptor(logging).build();
        _request_message.setDeviceId(InstagramHashUtil.generateDeviceId(_request_message.getUsername(),_request_message.getPassword()));
        this.callBack=callBack;
        setUser(user);
    }
    public void StartStream(){
        callBack.Start();
        //Log.e(TAG, "InstagramApi: "+getCsrf());
    }
    /*public void BroadCast(long id){
        new BroadCastRequest(_request_message, getRequestMessage(), getHeaders(), client, new IBroadcastResponse() {
            @Override
            public void finish(final BroadcastCreateResponse response) {
                String url = preferences.getInstagramAddUrl(context,response);
                Log.e(TAG, "finish: "+url );
                broadcastCreateResponse=response;
                new InstagramYayin_background(new Instagram_yayin_start() {
                    @Override
                    public void Start(String url) {
                        new StartBroadcastRequest(client, getHeaders(), new IBroadcastStart() {
                            @Override
                            public void Start(Boolean isOk) {
                                callBack.Finish(response);
                            }
                        },new BroadcastStart()
                                .setCsrfToken(_request_message.getCsrftoken())
                        .setUuid(_request_message.getDeviceId()),response.BroadcastId+"").execute();
                    }
                }).execute(preferences.InstagramYayin,url);

            }
        }).execute();
    }*/
    public void StopBroadCast(){
        /*new AddToPostRequest(client, getHeaders(), new IBroadcastAddToPost() {
            @Override
            public void finish() {
                new EndStreamRequest(client, getHeaders(), new IBroadcastEnd() {
                    @Override
                    public void Finish() {
                        Log.e(TAG,"İşlem Tamamlandı");
                    }
                },new BroadcastEnd().setCsrfToken(_request_message.getCsrftoken()).setUser(id).setUuid(_request_message.getDeviceId()),broadcastCreateResponse.BroadcastId+"").execute();
            }
        }, new BroadcastAddToPostResponse().setCsrfToken(_request_message.getCsrftoken()).setUser(id).setUuid(_request_message.getDeviceId()),broadcastCreateResponse.BroadcastId+"").execute();
    */
    }
    Map<String,String> getHeaders(){
        Map<String, String> header = new HashMap<>();
        header.put("Connection", "close");
        header.put("Accept", "*/*");
        header.put("Content-Type", "application/x-www-form-urlencoded); charset=UTF-8");
        header.put("Cookie2", "$Version=1");
        header.put("Accept-Language", "en-US");
        header.put("User-Agent", InstagramConstants.USER_AGENT);
        return header;
    }
    /*private String getCsrf(){
        Cookie cokie=GetCookies();
        //cokie=null;
        Log.e(TAG, "getOrFetchCsrf: "+(cokie==null));
        final boolean[] ok=new boolean[1];
        if (cokie==null)
        {
            new CsrfRequest(getHeaders(), client, new ICsrfResponse() {
                @Override
                public void isOk(boolean isOk, final String str) {
                    _request_message.setCsrftoken(str);
                    new LoginRequest(_request_message, new ILoginResponse(){

                        @Override
                        public void Login(InstaLoginResponse response) {
                            loginResponse=response;
                            id=response.User.Pk;
                            BroadCast(response.User.Pk);

                        }
                    },getHeaders(),client).execute();
                }
            }).execute();
            cokie=GetCookies();
        }
        return ok[0]?cokie.value():null;
    }*/
    public void Login(final ILoginResponse Iresponse){
        callBack.Start();
        new LoginRequest(_request_message, new ILoginResponse() {
            @Override
            public void Login(InstaLoginResponse response) {
                Log.e(TAG, "Login: "+response.Status.equalsIgnoreCase("ok"));
                if (response.Status.equalsIgnoreCase("ok"))
                    Log.e(TAG, "Login: Replace"+cookiePersistor.ReplaceFile(response.User.UserName));
                Iresponse.Login(response);
            }
        }, getHeaders(), client).execute();
    }
    @Override
    public void StartBroadcast(IAddRtmpURL url) {
        //Login();
        MediaType JSON = MediaType.parse("text/plain; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = RequestBody.create(JSON, getRequestMessage().toString());
        //Crashlytics.log(1,TAG, "StartBroadcast: "+getRequestMessage().toString());
        Request request =new Request.Builder()
                .url(InstagramConstants.BROADCAST_URL)
                .post(body)
                .headers(Headers.of(getHeaders()))
                .build();
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            //Crashlytics.log(1,TAG,"StartBroadcast: "+sonuc);
            //Crashlytics.getInstance().crash();
        } catch (Exception e) {
            //Crashlytics.logException(e);
        }

        BroadcastCreateResponse response=new Gson().fromJson(sonuc,BroadcastCreateResponse.class);
        if(response.Status.equalsIgnoreCase("fail")){
            ((AppCompatActivity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context,"İnstagram Girişi Yapılamadı",Toast.LENGTH_LONG).show();
                }
            });
            return;
        }
        url.AddURl(new UploadMedya.Medyas().setLink(response.UploadUrl.replace("rtmps","rtmp").replace(":443",":80")).setType("Instagram").setYayinName("İnstagram"));
        BrodcastId=response.BroadcastId;
        String str=new BroadcastStart()
                .setCsrfToken(_request_message.getCsrftoken())
                .setUser(user.getUserID())
                .setUuid(_request_message.getDeviceId()).toString();
        StartPublish(response.BroadcastId,str);
    }
    @Override
    public void EndBroadcast() {
        MediaType JSON = MediaType.parse("text/plain; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = RequestBody.create(JSON, getEntBroadcast().toString());
        Request request =new Request.Builder()
                .url(String.format(InstagramConstants.BROADCAST_END_URL,BrodcastId))
                .post(body)
                .headers(Headers.of(getHeaders()))
                .build();
        Log.e(TAG, "doInBackground: "+request.url().toString() );
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "EndBroadcast: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        BaseRequest base=new Gson().fromJson(sonuc,BaseRequest.class);
        if (base.Status.equalsIgnoreCase("fail"))
            return;
        AddToPost(getAddToPost().toString(),BrodcastId);
    }
    private InstaLoginResponse Login(){
        MediaType JSON = MediaType.parse("text/plain; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = null;
        Log.e(TAG, "doInBackground: "+_request_message.toString() );
        try {
            body = RequestBody.create(JSON, InstagramHashUtil.generateSignature(_request_message.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Request request =new Request.Builder()
                .url(InstagramConstants.LOGIN_URL)
                .post(body)
                .headers(Headers.of(getHeaders()))
                .build();
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "doInBackground: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(sonuc,InstaLoginResponse.class);
    }
    private void AddToPost(String str,long brodcastId){
        MediaType JSON = MediaType.parse("text/plain; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = RequestBody.create(JSON, str);
        Request request =new Request.Builder()
                .url(String.format(InstagramConstants.BROADCAST_ADD_TO_POST_URL,brodcastId))
                .post(body)
                .headers(Headers.of(getHeaders()))
                .build();
        String sonuc="";
        Log.e(TAG, "AddToPost: "+request.url().toString() );
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "AddToPost: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void StartPublish(long brodcastid,String str){
        MediaType JSON = MediaType.parse("text/plain; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = RequestBody.create(JSON, str);
        Request request =new Request.Builder()
                .url(String.format(InstagramConstants.BROADCAST_START_URL,brodcastid+""))
                .post(body)
                .headers(Headers.of(getHeaders()))
                .build();
        String sonuc="";
        Log.e(TAG, "StartPublish: "+str );
        Log.e(TAG, "doInBackground: "+request.url().toString() );
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "StartPublish: "+sonuc);
        ///YorumYap(brodcastid);
    }
    private void YorumYap(long brodcastId){
        MediaType JSON = MediaType.parse("application/json; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = RequestBody.create(JSON, getComment().toString());
        Request request =new Request.Builder()
                .url(String.format(InstagramConstants.ADD_COMMENT,brodcastId))
                .post(body)
                .headers(Headers.of(getHeaders()))
                .build();
        Log.e(TAG, "YorumYap: "+getComment().toString() );
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Log.e(TAG, "YorumYap: "+sonuc);
        JsonElement jelement = new JsonParser().parse(sonuc);
        YorumSabitle(brodcastId,jelement.getAsJsonObject().get("Pk").getAsLong()+"");

    }
    private void YorumSabitle(long brodcastId,String commentID){
        JsonObject jsonObject=new JsonObject();
        jsonObject.addProperty("_csrftoken",_request_message.getCsrftoken());
        jsonObject.addProperty("_uuid",user.getUserID());
        jsonObject.addProperty("_uid",_request_message.getDeviceId());
        jsonObject.addProperty("comment_id",commentID);
        jsonObject.addProperty("offset_to_video_start",0);
        MediaType JSON = MediaType.parse("text/plain; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request =new Request.Builder()
                .url(String.format(InstagramConstants.PIN_COMMENT,brodcastId))
                .post(body)
                .headers(Headers.of(getHeaders()))
                .build();
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Log.e(TAG, "YorumSabitle: "+sonuc );

    }
    private BroadcastComment getComment(){
        return new BroadcastComment.Builder()
                .setComment_text("deneme")
                .setCsrf_token(_request_message.getCsrftoken())
                .setUid(""+user.getUserID())
                .setUuid(_request_message.getDeviceId())
                .Build();
    }
    private Cookie GetCookies(){
        for ( Cookie item: client.cookieJar().loadForRequest(HttpUrl.parse(InstagramConstants.LOGIN_URL))) {
            Log.e(TAG, "GetCookies: "+item.name() +" : "+item.value());
            if (item.name().equalsIgnoreCase("csrftoken"))
                return item;
        }
        return null;
    }
    private BroadcastRequestMessage getRequestMessage(){
        return new BroadcastRequestMessage()
                .setBroadcast_message("CanliYayin")
                .set_csrftoken(_request_message.getCsrftoken())
                .set_uid(user.getUserID())
                .setPreview_height(1080)
                .setPreview_width(720)
                .set_uuid(_request_message.getDeviceId());
    }
    private BroadcastEnd getEntBroadcast(){
        return new BroadcastEnd()
                .setCsrfToken(_request_message.getCsrftoken())
                .setUser(user.getUserID())
                .setUuid(_request_message.getDeviceId());
    }
    private BroadcastAddToPostResponse getAddToPost(){
        return new BroadcastAddToPostResponse()
                .setCsrfToken(_request_message.getCsrftoken())
                .setUser(user.getUserID())
                .setUuid(_request_message.getDeviceId());
    }
    public interface CallBack{
        void Start();
        void Finish(BroadcastCreateResponse requestMessage);
    }
}