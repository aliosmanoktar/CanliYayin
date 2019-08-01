package net.catsbilisim.canliyayin.Api.YoutubeApi;


import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;
import com.google.android.gms.common.Scopes;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.CdnSettings;
import com.google.api.services.youtube.model.IngestionInfo;
import com.google.api.services.youtube.model.LiveBroadcast;
import com.google.api.services.youtube.model.LiveBroadcastContentDetails;
import com.google.api.services.youtube.model.LiveBroadcastListResponse;
import com.google.api.services.youtube.model.LiveBroadcastSnippet;
import com.google.api.services.youtube.model.LiveBroadcastStatus;
import com.google.api.services.youtube.model.LiveStream;
import com.google.api.services.youtube.model.LiveStreamSnippet;
import com.google.api.services.youtube.model.MonitorStreamInfo;
import com.google.gson.Gson;
import net.catsbilisim.canliyayin.Api.IAddRtmpURL;
import net.catsbilisim.canliyayin.Api.UploadMedya;
import net.catsbilisim.canliyayin.Api.VideoEndEndpoint;
import net.catsbilisim.canliyayin.Api.VideoStartEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;

public class YoutubeApi implements VideoEndEndpoint, VideoStartEndpoint {

    private static String TAG="YoutubeApi";
    private YouTube youTube;
    private String BroadcastTitle="Test Yayini Son";
    private LiveBroadcast mbroadcast;
    private LiveStream mstream;
    private String BroadcastKind="youtube#liveBroadcast";
    private String StreamKind="youtube#liveStream";
    public static final String startStream="live";
    public static final String endStream="complete";
    private AppCompatActivity activity;

    public YoutubeApi(YouTube youTube,AppCompatActivity activity){
        this.youTube=youTube;
        this.activity=activity;
    }

    public String CreteBroadCast() throws IOException {
        Log.e(TAG, "CreteBroadCast: Basladi" );
        YouTube.LiveBroadcasts.Insert liveBroadcastInsert =
                youTube.liveBroadcasts().insert("snippet,status,contentDetails", getBroadcast());
        LiveBroadcast returnedBroadcast = liveBroadcastInsert.execute();
        YouTube.LiveStreams.Insert liveStreamInsert =
                youTube.liveStreams().insert("snippet,cdn", getLiveStream());
        mstream  = liveStreamInsert.execute();
        YouTube.LiveBroadcasts.Bind liveBroadcastBind =
                youTube.liveBroadcasts().bind(returnedBroadcast.getId(), "id,contentDetails");
        liveBroadcastBind.setStreamId(mstream.getId());
        mbroadcast = liveBroadcastBind.execute();
        Log.e(TAG, "CreteBroadCast: "+getStreamUrl(mstream));
        return getStreamUrl(mstream);
    }

    public void BroadcastTransition(String status) throws IOException {
        Log.e(TAG, "BroadcastTransition: "+mbroadcast.getId());
        LiveBroadcast transition=youTube.liveBroadcasts().transition(status,mbroadcast.getId(),"status").execute();
        Log.e(TAG, "BroadcastTransition: "+new Gson().toJson(transition));
    }

    private LiveBroadcast getBroadcast(){
        return new LiveBroadcast()
                .setKind(BroadcastKind)
                .setStatus(getLiveBroadcastStatus())
                .setSnippet(getBroadcastSnippet())
                .setContentDetails(getBroadcastContentDetails());
    }

    private LiveBroadcastSnippet getBroadcastSnippet(){
        return new LiveBroadcastSnippet()
                .setTitle(BroadcastTitle+" : "+(Calendar.getInstance().getTime().getMinutes()))
                .setScheduledStartTime(getNowDate());
    }

    private LiveBroadcastContentDetails getBroadcastContentDetails(){
        return new LiveBroadcastContentDetails()
                .setMonitorStream(new MonitorStreamInfo()
                .setEnableMonitorStream(false));
    }

    private LiveBroadcastStatus getLiveBroadcastStatus(){
        return new LiveBroadcastStatus().setPrivacyStatus("public");
    }

    private LiveStream getLiveStream(){
        return new LiveStream()
                .setCdn(getStreamCdn())
                .setSnippet(getStreamSnippet())
                .setKind(StreamKind);
    }

    private LiveStreamSnippet getStreamSnippet(){
        return new LiveStreamSnippet()
                .setTitle(BroadcastTitle);
    }

    private CdnSettings getStreamCdn(){
        return new CdnSettings()
                .setFormat("480p")
                .setIngestionType("rtmp");
    }

    private String getStreamUrl(LiveStream stream){
        IngestionInfo ingestionInfo = stream.getCdn().getIngestionInfo();
        return ingestionInfo.getIngestionAddress()+"/"+ingestionInfo.getStreamName();
    }

    private DateTime getNowDate(){
        return new DateTime(Calendar.getInstance().getTime(),Calendar.getInstance().getTimeZone());
    }
    public void CheckAuthorization() throws IOException {
        Log.e(TAG, "CheckAuthorization: Calisti" );
        YouTube.LiveBroadcasts.List liveBroadcastRequest = youTube
                .liveBroadcasts().list("id,snippet,contentDetails");
        liveBroadcastRequest.setBroadcastStatus("upcoming");
        //List request is executed and list of broadcasts are returned
        LiveBroadcastListResponse returnedListResponse = liveBroadcastRequest.execute();
        Log.e(TAG, "CheckAuthorization: Tamamlandı" );
    }
    @Override
    public void EndBroadcast() {
        try {
            BroadcastTransition(endStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void StartBroadcast(IAddRtmpURL url) {
        try {
            String link =CreteBroadCast();
            url.AddURl(new UploadMedya.Medyas().setYayinName("Youtube").setType("Youtube").setLink(link));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(20000);
                        BroadcastTransition(startStream);
                    } catch (IOException e) {
                        Log.e(TAG, "run: "+e.getMessage());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "StartBroadcast: ");
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity,"Youtube Yayını Başlatılamadı",Toast.LENGTH_LONG).show();
                }
            });
        }

    }


    public static class Builder{
        private static final String[] SCOPES = { Scopes.PROFILE, YouTubeScopes.YOUTUBE};
        private GoogleAccountCredential credential;
        private AppCompatActivity activity;
        public Builder setCredential(GoogleAccountCredential credential) {
            this.credential = credential;
            return this;
        }
        public Builder setAccountName(String name, AppCompatActivity context){
             credential = GoogleAccountCredential.usingOAuth2(
                    context, Arrays.asList(SCOPES))
                    .setBackOff(new ExponentialBackOff()).setSelectedAccountName(name);
             this.activity=context;
             return this;
        }
        public YoutubeApi Build(){
            return new YoutubeApi(new YouTube.Builder(AndroidHttp.newCompatibleTransport(),
                    new GsonFactory(), credential).setApplicationName("CanlıYayın").build(),activity);
        }
    }

}