package net.catsbilisim.canliyayin.backgrund;

import android.os.AsyncTask;
import android.util.Log;
import net.catsbilisim.canliyayin.Preferences.Instagram_yayin_start;
import io.fabric.sdk.android.Fabric;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class InstagramYayin_background extends AsyncTask<String,String,String> {
    Instagram_yayin_start start;
    String TAG=getClass().getName();
    public InstagramYayin_background(Instagram_yayin_start start) {
        this.start=start;
    }

    @Override
    protected String doInBackground(String... strings) {
        String url=strings[0];
        Log.e(TAG,"doinBackground "+url);
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url(url)
                .post(FormBody.create(MediaType.parse("application/json"),strings[1]))
                .header("Accept","application/json")
                .build();
        String sonuc;
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
        super.onPostExecute(s);
        start.Start(s);
    }
}
