package net.catsbilisim.canliyayin.adapter;

import android.app.Activity;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.os.Handler;
import android.security.keystore.KeyProperties;
import android.support.design.widget.BottomSheetDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import net.catsbilisim.canliyayin.R;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;

public class FingerPrintBottomSheet extends BottomSheetDialogFragment implements FingerprintUiHelper.Callback {
    TextView status;
    ImageView icon;
    private FingerprintManager.CryptoObject mCryptoObject;
    private FingerprintUiHelper mFingerprintUiHelper;
    private Cipher defaultCipher;
    static Activity mactivity;
    static FingerprintUiHelper.Callback mcallback;
    public static FingerPrintBottomSheet newInstance(Activity activity, FingerprintUiHelper.Callback callback){
        mactivity=activity;
        mcallback=callback;
        return new FingerPrintBottomSheet();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet,container,false);
        status=view.findViewById(R.id.description_tv);
        icon=view.findViewById(R.id.fingerprint_symbol_iv);
        try {
            defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        setStyle(R.style.AppBottomSheetDialogTheme,getTheme());
        mFingerprintUiHelper = new FingerprintUiHelper(
                mactivity.getSystemService(FingerprintManager.class), this);
        mFingerprintUiHelper.startListening(mCryptoObject);
        Button cancel = view.findViewById(R.id.negative_btn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcallback.cancel();
            }
        });
        setCancelable(false);
        view.setBackgroundResource(R.drawable.bottom_sheet_background);
        //getDialog().getWindow().setBackgroundDrawableResource(R.drawable.bottom_sheet_background);
        return view;
    }

    @Override
    public void cancel() {
        icon.setImageResource(R.drawable.ic_fingerprint_error);
        status.setText("Çok Fazla deneme yapıldı!!!");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
                mcallback.cancel();
            }
        },1600);
    }

    /**
     * Sets the crypto object to be passed in when authenticating with fingerprint.
     */


    @Override
    public void onAuthenticated() {
        icon.setImageResource(R.drawable.ic_fingerprint_success);
        status.setText("Parmak izi doğrulandı");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismiss();
            }
        },1300);
    }

    @Override
    public void onError() {
        Log.e("OnError","OnError");
        icon.setImageResource(R.drawable.ic_fingerprint_error);
        status.setText("Parmak izi doğrulanamadı");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                icon.setImageResource(R.drawable.ic_fp_40px);
                status.setText("Sensöre Dokunun!!!");
                mFingerprintUiHelper.startListening(mCryptoObject);
            }
        },1600);
    }
}
