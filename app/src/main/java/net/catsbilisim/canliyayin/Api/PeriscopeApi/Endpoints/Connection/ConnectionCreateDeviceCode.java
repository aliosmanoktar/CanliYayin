package net.catsbilisim.canliyayin.Api.PeriscopeApi.Endpoints.Connection;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.IPeriscopeFinish;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CreateDeviceCodeResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.PeriscopeConstant;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectionCreateDeviceCode extends AsyncTask<String,String, CreateDeviceCodeResponse> {
    IPeriscopeFinish finish;
    String TAG=getClass().getName();

    public ConnectionCreateDeviceCode(IPeriscopeFinish finish) {
        this.finish = finish;
    }

    @Override
    protected CreateDeviceCodeResponse doInBackground(String... strings) {
        OkHttpClient client=new OkHttpClient.Builder().build();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(PeriscopeConstant.CLIENT_ID_KEY, PeriscopeConstant.CLIENT_ID);
        Log.i(TAG, "doInBackground: "+jsonObject.toString());
        RequestBody body = RequestBody.create(MediaType.parse(PeriscopeConstant.TEXT_PLAIN), jsonObject.toString());
        Request request =new Request.Builder()
                .url(PeriscopeConstant.CreateDevice_URL)
                .post(body)
                .addHeader("Content-Type","application/json")
                .addHeader("User-Agent",PeriscopeConstant.USER_AGENT)
                .build();
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "doInBackground: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(sonuc, CreateDeviceCodeResponse.class);
    }

    @Override
    protected void onPostExecute(CreateDeviceCodeResponse createDeviceCodeResponse) {
        finish.Finish(createDeviceCodeResponse);
    }
}
