package net.catsbilisim.canliyayin.backgrund;

import android.os.AsyncTask;
import android.util.Log;

import net.catsbilisim.canliyayin.Preferences.IDefaultBackground;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DeleteMedya_background extends AsyncTask<String,String,Integer> {
    String TAG = getClass().getName();
    IDefaultBackground background;
    public DeleteMedya_background(IDefaultBackground background){
        this.background=background;
    }

    protected Integer doInBackground(String... strings) {
        String url = strings[0];
        Log.e(TAG,"doinBackground "+url);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .delete()
                .build();
        OkHttpClient client = httpClient.build();
        Integer sonuc;
        try{
            Response response = client.newCall(request).execute();
            sonuc = Integer.parseInt(response.body().string());
        }catch (Exception ex){
            ex.printStackTrace();
            sonuc=-1;
        }
        return sonuc;
    }

    @Override
    protected void onPostExecute(Integer ınteger) {
        background.finish(ınteger);
    }
}