package net.catsbilisim.canliyayin.adapter;

import android.hardware.fingerprint.FingerprintManager;
import android.util.Log;

public class FingerprintUiHelper extends FingerprintManager.AuthenticationCallback {
    private final FingerprintManager mFingerprintManager;
    private final Callback mCallback;

    /**
     * Constructor for {@link FingerprintUiHelper}.
     */
    FingerprintUiHelper(FingerprintManager fingerprintManager, Callback callback) {
        mFingerprintManager = fingerprintManager;
        mCallback = callback;
    }

    public boolean isFingerprintAuthAvailable() {
        return mFingerprintManager.isHardwareDetected()
                && mFingerprintManager.hasEnrolledFingerprints();
    }

    public void startListening(FingerprintManager.CryptoObject cryptoObject) {
        if (!isFingerprintAuthAvailable()) {
            return;
        }
        mFingerprintManager
                .authenticate(cryptoObject, null,0 /* flags */, this, null);
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        Log.e("Error"+errMsgId,errString.toString());
        if (errMsgId==7)
            mCallback.cancel();
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {

    }

    @Override
    public void onAuthenticationFailed() {
        Log.e("FingerPrintUiHelper","OnFailed");
        mCallback.onError();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        mCallback.onAuthenticated();
        Log.e("FingerPrintUiHelper","Tanımlandı");
        mCallback.onAuthenticated();
    }

    public interface Callback {

        void onAuthenticated();

        void onError();

        void cancel();
    }
}