package net.catsbilisim.canliyayin.Api.PeriscopeApi.Endpoints.Connection;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.IPeriscopeFinish;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CreateBroadcastResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.PeriscopeConstant;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class ConnectionCreateBroadcast extends AsyncTask<String,String, CreateBroadcastResponse> {
    String TAG = getClass().getName();
    IPeriscopeFinish finish;

    public ConnectionCreateBroadcast(IPeriscopeFinish finish) {
        this.finish = finish;
    }

    @Override
    protected CreateBroadcastResponse doInBackground(String... strings) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient client=new OkHttpClient.Builder().addInterceptor(logging).build();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(PeriscopeConstant.REGION_KEY, strings[0]);
        jsonObject.addProperty(PeriscopeConstant.IS_360_KEY, false);
        jsonObject.addProperty(PeriscopeConstant.IS_LOW_LATENCY, false);
        Log.e(TAG, "doInBackground: "+jsonObject.toString() );
        RequestBody body = RequestBody.create(MediaType.parse(PeriscopeConstant.TEXT_PLAIN), jsonObject.toString());
        Request request =new Request.Builder()
                .url(PeriscopeConstant.CreateBroadcast_URL)
                .post(body)
                .addHeader("Content-Type","application/json")
                .addHeader("User-Agent",PeriscopeConstant.USER_AGENT)
                .addHeader("Authorization",strings[1])
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

    @Override
    protected void onPostExecute(CreateBroadcastResponse createBroadcastResponse) {
        finish.Finish(createBroadcastResponse);
    }
}
