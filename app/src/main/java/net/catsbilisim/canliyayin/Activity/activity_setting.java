package net.catsbilisim.canliyayin.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.pedro.rtplibrary.rtmp.RtmpCamera1;

import net.catsbilisim.canliyayin.DataBase.veritabani;
import net.catsbilisim.canliyayin.R;
import net.catsbilisim.canliyayin.adapter.custom_dialog;
import net.catsbilisim.canliyayin.Preferences.interface_dialog_ok_click;
import net.catsbilisim.canliyayin.Preferences.preferences;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class activity_setting extends Activity {
    TextView txt_cozunurluk;
    TextView txt_fps;
    preferences ayarlar;
    CheckBox chk_save;
    TextView txt_path;
    private File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/CanliYayin");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        RelativeLayout video_resulation = findViewById(R.id.setting_video_resulution);
        RelativeLayout setting_fps = findViewById(R.id.setting_fps);
        RelativeLayout setting_bitrate=findViewById(R.id.setting_bitRate);
        RelativeLayout setting_sosyal=findViewById(R.id.setting_sosyal);
        RelativeLayout setting_kullanici = findViewById(R.id.setting_kullanici);
        TextView txt_kullanici = findViewById(R.id.txt_kullanici_name);
        setting_kullanici.setOnClickListener(kullanici_click);
        txt_kullanici.setText(new preferences(getApplicationContext()).getKullanici().getUserName());
        setting_sosyal.setOnClickListener(sosyal_click);
        final RelativeLayout setting_save= findViewById(R.id.setting_save);
        RelativeLayout setting_path = findViewById(R.id.setting_path);
        setting_path.setOnClickListener(path_click);
        setting_save.setOnClickListener(save_click);
        video_resulation.setOnClickListener(video_resulation_click);
        setting_bitrate.setOnClickListener(bitrate_click);
        setting_fps.setOnClickListener(fps_click);
        txt_cozunurluk = findViewById(R.id.video_resolution_value);
        txt_fps = findViewById(R.id.video_framerate_value);
        ayarlar = new preferences(getBaseContext());
        txt_cozunurluk.setText(ayarlar.getCozunurluk());
        txt_fps.setText(ayarlar.getFps());
        chk_save=findViewById(R.id.save_stream);
        chk_save.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                chk_save.setChecked(!isChecked);
                setting_save.callOnClick();
            }
        });
        txt_path= findViewById(R.id.txt_path);
        txt_path.setText(folder.getPath());
        chk_save.setChecked(ayarlar.getSave());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    RelativeLayout.OnClickListener video_resulation_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            List<String> cozunurluk = new ArrayList<>();
            for (Camera.Size size : new RtmpCamera1(getBaseContext(),null).getResolutionsBack())
                cozunurluk.add(size.width +" X "+size.height);
            String select= ayarlar.getCozunurluk();
            new custom_dialog(new interface_dialog_ok_click() {
                @Override
                public void Click(String s) {
                    ayarlar.setCozunurluk(s);
                    txt_cozunurluk.setText(s);
                }
            }).cozunurluk_dialog(activity_setting.this,cozunurluk,select).show();
        }
    };

    RelativeLayout.OnClickListener fps_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            new custom_dialog(new interface_dialog_ok_click() {
                @Override
                public void Click(String s) {
                    ayarlar.setFps(s);
                    txt_fps.setText(s);
                }
            }).inputDialog(activity_setting.this,ayarlar.getFps(),"FPS","0 ~ 30 ",30).show();
        }
    };

    RelativeLayout.OnClickListener kullanici_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Dialog dialog = new custom_dialog().cikisDialog(activity_setting.this);
            Button cikis = dialog.findViewById(R.id.dialog_cikis_cikis);
            cikis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    preferences ayarlar = new preferences(getBaseContext());
                    ayarlar.DeleteCookie();
                    ayarlar.DeleteKullanici();
                    new veritabani(getBaseContext()).DeleteAll();
                    Intent i = new Intent(getBaseContext(),activity_login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialog.dismiss();
                    startActivity(i);
                    finish();
                }
            });
            dialog.show();

        }
    };

    RelativeLayout.OnClickListener sosyal_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getBaseContext(),ActivitySosyalMedya.class));
        }
    };
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    RelativeLayout.OnClickListener bitrate_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    RelativeLayout.OnClickListener save_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            chk_save.setChecked(!chk_save.isChecked());
            ayarlar.setSave(chk_save.isChecked());
        }

    };

    RelativeLayout.OnClickListener path_click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!folder.exists())
                folder.mkdir();
            Uri selectedUri = Uri.parse(Environment.getExternalStorageDirectory() + "/CanliYayin/");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(selectedUri, "resource/folder");
            startActivity(intent);
        }
    };
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String Fpath = data.getDataString();
        //TODO handle your request here
        Log.e("Path",Fpath);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
