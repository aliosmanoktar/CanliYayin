package net.catsbilisim.canliyayin.Api.InstagramApi.Connection;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;

import net.catsbilisim.canliyayin.Api.ApiResultSingle;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.InstagramConstants;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.InstagramHashUtil;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.Interface.ILoginResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.User.InstaLoginResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.User.UserRequestMessage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import okhttp3.*;

public class LoginRequest extends AsyncTask<String,String, InstaLoginResponse> {

    private UserRequestMessage requestMessage;
    private ApiResultSingle<InstaLoginResponse> response;
    private Map<String,String> header;
    private OkHttpClient client;
    private String TAG = getClass().getName();

    public LoginRequest(UserRequestMessage requestMessage, ApiResultSingle<InstaLoginResponse> response, Map<String, String> header, OkHttpClient client) {
        this.requestMessage = requestMessage;
        this.response = response;
        this.header = header;
        this.client = client;
    }

    @Override
    protected InstaLoginResponse doInBackground(String... strings) {
        MediaType JSON = MediaType.parse("text/plain; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = null;
        Log.e(TAG, "doInBackground: "+requestMessage.toString() );
        try {
            body = RequestBody.create(JSON, InstagramHashUtil.generateSignature(requestMessage.toString()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Request request =new Request.Builder()
                .url(InstagramConstants.LOGIN_URL)
                .post(body)
                .headers(Headers.of(header))
                .build();
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "doInBackground: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(sonuc,InstaLoginResponse.class);
    }

    @Override
    protected void onPostExecute(InstaLoginResponse instaLoginResponse) {
        response.Finish(instaLoginResponse);
    }
}