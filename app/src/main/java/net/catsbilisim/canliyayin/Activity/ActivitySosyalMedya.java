package net.catsbilisim.canliyayin.Activity;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.google.android.gms.common.Scopes;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.gson.Gson;

import net.catsbilisim.canliyayin.Api.ApiResultSingle;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.User.InstaLoginResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CheckDeviceCodeResponse;
import net.catsbilisim.canliyayin.Api.YoutubeApi.YoutubeApi;
import net.catsbilisim.canliyayin.DataBase.InstagramUser;
import net.catsbilisim.canliyayin.DataBase.PeriscopeUser;
import net.catsbilisim.canliyayin.DataBase.YoutubeUser;
import net.catsbilisim.canliyayin.DataBase.veritabani;
import net.catsbilisim.canliyayin.Preferences.*;
import net.catsbilisim.canliyayin.R;
import net.catsbilisim.canliyayin.adapter.Helper.ReyclerItemTouchHelper;
import net.catsbilisim.canliyayin.adapter.Helper.ReyclerItemTouchHelperListener;
import net.catsbilisim.canliyayin.adapter.adapter_canliyayinlar;
import net.catsbilisim.canliyayin.adapter.custom_dialog;
import net.catsbilisim.canliyayin.backgrund.AddMedya_background;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ActivitySosyalMedya extends AppCompatActivity implements IAddMedya, ReyclerItemTouchHelperListener {

    private LinearLayout root_view;
    private String TAG = getClass().getName();
    private Dialog dialog=null;
    private RecyclerView recyclerView;
    private adapter_canliyayinlar adapter;
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_ACCOUNT_UPDATE = 1003;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private String[] permission = new String[]{Manifest.permission.GET_ACCOUNTS};
    private GoogleAccountCredential mCredential;
    private YoutubeUser user=null;
    private static final String[] SCOPES = { Scopes.PROFILE, YouTubeScopes.YOUTUBE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sosyal_medya);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ImageView back = findViewById(R.id.back);
        ImageView addSosyal = findViewById(R.id.AddSosyalMedya);
        addSosyal.setOnClickListener(addSosyalClick);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*addSosyal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/
        root_view=findViewById(R.id.root_view);
        recyclerView=findViewById(R.id.list_sosyalMedya);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, 0));
        ItemTouchHelper.SimpleCallback item = new ReyclerItemTouchHelper(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT,this);
        new ItemTouchHelper(item).attachToRecyclerView(recyclerView);
        ListEdit();

    }
    View.OnClickListener addSosyalClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PopupMenu popup = new PopupMenu(ActivitySosyalMedya.this,v);
            popup.getMenuInflater()
                    .inflate(R.menu.sosyal_medya_menu, popup.getMenu());
            popup.setOnMenuItemClickListener(menuItemClickListener);
            popup.show();
        }
    };

    PopupMenu.OnMenuItemClickListener menuItemClickListener=new PopupMenu.OnMenuItemClickListener(){
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId()==R.id.menu_instagram){
                dialog=new custom_dialog().AddInstagram(ActivitySosyalMedya.this, new IAddInstagram() {
                    @Override
                    public void Succes(boolean ok, InstaLoginResponse response) {
                        if (ok){
                            dialog.dismiss();
                            boolean sonuc=new veritabani(getBaseContext()).AddInstagramUser(new InstagramUser().setUserID(response.User.Pk).setName(response.User.UserName));
                            if (sonuc)
                                ShowSuccesDialog("İnstagram Girişi Başarılı bir şekilde gerçekleştirildi");

                            else
                                ShowSuccesDialog("Eklemek istediğiniz kullanıcı zaten mevcut");
                            ListEdit();
                        }
                        else {
                            ShowErrorDialog("İnstagram girişi yapılamadı lütfen bilgilerinizi kontrol ediniz");
                        }
                    }
                });
                dialog.show();
            }
            else if (item.getItemId()==R.id.menu_periscope)
            {
                new custom_dialog().AddPeriscope(ActivitySosyalMedya.this, new ApiResultSingle<CheckDeviceCodeResponse>() {
                    @Override
                    public void Finish(CheckDeviceCodeResponse value) {
                        CheckDeviceCodeResponse codeResponse = (CheckDeviceCodeResponse)value;
                        boolean sonuc= new veritabani(getBaseContext()).AddPeriscopeUser(new PeriscopeUser().setAccesToken(codeResponse.access_token).setTokenType(codeResponse.token_type).setUserName(codeResponse.user.username));
                        Log.e(TAG, "Finish: "+new Gson().toJson(codeResponse) );
                        if (sonuc)
                            ShowSuccesDialog(codeResponse.user.username+" Başarılı bir şekilde giriş gerçekleştirildi");
                        else
                            ShowErrorDialog("Eklemek istediğiniz kullanıcı zaten mevcut");
                        ListEdit();
                    }
                });
            }
            else if (item.getItemId()==R.id.menu_youtube){
                ActivityCompat.requestPermissions(ActivitySosyalMedya.this,
                        permission,1);
            }
            else{
                dialog=new custom_dialog().AddMedya(ActivitySosyalMedya.this,new ISosyalKayitDialog(){
                    @Override
                    public void Click(String name, String Link) {
                        kullanici kullanici = new preferences(getBaseContext()).getKullanici();
                        new AddMedya_background(ActivitySosyalMedya.this).execute(String.format(preferences.AddYayin,Link,kullanici.getUid(),name),name,Link);
                    }
                });
                dialog.show();
            }
            return false;
        }
    };
    @Override
    public void Finish(Integer ResultCode,SosyalMedya medya) {
        Log.e(TAG, "IPeriscopeFinish: "+ResultCode );
        dialog.dismiss();
        if (ResultCode==-1){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setMessage("İşlem Sırasında Hata Oluştu");
            dialog.setTitle("Hata Oluştu");
            dialog.create().show();
        }else{
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setMessage("İşlem Başarılı");
            dialog.setTitle("Başarılı Bir şekilde medya Eklenmiştir");
            dialog.create().show();
            medya.setId(ResultCode);
            //new preferences(getBaseContext()).AddYayin(medya);
            ListEdit();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults[0]== PackageManager.PERMISSION_GRANTED){
            if (requestCode==1){
                mCredential = GoogleAccountCredential.usingOAuth2(
                        getApplicationContext(), Arrays.asList(SCOPES))
                        .setBackOff(new ExponentialBackOff());
                Youtube(REQUEST_ACCOUNT_PICKER);

            }
            else if (requestCode==2){
                mCredential = GoogleAccountCredential.usingOAuth2(
                        getApplicationContext(), Arrays.asList(SCOPES))
                        .setBackOff(new ExponentialBackOff());
                mCredential.setSelectedAccountName(user.getName());
                Youtube(REQUEST_ACCOUNT_UPDATE);
            }
        }else{
            ShowErrorDialog("Kullanıcı izni olmadan youtube girişi yapılamaz");
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case REQUEST_GOOGLE_PLAY_SERVICES:
                System.out.println("Request_google_play_services");
                if (resultCode!=RESULT_OK){
                    ShowErrorDialog("Play Hizmetlerini Yükleyiniz","Play Hizmetleri Gerekli");
                }else{
                    return;
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode==RESULT_OK && data!=null && data.getExtras()!=null){
                    final String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    boolean sonuc =new veritabani(getBaseContext()).GetYoutubeUser(name)==null;
                    if (sonuc)
                    {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                YoutubeApi api = getYoutubeApi(name);
                                try {
                                    api.CheckAuthorization();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            ShowSuccesDialogYoutube(name);
                                        }
                                    });
                                    //new veritabani(getBaseContext()).AddYoutubeUser(new YoutubeUser().setName(name));
                                } catch (final UserRecoverableAuthIOException ex){
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            startActivityForResult(ex.getIntent(),REQUEST_AUTHORIZATION);
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }else {
                        ShowErrorDialog("Eklemek istediğiniz kullanıcı zaten mevcut");
                    }
                    ListEdit();
                }
                break;
            case REQUEST_ACCOUNT_UPDATE:
                if (resultCode==RESULT_OK && data!=null && data.getExtras()!=null){
                    final veritabani db =new veritabani(getBaseContext());
                    final String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (db.GetYoutubeUser(name)!=null){
                        ShowErrorDialog("Eklemek istediğiniz kullanıcı zaten mevcut");
                        ListEdit();
                        return;
                    }
                    db.DeleteYoutubeUser(user.getID());

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            YoutubeApi api = getYoutubeApi(name);
                            try {
                                api.CheckAuthorization();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ShowSuccesDialogYoutube(name);
                                    }
                                });
                            } catch (final UserRecoverableAuthIOException ex){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(ex.getIntent(),REQUEST_AUTHORIZATION);
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    ListEdit();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode==RESULT_OK) {
                    String name = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    Log.e(TAG, "onActivityResult: AUTHORİZATİON"+name );
                    veritabani db = new veritabani(getBaseContext());
                    db.AddYoutubeUser(new YoutubeUser().setName(name));
                    ListEdit();
                }
                break;
        }

    }
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, final int position) {
        if (viewHolder instanceof adapter_canliyayinlar.ViewHolder)
        {
            user=null;
            if (direction==ItemTouchHelper.LEFT){
                final Object item = adapter.getItem(position);
                String name="";
                if (item instanceof  YoutubeUser)
                    name=((YoutubeUser)item).getName();
                else if (item instanceof InstagramUser)
                    name=((InstagramUser)item).getName();
                else if (item instanceof  PeriscopeUser)
                    name=((PeriscopeUser)item).getUserName();
                adapter.removeItem(position);
                Snackbar snackbar= Snackbar.make(root_view,name+" Silindi",Snackbar.LENGTH_SHORT);
                snackbar.setAction("Geri Al", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapter.restoreItem(item,position);
                    }
                });
                snackbar.addCallback(new Snackbar.Callback(){
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        super.onDismissed(transientBottomBar, event);
                        if (event != DISMISS_EVENT_ACTION){
                            if (item instanceof  YoutubeUser) {
                                int id = ((YoutubeUser)item).getID();
                                new veritabani(getBaseContext()).DeleteYoutubeUser(id);
                            }else if (item instanceof InstagramUser) {
                                InstagramUser user=((InstagramUser)item);
                                DeleteCookieFile(user.getName());
                                new veritabani(getBaseContext()).DeleteInstagramUser(user.getID());
                            }else if (item instanceof  PeriscopeUser){
                                int id=((PeriscopeUser)item).getID();
                                new veritabani(getBaseContext()).DeletePeriscopeUser(id);
                            }
                        }
                    }
                });
                snackbar.show();
            }
            else if (direction==ItemTouchHelper.RIGHT){
                Object item = adapter.getItem(position);
                if (item instanceof  InstagramUser)
                    UpateInstagram((InstagramUser)item);
                else if (item instanceof YoutubeUser)
                    UpdateYoutube((YoutubeUser)item);
                else if (item instanceof  PeriscopeUser)
                    UpdatePeriscope((PeriscopeUser)item);
            }
        }
    }
    private void UpateInstagram(final InstagramUser user){
        dialog=new custom_dialog().UpdateInstagram(user,ActivitySosyalMedya.this, new IAddInstagram() {
            @Override
            public void Succes(boolean ok, InstaLoginResponse response) {
                if (ok){
                    dialog.dismiss();
                    veritabani db = new veritabani(getBaseContext());
                    db.DeleteInstagramUser(user.getID());
                    db.AddInstagramUser(new InstagramUser().setUserID(response.User.Pk).setName(response.User.UserName));
                    ShowSuccesDialog("İnstagram Girişi Başarılı bir şekilde gerçekleştirildi");
                    ListEdit();
                }
                else {
                    ShowSuccesDialog("İnstagram girişi yapılamadı lütfen bilgilerinizi kontrol ediniz");
                }
            }
        });
        dialog.show();
    }
    private void UpdateYoutube(final YoutubeUser user){
        this.user=user;
        ActivityCompat.requestPermissions(ActivitySosyalMedya.this,
                permission,2);
    }
    private void UpdatePeriscope(final PeriscopeUser user){
        new custom_dialog().AddPeriscope(ActivitySosyalMedya.this, new ApiResultSingle<CheckDeviceCodeResponse>() {
            @Override
            public void Finish(CheckDeviceCodeResponse value) {
                CheckDeviceCodeResponse codeResponse = (CheckDeviceCodeResponse)value;
                ShowSuccesDialog(codeResponse.user.username+" Başarılı bir şekilde giriş gerçekleştirildi");
                Log.e(TAG, "Finish: "+new Gson().toJson(codeResponse) );
                veritabani db =new veritabani(getBaseContext());
                db.DeletePeriscopeUser(user.getID());
                db.AddPeriscopeUser(new PeriscopeUser().setAccesToken(codeResponse.access_token).setTokenType(codeResponse.token_type).setUserName(codeResponse.user.username));
                ListEdit();
            }
        });
    }
    private void ListEdit(){
        List<Object> veris= new veritabani(getBaseContext()).getAll();
        adapter=new adapter_canliyayinlar(recyclerView,veris);
        recyclerView.setAdapter(adapter);
    }
    private void Youtube(int RequestCode){
        startActivityForResult(
                mCredential.newChooseAccountIntent(),
                RequestCode);
    }
    private boolean DeleteCookieFile(String name){
        return new File(getFilesDir().getParent() + "/shared_prefs/"+name+".xml").delete();
    }
    private YoutubeApi getYoutubeApi(String name){
        return new YoutubeApi.Builder().setAccountName(name,this).Build();
    }
    private void ShowSuccesDialogYoutube(String name){
        new veritabani(getBaseContext()).AddYoutubeUser(new YoutubeUser().setName(name));

        /*final AwesomeSuccessDialog scs = new AwesomeSuccessDialog(getBaseContext())
                .setTitle("Başarılı")
                .setMessage(name+" Youtube hesabı başarılı bir şekilde eklendi")
                .setDoneButtonText("Tamam");
        scs.setDoneButtonClick(new Closure() {
            @Override
            public void exec() {
                scs.show().dismiss();
            }
        });
        scs.show();*/

        ListEdit();
    }

    /***
     * Show AwesomeSucces Dialog
     * @param message Gösterilecek Messaj
     */
    private void ShowSuccesDialog(String message){
        ShowSuccesDialog(message,null,null);
    }
    /***
     * Show AwesomeSucces Dialog
     * @param message Gösterilecek Messaj
     * @param title Dialog Başlığı Null olursa default Başarılı
     */
    private void ShowSuccesDialog(String message,String title ){
        ShowSuccesDialog(message,title,null);
    }

    /***
     * Show AwesomeSucces Dialog
     * @param message Gösterilecek Messaj
     * @param title Dialog Başlığı Null olursa default Başarılı
     * @param callBack Button Click Anında Calışır
     */
    private void ShowSuccesDialog(String message, String title, final CallBack callBack){
        final AwesomeSuccessDialog scs = new AwesomeSuccessDialog(getBaseContext())
                .setTitle(title==null?"Başarılı":title)
                .setMessage(message)
                .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                .setDoneButtonText("Tamam");
        scs.setDoneButtonClick(new Closure() {
            @Override
            public void exec() {
                scs.hide();
                if (callBack!=null)
                    callBack.Execute();
            }
        });
        scs.show();
    }


    /**
     * Show AwesomeError Dialog
     * @param message Gösterilecek Messaj
     */
    private void ShowErrorDialog(String message){
        ShowErrorDialog(message,null,null);
    }
    /**
     * Show AwesomeError Dialog
     * @param message Gösterilecek Messaj
     * @param title Dialog Başlığı Null olursa default Başarısız
     */
    private void ShowErrorDialog(String message,String title){
        ShowErrorDialog(message,title,null);
    }
    /**
     * Show AwesomeError Dialog
     * @param message Gösterilecek Messaj
     * @param title Dialog Başlığı Null olursa default Başarısız
     * @param callBack Button Click Anında Calışır
     */
    private void ShowErrorDialog(String message,String title,final CallBack callBack){
        final AwesomeErrorDialog error_dialog = new AwesomeErrorDialog(ActivitySosyalMedya.this)
                .setCancelable(true)
                .setTitle(title==null ? "Başarısız" :title)
                .setMessage(message)
                .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                .setButtonText("Tamam")
                .setCancelable(true);
        error_dialog.setErrorButtonClick(new Closure() {
            @Override
            public void exec() {
                error_dialog.hide();
                if (callBack!=null)
                    callBack.Execute();
            }
        });
        error_dialog.show();
    }
}