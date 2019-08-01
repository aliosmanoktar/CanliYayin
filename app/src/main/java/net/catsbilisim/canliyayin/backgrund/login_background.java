package net.catsbilisim.canliyayin.backgrund;

import android.os.AsyncTask;

import net.catsbilisim.canliyayin.Preferences.ILogin;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class login_background extends AsyncTask<String,String,String> {
    ILogin login;
    String TAG=getClass().getName();
    public login_background(ILogin login){
        this.login=login;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        Fabric.getLogger().e(TAG,"doinBackground "+url);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .build();
        OkHttpClient client = httpClient.build();
        String sonuc="";
        try{
            Response response = client.newCall(request).execute();
            sonuc = response.body().string();
        }catch (Exception ex){
            Fabric.getLogger().e(TAG,"doinBakgcround",ex);
            ex.printStackTrace();
            sonuc=null;
        }
        return sonuc;
    }

    @Override
    protected void onPostExecute(String s) {
        login.login(s);
    }
}
