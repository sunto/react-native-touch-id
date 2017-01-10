package com.touchid;

import com.facebook.react.bridge.Callback;

import android.util.Log;

public class DialogResultHandler implements FingerprintDialog.DialogResultListener {
    private Callback errorCallback;
    private Callback successCallback;
    public DialogResultHandler(Callback reactErrorCallback, Callback reactSuccessCallback) {
      errorCallback = reactErrorCallback;
      successCallback = reactSuccessCallback;
    };

    @Override
    public void onAuthenticated() {
        Log.d("***DIALOG RESULT HANDLER ", "***On authenticated dialog handler called");
        successCallback.invoke("Successfully authenticated.");
    }
    @Override
    public void onError(String errorString) {
        Log.d("***DIALOG RESULT ERROR ",errorString);
        errorCallback.invoke(errorString);
    }
    @Override
    public void onCancelled() {
        errorCallback.invoke("cancelled");
    }
}
