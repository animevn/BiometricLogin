package com.haanhgs.app.biometriclogin.controller;

import android.util.Log;
import java.util.concurrent.Executor;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

public class Controller {

    private static final String DTAG = "D.Controller";
    private static final String ETAG = "E.Controller";
    private AppCompatActivity activity;
    private OnSuccesLogIn onSuccesLogIn;
    private BiometricPrompt.PromptInfo promptInfo;
    private BiometricPrompt biometricPrompt;

    private void getPromptInfo(){
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setConfirmationRequired(true)
                .setDeviceCredentialAllowed(true)
                .build();
    }

    private void checkBiometricsHardware(){
        BiometricManager manager = BiometricManager.from(activity);
        switch (manager.canAuthenticate()){
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d(DTAG, "App can authenticate using biometrics.");
                getPromptInfo();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e(ETAG, "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e(ETAG, "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e(ETAG, "The user hasn't associated any biometric credentials.");
                break;
        }
    }

    public Controller(AppCompatActivity activity, OnSuccesLogIn onSuccesLogIn) {
        this.activity = activity;
        this.onSuccesLogIn = onSuccesLogIn;
        getPromptInfo();
        checkBiometricsHardware();
    }

    private void getBioMetricsPrompt(){
        Executor executor = ContextCompat.getMainExecutor(activity);
        biometricPrompt = new BiometricPrompt(activity, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode,
                                                      @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Log.e(ETAG, errString.toString());
                    }

                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        //for encrypt user data or sth, here is unneccessary
//                        BiometricPrompt.CryptoObject cryptoObject = result.getCryptoObject();
                        onSuccesLogIn.onSuccess(result);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Log.e(ETAG, "authentication failed");
                    }
                });
    }

    public void signIn(){
        if (promptInfo != null){
            getBioMetricsPrompt();
            biometricPrompt.authenticate(promptInfo);
        }
    }

}
