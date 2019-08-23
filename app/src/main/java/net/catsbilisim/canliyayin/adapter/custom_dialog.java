package net.catsbilisim.canliyayin.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import net.catsbilisim.canliyayin.Activity.ana_ekran;
import net.catsbilisim.canliyayin.Api.ApiResultSingle;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.BroadCast.BroadcastCreateResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.Interface.ILoginResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.Class.User.InstaLoginResponse;
import net.catsbilisim.canliyayin.Api.InstagramApi.InstagramApi;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.IPeriscopeFinish;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CheckDeviceCodeResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CreateBroadcastResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Class.Response.CreateDeviceCodeResponse;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.Endpoints.Connection.ConnectionCreateBroadcast;
import net.catsbilisim.canliyayin.Api.PeriscopeApi.PeriscopeApi;
import net.catsbilisim.canliyayin.DataBase.InstagramUser;
import net.catsbilisim.canliyayin.DataBase.PeriscopeUser;
import net.catsbilisim.canliyayin.Preferences.CallBack;
import net.catsbilisim.canliyayin.Preferences.IAddInstagram;
import net.catsbilisim.canliyayin.Preferences.IAddMedya;
import net.catsbilisim.canliyayin.Preferences.ISosyalKayitDialog;
import net.catsbilisim.canliyayin.Preferences.SosyalMedya;
import net.catsbilisim.canliyayin.Preferences.UpdateListener;
import net.catsbilisim.canliyayin.Preferences.kullanici;
import net.catsbilisim.canliyayin.Preferences.preferences;
import net.catsbilisim.canliyayin.R;
import net.catsbilisim.canliyayin.Preferences.interface_dialog_ok_click;
import net.catsbilisim.canliyayin.backgrund.AddMedya_background;
import net.catsbilisim.canliyayin.backgrund.DownloadFile;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class custom_dialog {
    private int maxHeight;
    private interface_dialog_ok_click click;
    public custom_dialog(interface_dialog_ok_click click){
        maxHeight=800;
        this.click=click;
    }
    public custom_dialog(){}
    public Dialog cozunurluk_dialog(Activity activity, final List<String> items, String select){
        final Dialog  dialog= new Dialog(activity);
        dialog.setContentView(R.layout.layout_cozunurluk_list);
        final RadioGroup grp = dialog.findViewById(R.id.radio_grp);
        for (int i = 0;i<items.size();i++)
        {
            String item = items.get(i);
            RadioButton btn = getRadio(i,item,dialog.getContext());
            if (item.equals(select)){
                Log.e("select",item);
                btn.setChecked(true);
            }
            grp.addView(btn);
        }
        TextView txt=dialog.findViewById(R.id.list_title);
        txt.setText("Çözünürlük");
        dialog.getWindow().setLayout(-1,1280);
        Button btn_ok = dialog.findViewById(R.id.btn_save);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.Click(items.get(grp.getCheckedRadioButtonId()));
                dialog.dismiss();
            }
        });
        Button cancel = dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }
    private RadioButton getRadio(int id,String text, Context context){
        RadioButton radioButton = new RadioButton(context);
        radioButton.setText(text);
        radioButton.setId(id);
        return radioButton;
    }
    public Dialog inputDialog(Activity activity, String value, String title, final String info, final int max){
        final Dialog dialog=new Dialog(activity);
        dialog.setContentView(R.layout.layout_input_dialog);
        TextView txt_title = dialog.findViewById(R.id.dialog_title);
        txt_title.setText(title);
        final EditText edit = dialog.findViewById(R.id.dialog_edit);
        edit.setText(value);
        TextView txt_info = dialog.findViewById(R.id.text_info);
        if (info.length()==0)
            txt_info.setVisibility(View.GONE);
        else txt_info.setText(info);
        final TextInputLayout input = dialog.findViewById(R.id.edit_layout);
        Button cancel = dialog.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Button btn_ok = dialog.findViewById(R.id.btn_save);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(edit.getText().toString())>max){
                    input.setError("Geçerli değer aralığında bir değer giriniz!!");
                    input.setErrorEnabled(true);
                }else{
                    click.Click(edit.getText().toString());
                    dialog.dismiss();
                }
            }
        });
        return dialog;
    }
    public Dialog AddMedya(Activity activity, final ISosyalKayitDialog IDialog){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_add_sosyal_medya);
        final EditText edit_name = dialog.findViewById(R.id.dialog_sosyalMedya_Name);
        final EditText edit_link = dialog.findViewById(R.id.dialog_sosyalMedya_Link);
        Button btn = dialog.findViewById(R.id.btn_sosyal_kaydet);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDialog.Click(edit_name.getText().toString(),edit_link.getText().toString());
            }
        });
        return dialog;
    }
    public Dialog UpdateMedya(Activity activity, String Name,String Link,final ISosyalKayitDialog IDialog){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_add_sosyal_medya);
        final EditText edit_name = dialog.findViewById(R.id.dialog_sosyalMedya_Name);
        final EditText edit_link = dialog.findViewById(R.id.dialog_sosyalMedya_Link);
        edit_name.setText(Name);
        edit_link.setText(Link);
        Button btn = dialog.findViewById(R.id.btn_sosyal_kaydet);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IDialog.Click(edit_name.getText().toString(),edit_link.getText().toString());
            }
        });
        return dialog;
    }
    public Dialog cikisDialog(Activity activity){
        final Dialog dialog=new Dialog(activity);
        dialog.setContentView(R.layout.dialog_cikis);
        Button iptal = dialog.findViewById(R.id.dialog_cikis_iptal);
        iptal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        return dialog;
    }
    public Dialog AddInstagram(final Activity activity, final IAddInstagram instagram){
        /*final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_add_instagram);
        final EditText edit_name = dialog.findViewById(R.id.dialog_sosyalMedya_Name);
        final EditText edit_password = dialog.findViewById(R.id.dialog_sosyalMedya_Link);
        Button btn = dialog.findViewById(R.id.btn_sosyal_kaydet);
        final AwesomeProgressDialog pd_dialog = new AwesomeProgressDialog(activity)
                .setTitle("Lütfen Bekleyiniz")
                .setMessage("Instagram Girişi Yapılıyor")
                .setCancelable(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InstagramApi(ana_ekran._requestMessage.setPassword(edit_password.getText().toString()).setUsername(edit_name.getText().toString()), ana_ekran._device, new InstagramApi.CallBack() {
                    @Override
                    public void Start() {
                        pd_dialog.show();
                    }

                    @Override
                    public void Finish(BroadcastCreateResponse requestMessage) {

                    }
                },activity).Login(new ILoginResponse() {
                    @Override
                    public void Login(InstaLoginResponse response) {
                        pd_dialog.show().dismiss();
                        instagram.Succes(response.Status.equalsIgnoreCase("ok"),response);
                    }
                });
            }
        });
        return dialog;*/
        return UpdateInstagram(null,activity,instagram);
    }
    public Dialog UpdateInstagram(final InstagramUser user,final Activity activity, final IAddInstagram instagram){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_add_instagram);
        final EditText edit_name = dialog.findViewById(R.id.dialog_sosyalMedya_Name);
        final EditText edit_password = dialog.findViewById(R.id.dialog_sosyalMedya_Link);
        edit_name.setText(user == null ? "" : user.getName());
        Button btn = dialog.findViewById(R.id.btn_sosyal_kaydet);
        final AwesomeProgressDialog pd_dialog = new AwesomeProgressDialog(activity)
                .setTitle("Lütfen Bekleyiniz")
                .setMessage("Instagram Girişi Yapılıyor")
                .setCancelable(false);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new InstagramApi.Builder()
                        .setRequestMessage(ana_ekran._requestMessage
                                .setPassword(edit_password.getText().toString())
                                .setUsername(edit_name.getText().toString()))
                        .setDevice(ana_ekran._device).setContext(activity)
                        .setCallBack(new InstagramApi.CallBack() {
                            @Override
                            public void Start() {
                                pd_dialog.show();
                            }

                            @Override
                            public void Finish(BroadcastCreateResponse requestMessage) {

                            }
                        })
                        .Build()
                        .Login(new ApiResultSingle<InstaLoginResponse>() {
                            @Override
                            public void Finish(InstaLoginResponse response) {
                                pd_dialog.hide();
                                instagram.Succes(response.Status.equalsIgnoreCase("ok"),response);
                            }
                        });
            }
        });
        return dialog;
    }
    public Dialog UpdateVersion(final Activity activity, final CallBack callBack){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_update_version);
        Button dismis = dialog.findViewById(R.id.update_dialog_btn_dismis);
        Button update = dialog.findViewById(R.id.update_dialog_btn_update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateVersion(dialog,callBack);
            }
        });
        dismis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }
    private void UpdateVersion(Dialog dialog, final CallBack callBack){
        LinearLayout button_layout = dialog.findViewById(R.id.update_dialog_button_layout);
        LinearLayout update_layout = dialog.findViewById(R.id.update_dialog_progress_layout);
        final ProgressBar progressBar = dialog.findViewById(R.id.update_dialog_progress);
        final TextView txt_progress = dialog.findViewById(R.id.update_dialog_progress_txt);
        final TextView txt_size = dialog.findViewById(R.id.update_dialog_size_txt);
        progressBar.setIndeterminate(true);
        button_layout.setVisibility(View.GONE);
        update_layout.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        UpdateListener listener = new UpdateListener() {
            String size="";
            @Override
            public void start() {
            }

            @Override
            public void UpdateSize(final String size) {
                this.size=size;
            }

            @Override
            public void UpdateLoad(final long... params) {
                if (progressBar.isIndeterminate())
                    progressBar.setIndeterminate(false);
                txt_progress.setText(params[0]+"%");
                progressBar.setProgress(((int)params[0]));
                txt_size.setText(format(params[1],2)+" / "+format(params[2],2));
            }

            @Override
            public void Finish() {
                callBack.Execute();
            }
        };
        new DownloadFile(dialog.getContext().getFilesDir(),listener).execute("https://d-04.winudf.com/b/apk/Y29tLmxvbmVseWNhdGdhbWVzLlhwbG9yZV80MTUwN19jYTcxMjU1Yw?_fn=WCBwbG9yZSBGaWxlIE1hbmFnZXJfdjQuMTUuMDdfYXBrcHVyZS5jb20uYXBr&_p=Y29tLmxvbmVseWNhdGdhbWVzLlhwbG9yZQ&as=e5a8f7880f5b6957c95265b653f26fc75d5fac7e&c=1%7CTOOLS%7CZGV2PUxvbmVseSUyMENhdCUyMEdhbWVzJnQ9YXBrJnM9NzA0MTc1NiZ2bj00LjE1LjA3JnZjPTQxNTA3&hot=1&k=18b288a0e9098ec9238f70b7547877655d6254b0");
    }
    public String format(double bytes, int digits) {
        String[] dictionary = { "bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB" };
        int index = 0;
        for (index = 0; index < dictionary.length; index++) {
            if (bytes < 1024) {
                break;
            }
            bytes = bytes / 1024;
        }
        return String.format("%." + digits + "f", bytes) + " " + dictionary[index];
    }
    public void AddPeriscope(final Activity activity, final ApiResultSingle<CheckDeviceCodeResponse> finish){
        final Dialog pr_dialog = new AwesomeProgressDialog(activity)
                .setMessage("Periscope Sunucusuna Bağlantı kuruluyor")
                .setTitle("Lütfen Bekleyiniz")
                .setCancelable(false)
                .show();
        final PeriscopeApi api=new PeriscopeApi(null);
        api.setSuperfinish(new ApiResultSingle<CreateDeviceCodeResponse>() {
            @Override
            public void Finish(final CreateDeviceCodeResponse value) {
                pr_dialog.dismiss();
                final AwesomeInfoDialog if_dialog =getPeriscopeInfoDialog(activity,(CreateDeviceCodeResponse) value);
                if_dialog.show();
                final String device_code =value.device_code;
                Closure c =new Closure() {
                    @Override
                    public void exec() {
                        if_dialog.show().dismiss();
                        final Dialog pr_dialog = new AwesomeProgressDialog(activity)
                                .setMessage("Yetkilendirme Bekleniyor")
                                .setTitle("Lütfen Bekleyiniz")
                                .setCancelable(false)
                                .show();
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                final CheckDeviceCodeResponse response=api.CheckDevice(device_code);
                                if (response.state.equalsIgnoreCase("associated")){
                                    this.cancel();
                                    api.setUser(new PeriscopeUser().setAccesToken(response.access_token).setTokenType(response.token_type));
                                    String region = api.GetRegion();
                                    new ConnectionCreateBroadcast(new ApiResultSingle<CreateBroadcastResponse>() {
                                        @Override
                                        public void Finish(CreateBroadcastResponse value) {
                                            CreateBroadcastResponse broadcastResponse=value;
                                            //final preferences ayarlar =new preferences(activity);
                                            //
                                            kullanici kullanici = new preferences(activity).getKullanici();
                                            String Link=broadcastResponse.encoder.rtmp_url+"/"+broadcastResponse.encoder.stream_key;
                                            new AddMedya_background(null).execute(String.format(preferences.AddYayin,Link,kullanici.getUid(),"Periscope"),"Periscope",Link);
                                           /* new AddMedya_background(new IAddMedya() {
                                                @Override
                                                public void Finish(Integer ResultCode, SosyalMedya medya) {
                                                    if (ResultCode==200)
                                                        //ayarlar.AddYayin(medya);
                                                    System.out.println("Add Medya");
                                                }
                                            }).execute(String.format(preferences.AddYayin,Link,kullanici.getUid(),"Periscope"),"Periscope",Link);*/
                                            activity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    pr_dialog.dismiss();
                                                    finish.Finish(response);
                                                }
                                            });
                                        }
                                    }).execute(region,response.token_type+" "+response.access_token);
                                }
                            }
                        },1000,5000);
                    }
                };
                if_dialog.setPositiveButtonClick(c);
            }
        });
        api.GetDeviceKey();
    }
    private AwesomeInfoDialog getPeriscopeInfoDialog(Activity activity,CreateDeviceCodeResponse response){
        return new AwesomeInfoDialog(activity)
                .setTitle(response.user_code)
                .setMessage(response.associate_url)
                .setPositiveButtonText("Tamam");
    }
}
