package net.catsbilisim.canliyayin.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeInfoDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeProgressDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import net.catsbilisim.canliyayin.Activity.ana_ekran;
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
import net.catsbilisim.canliyayin.Preferences.IAddInstagram;
import net.catsbilisim.canliyayin.Preferences.IAddMedya;
import net.catsbilisim.canliyayin.Preferences.ISosyalKayitDialog;
import net.catsbilisim.canliyayin.Preferences.SosyalMedya;
import net.catsbilisim.canliyayin.Preferences.kullanici;
import net.catsbilisim.canliyayin.Preferences.preferences;
import net.catsbilisim.canliyayin.R;
import net.catsbilisim.canliyayin.Preferences.interface_dialog_ok_click;
import net.catsbilisim.canliyayin.backgrund.AddMedya_background;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class custom_dialog {
    int maxHeight;
    interface_dialog_ok_click click;
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
        return dialog;
    }
    public Dialog AddPeriscope(final Activity activity, final IPeriscopeFinish finish){
        final Dialog pr_dialog = new AwesomeProgressDialog(activity)
                .setMessage("Periscope Sunucusuna Bağlantı kuruluyor")
                .setTitle("Lütfen Bekleyiniz")
                .setCancelable(false)
                .show();
        final PeriscopeApi api=new PeriscopeApi(null);
        api.setSuperfinish(new IPeriscopeFinish() {
            @Override
            public void Finish(final Object value) {
                pr_dialog.dismiss();
                final AwesomeInfoDialog if_dialog =getPeriscopeInfoDialog(activity,(CreateDeviceCodeResponse) value);
                if_dialog.show();
                final String device_code =((CreateDeviceCodeResponse)value).device_code;
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
                                    String region = api.GetRegion(response.token_type+" "+response.access_token);
                                    new ConnectionCreateBroadcast(new IPeriscopeFinish() {
                                        @Override
                                        public void Finish(Object value) {
                                            CreateBroadcastResponse broadcastResponse=(CreateBroadcastResponse)value;
                                            //final preferences ayarlar =new preferences(activity);
                                            //
                                            kullanici kullanici = new preferences(activity).getKullanici();
                                            String Link=broadcastResponse.encoder.rtmp_url+"/"+broadcastResponse.encoder.stream_key;
                                            new AddMedya_background(new IAddMedya() {
                                                @Override
                                                public void Finish(Integer ResultCode, SosyalMedya medya) {
                                                    if (ResultCode==200)
                                                        //ayarlar.AddYayin(medya);
                                                    System.out.println("Add Medya");
                                                }
                                            }).execute(String.format(preferences.AddYayin,Link,kullanici.getUid(),"Periscope"),"Periscope",Link);
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
        return null;
    }
    private AwesomeInfoDialog getPeriscopeInfoDialog(Activity activity,CreateDeviceCodeResponse response){
        return new AwesomeInfoDialog(activity)
                .setTitle(response.user_code)
                .setMessage(response.associate_url)
                .setPositiveButtonText("Tamam");
    }
}
