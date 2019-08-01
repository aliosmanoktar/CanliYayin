package net.catsbilisim.canliyayin.backgrund;

import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeleteInstagramYayin_background extends AsyncTask<String,String,String> {
    String TAG=getClass().getName();
    @Override
    protected String doInBackground(String... strings) {
        String url=strings[0];
        Log.e(TAG,"doinBackground "+url);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .readTimeout(60,TimeUnit.SECONDS)
                .connectTimeout(45,TimeUnit.SECONDS);
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .header("Accept","application/json")
                .build();
        OkHttpClient client = httpClient.build();
        String sonuc="";
        try{
            Response response = client.newCall(request).execute();
            sonuc = response.body().string();
            Log.e(TAG, "doInBackground: "+sonuc );
        }catch (Exception ex){
            Fabric.getLogger().e(TAG,"doinBakgcround",ex);
            ex.printStackTrace();
            sonuc=null;
        }
        return sonuc;
    }
}
