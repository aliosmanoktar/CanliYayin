package net.catsbilisim.canliyayin.Api.InstagramApi.Connection;

import android.os.AsyncTask;
import android.util.Log;

import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastAddToPostResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.InstagramConstants;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.Interface.IBroadcastAddToPost;

import java.io.IOException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddToPostRequest extends AsyncTask<String,String,String> {
    private OkHttpClient client;
    private Map<String,String> header;
    private IBroadcastAddToPost callBack;
    private BroadcastAddToPostResponse add;
    private String broadCastid;
    private String TAG = getClass().getName();

    public AddToPostRequest(OkHttpClient client, Map<String, String> header, IBroadcastAddToPost callBack, BroadcastAddToPostResponse add, String broadCastid) {
        this.client = client;
        this.header = header;
        this.callBack = callBack;
        this.add = add;
        this.broadCastid = broadCastid;
    }

    @Override
    protected String doInBackground(String... strings) {
        MediaType JSON = MediaType.parse("text/plain; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = RequestBody.create(JSON, add.toString());
        Request request =new Request.Builder()
                .url(String.format(InstagramConstants.BROADCAST_ADD_TO_POST_URL,broadCastid))
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
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        callBack.finish();
    }
}
