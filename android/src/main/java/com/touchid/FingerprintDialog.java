package com.touchid;

import android.app.DialogFragment;
import android.os.Bundle;

import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import android.view.LayoutInflater;
import android.view.ViewGroup;


import android.hardware.fingerprint.FingerprintManager;

import android.os.Handler.Callback;

public class FingerprintDialog extends DialogFragment
        implements FingerprintHandler.Callback {

    private Button mCancelButton;
    private View mFingerprintContent;
    private TextView mFingerprintDescription;
    private ImageView mFingerprintImage;

    private FingerprintManager.CryptoObject mCryptoObject;
    private DialogResultListener dialogCallback;
    private FingerprintHandler mFingerprintHandler;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Do not create a new Fragment when the Activity is re-created such as orientation changes.
        setRetainInstance(true);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog);

        // dialogCallback = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(getString(R.string.touch_dialog_title));
        View v = inflater.inflate(R.layout.fingerprint_dialog, container, false);


        mFingerprintContent = v.findViewById(R.id.fingerprint_container);

        mFingerprintDescription = (TextView) v.findViewById(R.id.fingerprint_description);
        mFingerprintDescription.setText(getString(R.string.touch_dialog_text));
        mFingerprintImage = (ImageView) v.findViewById(R.id.fingerprint_icon);

        mFingerprintHandler = new FingerprintHandler(this.getContext(), this.getActivity().getSystemService(FingerprintManager.class), this);

        if (!mFingerprintHandler.isFingerprintAuthAvailable()) {
            dismiss(); //dismiss if fingerpint not available
        } else {
            mFingerprintHandler.startAuth(mCryptoObject);
        }


        mCancelButton = (Button) v.findViewById(R.id.cancel_button);
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFingerprintHandler.endAuth();
                // dismiss();
            }
        });

        return v;
    }

    public void setCryptoObject(FingerprintManager.CryptoObject cryptoObject) {
        mCryptoObject = cryptoObject;
    }

    public void setDialogCallback(DialogResultListener newDialogCallback) {
      dialogCallback = newDialogCallback;
    }

    public interface DialogResultListener {
      void onAuthenticated();
      void onError(String errorString);
      void onCancelled();
    }

    @Override
    public void onAuthenticated() {
        //trigger the login of the user
        // Log.d("***on authenticated listener", this.toString() + ", " + String.valueOf(this.dialogCallback));
        dialogCallback.onAuthenticated();
        dismiss();
    }

    @Override
    public void onError(String errorString) {
        dialogCallback.onError(errorString);
        dismiss();
    }

    @Override
    public void onCancelled() {
        dialogCallback.onCancelled();
        dismiss();
    }

}
