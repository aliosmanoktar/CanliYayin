package net.catsbilisim.canliyayin.Api.PeriscopeApi.Endpoints.Connection;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.IPeriscopeFinish;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CheckDeviceCodeResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.PeriscopeConstant;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectionCheckDeviceCode extends AsyncTask<String,String, CheckDeviceCodeResponse> {
    String TAG=getClass().getName();
    String deviceCode;
    IPeriscopeFinish finish;

    public ConnectionCheckDeviceCode(String deviceCode, IPeriscopeFinish finish) {
        this.deviceCode = deviceCode;
        this.finish = finish;
    }

    @Override
    protected CheckDeviceCodeResponse doInBackground(String... strings) {
        OkHttpClient client=new OkHttpClient.Builder().build();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(PeriscopeConstant.CLIENT_ID_KEY, PeriscopeConstant.CLIENT_ID);
        jsonObject.addProperty(PeriscopeConstant.DEVICE_CODE_KEY, deviceCode);
        RequestBody body = RequestBody.create(MediaType.parse(PeriscopeConstant.TEXT_PLAIN), jsonObject.getAsString());
        Request request =new Request.Builder()
                .url(PeriscopeConstant.CheckDevice_URL)
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
        return new Gson().fromJson(sonuc, CheckDeviceCodeResponse.class);
    }

    @Override
    protected void onPostExecute(CheckDeviceCodeResponse checkDeviceCodeResponse) {
        finish.Finish(checkDeviceCodeResponse);
    }
}