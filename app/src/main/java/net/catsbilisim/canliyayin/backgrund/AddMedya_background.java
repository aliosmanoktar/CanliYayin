package net.catsbilisim.canliyayin.backgrund;

import android.os.AsyncTask;
import android.util.Log;

import net.catsbilisim.canliyayin.Preferences.IAddMedya;
import net.catsbilisim.canliyayin.Preferences.SosyalMedya;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddMedya_background extends AsyncTask<String,String,Integer> {
    String TAG = getClass().getName();
    IAddMedya add;
    SosyalMedya medya;
    public AddMedya_background(IAddMedya add) {
        this.add = add;
    }
    @Override
    protected Integer doInBackground(String... strings) {
        String url = strings[0];
        medya = new SosyalMedya(strings[1],strings[2]);
        Log.e(TAG,"doinBackground "+url);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        Request request = new Request.Builder()
                .url(url)
                .post(new FormBody.Builder().build())
                .header("Accept","application/json")
                .build();
        OkHttpClient client = httpClient.build();
        Integer sonuc;
        try{
            Response response = client.newCall(request).execute();
            sonuc = Integer.parseInt(response.body().string());
            if (response.code()!=200)
                sonuc=-1;
        }catch (Exception ex){
            ex.printStackTrace();
            sonuc=-1;
        }
        return sonuc;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (add!=null)
            add.Finish(integer,medya);
    }
}
