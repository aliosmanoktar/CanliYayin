package net.catsbilisim.canliyayin.Api.InstagramApi.Connection;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.InstagramConstants;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.InstagramGenericUtil;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.Interface.ICsrfResponse;
import java.io.IOException;
import java.util.Map;
import okhttp3.Cookie;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CsrfRequest extends AsyncTask<String,String,String> {
    private ICsrfResponse _response;
    private Map<String,String> header;
    private OkHttpClient client;
    private HttpUrl url;
    private String TAG = getClass().getName();

    public CsrfRequest(Map<String, String> header, OkHttpClient client, ICsrfResponse csrfResponse) {
        this.header = header;
        this.client = client;
        this._response=csrfResponse;
    }

    @Override
    protected String doInBackground(String... strings) {
        Request request = new Request.Builder()
                .url(InstagramConstants.CSRF_URL+ InstagramGenericUtil.generateUuid(false))
                .get()
                .headers(Headers.of(header))
                .build();
        url=request.url();
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "doInBackground: "+sonuc);
        JsonElement jelement = new JsonParser().parse(sonuc);
        JsonObject jobject = jelement.getAsJsonObject();
        return jobject.get("status").getAsString();
    }

    @Override
    protected void onPostExecute(String s) {
        _response.isOk(s.equalsIgnoreCase("ok"),getCsrf());
    }

    private String getCsrf(){
        Cookie cokie=GetCookies();
        return cokie!=null ?cokie.value():"null";
    }
    private Cookie GetCookies(){
        for ( Cookie item: client.cookieJar().loadForRequest(url)) {
            if (item.name().equalsIgnoreCase("csrftoken"))
                return item;
        }
        return null;
    }

}
