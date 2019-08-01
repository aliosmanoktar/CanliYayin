package net.catsbilisim.canliyayin.Api.InstagramApi.Connection;

import android.os.AsyncTask;
import android.util.Log;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastEnd;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.InstagramConstants;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.Interface.IBroadcastEnd;
import java.io.IOException;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EndStreamRequest extends AsyncTask<String,String,String> {
    private OkHttpClient client;
    private Map<String,String> header;
    private IBroadcastEnd callBack;
    private BroadcastEnd end;
    private String broadCastid;
    private String TAG = getClass().getName();

    public EndStreamRequest(OkHttpClient client, Map<String, String> header, IBroadcastEnd callBack, BroadcastEnd end, String broadCastid) {
        this.client = client;
        this.header = header;
        this.callBack = callBack;
        this.end = end;
        this.broadCastid = broadCastid;
    }

    @Override
    protected String doInBackground(String... strings) {
        MediaType JSON = MediaType.parse("text/plain; charset=ISO-8859-1,Content-Length: 589,Chunked: false");
        RequestBody body = RequestBody.create(JSON, end.toString());
        Request request =new Request.Builder()
                .url(String.format(InstagramConstants.BROADCAST_END_URL,broadCastid))
                .post(body)
                .headers(Headers.of(header))
                .build();
        Log.e(TAG, "doInBackground: "+request.url().toString() );
        String sonuc="";
        try {
            Response response=client.newCall(request).execute();
            sonuc=response.body().string();
            Log.e(TAG, "doInBackground: "+sonuc );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sonuc;
    }

    @Override
    protected void onPostExecute(String s) {
        callBack.Finish();
    }
}
