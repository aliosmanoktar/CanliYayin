package net.catsbilisim.canliyayin.Api.InstagramApi.Connection;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastCreateResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastRequestMessage;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.InstagramConstants;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.Interface.IBroadcastResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.User.UserRequestMessage;
import java.io.IOException;
import java.util.Map;
import okhttp3.*;

public class BroadCastRequest extends AsyncTask<String,String,BroadcastCreateResponse> {
    private BroadcastRequestMessage requestMessage;
    private Map<String,String> header;
    private OkHttpClient client;
    private IBroadcastResponse Iresponse;
    private UserRequestMessage user;
    private String TAG=getClass().getName();

    public BroadCastRequest(UserRequestMessage user,BroadcastRequestMessage requestMessage, Map<String, String> header, OkHttpClient client, IBroadcastResponse iresponse) {
        this.requestMessage = requestMessage;
        this.header = header;
        this.client = client;
        this.Iresponse = iresponse;
        this.user=user;
    }

    @Override
    protected BroadcastCreateResponse doInBackground(String... strings) {
        MediaType JSON = MediaType.parse("text/plain; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = RequestBody.create(JSON, requestMessage.toString());
        Request request =new Request.Builder()
                .url(InstagramConstants.BROADCAST_URL)
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
        return new Gson().fromJson(sonuc,BroadcastCreateResponse.class);
    }
    @Override
    protected void onPostExecute(BroadcastCreateResponse response) {
        Iresponse.finish(response);
    }

}