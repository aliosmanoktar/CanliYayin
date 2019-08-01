package net.catsbilisim.canliyayin.backgrund;

import android.os.AsyncTask;
import android.util.Log;

import net.catsbilisim.canliyayin.Preferences.IYayinKontrol;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class yayinKontrol_background extends AsyncTask<String,String,Boolean> {
    IYayinKontrol kontrol;
    String TAG=getClass().getName();
    public yayinKontrol_background(IYayinKontrol kontrol) {
        this.kontrol = kontrol;
    }
    @Override
    protected Boolean doInBackground(String... strings) {
        String url = strings[0];
        Log.e(TAG,"doinBackground "+url);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .build();
        OkHttpClient client = httpClient.build();
        Boolean sonuc;
        try{
            Response response = client.newCall(request).execute();
            sonuc = Boolean.parseBoolean(response.body().string());
        }catch (Exception ex){
            ex.printStackTrace();
            sonuc=false;
        }
        return sonuc;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        Log.e(TAG, "onPostExecute: "+aBoolean );
        kontrol.Kontrol(aBoolean);
    }
}
