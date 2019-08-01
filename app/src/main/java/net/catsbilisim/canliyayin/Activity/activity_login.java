package net.catsbilisim.canliyayin.Activity;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.crashlytics.android.Crashlytics;
import com.nvanbenschoten.motion.ParallaxImageView;
import io.fabric.sdk.android.Fabric;

import net.catsbilisim.canliyayin.DataBase.veritabani;
import net.catsbilisim.canliyayin.Preferences.kullanici;
import net.catsbilisim.canliyayin.Preferences.preferences;
import net.catsbilisim.canliyayin.R;
import net.catsbilisim.canliyayin.Preferences.ILogin;
import net.catsbilisim.canliyayin.backgrund.login_background;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class activity_login extends AppCompatActivity implements ILogin {
    String TAG = getClass().getName();
    String[] permission = new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};
    TelephonyManager telephonyManager;
    ParallaxImageView panoramaImageView;
    String strUserName;
    String strPassword;
    EditText password;
    EditText username;
    Button sign;
    ProgressDialog dialog;
    boolean doubleBackToExitPressedOnce=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!checkDataBase())
            writeDataBase();
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build();
        //Fabric.with(fabric, new Crashlytics());
        Fabric.with(fabric);
        setContentView(R.layout.activity_login);
        Log.e(TAG,getUniquePsuedoID());
        password = findViewById(R.id.edit_password);
        username = findViewById(R.id.edit_username);
        sign = findViewById(R.id.btn_giris);
        dialog= new ProgressDialog(this);
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.setMessage("Giriş Yapılıyor!!");
                strUserName = username.getText().toString();
                strPassword= password.getText().toString();
                dialog.show();
                new login_background(activity_login.this).execute(String.format(preferences.loginUrl,strUserName,strPassword,getUniquePsuedoID()));
            }
        });
        if (new preferences(getBaseContext()).getKullanici()!=null)
        {
            password.setEnabled(false);
            username.setEnabled(false);
            sign.setEnabled(false);
            ActivityCompat.requestPermissions(this,
                    permission,1);
        }
        panoramaImageView =findViewById(R.id.panorama_image_view);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public String getUniquePsuedoID() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = null;
        try {
            serial = android.os.Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(findViewById(android.R.id.content), "Çıkmak için 1 kez daha dokunun", Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register GyroscopeObserver.
        panoramaImageView.registerSensorManager();
        //gyroscopeObserver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister GyroscopeObserver.
        //gyroscopeObserver.unregister();
        panoramaImageView.unregisterSensorManager();
    }
    @Override
    public void login(String value) {
        Log.e(TAG, "login: "+value );
        dialog.dismiss();
        if (value.length()<5){
            AlertDialog.Builder dialog= new AlertDialog.Builder(this);
            dialog.setMessage("Kullanıcı Adı Şifre Hatalı");
            dialog.setTitle("Hatalı Giriş");
            dialog.create().show();
        }else{

            password.setEnabled(false);
            username.setEnabled(false);
            password.clearFocus();
            username.clearFocus();
            sign.setEnabled(false);
            new preferences(getBaseContext()).saveKullanici(new kullanici(strPassword,strUserName,value.replace("\"",""),getUniquePsuedoID()));
            ActivityCompat.requestPermissions(this,
                    permission,1);
            //startActivity(new Intent(getBaseContext(),ana_ekran.class));
            //finish();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.e("RequestCode",requestCode+"");
        switch (requestCode) {
            case 1: {
                for (int i : grantResults)
                    if (i!=PackageManager.PERMISSION_GRANTED)
                        finish();
                //return;
            }
        }
        startActivity(new Intent(getBaseContext(),ana_ekran.class));
        finish();
    }
    boolean checkDataBase(){
        File dataBase=getDatabasePath(veritabani.DatabaseName);
        Log.d("File",dataBase.getPath());
        return dataBase.exists();
    }
    void writeDataBase(){
        try{
            InputStream is = getAssets().open(veritabani.DatabaseName);
            File file =getDatabasePath(veritabani.DatabaseName);
            file.getParentFile().mkdir();
            file.createNewFile();
            System.out.println(file.getPath());
            OutputStream os= new FileOutputStream(file);
            byte[] buff=new byte[1024];
            int len = 0;
            while ((len=is.read(buff))>0)
                os.write(buff,0,len);
            os.flush();
            os.close();
            is.close();
        }catch (Exception e){
            Log.e("Exceptioon",e.getMessage());
        }
    }
}
