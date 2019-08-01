package net.catsbilisim.canliyayin.backgrund;

import android.os.AsyncTask;
import android.util.Log;

import net.catsbilisim.canliyayin.Preferences.IDefaultBackground;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UpdateMedya_background extends AsyncTask<String,String,Integer> {
    String TAG=getClass().getName();
    IDefaultBackground background;

    public UpdateMedya_background(IDefaultBackground background) {
        this.background = background;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        String url = strings[0];
        Log.e(TAG,"doinBackground "+url);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .post(new FormBody.Builder().build())
                .build();
        OkHttpClient client = httpClient.build();
        Integer sonuc;
        try{
            Response response = client.newCall(request).execute();
            Log.e(TAG, "doInBackground: "+response.body().string() );
            sonuc=200;
        }catch (Exception ex){
            ex.printStackTrace();
            sonuc=-1;
        }
        return sonuc;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        background.finish(integer);
    }
}
