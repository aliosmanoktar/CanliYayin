package net.catsbilisim.canliyayin.Api.InstagramApi.Connection;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastStart;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcatStartResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.InstagramConstants;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.Interface.IBroadcastStart;
import java.io.IOException;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class StartBroadcastRequest extends AsyncTask<String, String,BroadcatStartResponse> {
    private OkHttpClient client;
    private Map<String,String> header;
    private IBroadcastStart callBack;
    private BroadcastStart start;
    private String broadCastid;
    private String TAG = getClass().getName();
    public StartBroadcastRequest(OkHttpClient client, Map<String, String> header, IBroadcastStart callBack, BroadcastStart start, String broadCastid) {
        this.client = client;
        this.header = header;
        this.callBack = callBack;
        this.start = start;
        this.broadCastid = broadCastid;
    }

    @Override
    protected BroadcatStartResponse doInBackground(String... strings) {
        MediaType JSON = MediaType.parse("text/plain; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = RequestBody.create(JSON, start.toString());
        Request request =new Request.Builder()
                .url(String.format(InstagramConstants.BROADCAST_START_URL,broadCastid))
                .post(body)
                .headers(Headers.of(header))
                .build();
        String sonuc="";
        Log.e(TAG, "doInBackground: "+request.url().toString() );
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "doInBackground: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Gson().fromJson(sonuc,BroadcatStartResponse.class);
    }

    @Override
    protected void onPostExecute(BroadcatStartResponse broadcatStartResponse) {
        callBack.Start(true);
    }
}
