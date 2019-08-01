package net.catsbilisim.canliyayin.Api.PeriscopeApi.Endpoints.Connection;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.IPeriscopeFinish;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.PublishBroadcastResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.PeriscopeConstant;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class ConnectionPublishBroadcast extends AsyncTask<String,String, PublishBroadcastResponse> {
    IPeriscopeFinish finish;
    String TAG=getClass().getName();

    public ConnectionPublishBroadcast(IPeriscopeFinish finish) {
        this.finish = finish;
    }

    @Override
    protected PublishBroadcastResponse doInBackground(final String... strings) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient client=new OkHttpClient.Builder().addInterceptor(logging).build();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(PeriscopeConstant.BROADCAST_ID_KEY, strings[0]);
        jsonObject.addProperty(PeriscopeConstant.TITLE_KEY, "test");
        jsonObject.addProperty(PeriscopeConstant.SHOULD_NOT_TWEET_KEY, true);
        jsonObject.addProperty(PeriscopeConstant.LOCALE_KEY, strings[1]);
        jsonObject.addProperty(PeriscopeConstant.ENABLE_SUPER_HEARTS, true);
        System.out.println(jsonObject.toString());
        RequestBody body = RequestBody.create(MediaType.parse(PeriscopeConstant.TEXT_PLAIN), jsonObject.toString());
        Request request =new Request.Builder()
                .url(PeriscopeConstant.PublishBroadcas_URL)
                .post(body)
                .addHeader("Content-Type","application/json")
                .addHeader("User-Agent",PeriscopeConstant.USER_AGENT)
                .addHeader("Authorization",strings[2])
                .build();
        String sonuc="";
        Log.e(TAG, "doInBackground: Calisti" );
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "doInBackground: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(sonuc, PublishBroadcastResponse.class);
    }

    @Override
    protected void onPostExecute(PublishBroadcastResponse publishBroadcastResponse) {
        finish.Finish(publishBroadcastResponse);
    }
}
