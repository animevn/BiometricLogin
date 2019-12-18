package com.haanhgs.app.biometriclogin;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import java.util.concurrent.Executor;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bnSignIn)
    Button bnSignIn;
    @BindView(R.id.pbrMain)
    ProgressBar pbrMain;
    @BindView(R.id.clHome)
    ConstraintLayout clHome;

    private static final String ETAG = "E.MainActivity";
    private static final String DTAG = "D.MainActivity";
    private BiometricPrompt.PromptInfo promptInfo;
    private BiometricManager manager;

    private void initViews() {
        pbrMain.setVisibility(View.INVISIBLE);
    }

    private void hideViews() {
        bnSignIn.setVisibility(View.INVISIBLE);
        pbrMain.setVisibility(View.INVISIBLE);
    }

    public void showViews() {
        bnSignIn.setVisibility(View.VISIBLE);
        pbrMain.setVisibility(View.INVISIBLE);
    }

    private void openFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("home");
        if (fragment == null) {
            FragmentHome home = new FragmentHome();
            ft.replace(R.id.clHome, home, "home");
            ft.commit();
        } else {
            ft.attach(fragment);
        }
    }

    private void getPromtInfo(){
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login with Biometrics")
//                .setSubtitle("Log in using your credentials")
//                .setNegativeButtonText("Cancel")

                //this will set other way to login
                //but have to remove setNegativeButton :)
                .setConfirmationRequired(true)
                .setDeviceCredentialAllowed(true)
                .build();
    }

    private void checkBiometrics(){
        manager = BiometricManager.from(this);
        switch (manager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d(DTAG, "App can authenticate using biometrics.");
                getPromtInfo();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e(ETAG, "No biometric features available on this device.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e(ETAG, "Biometric features are currently unavailable.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e(ETAG, "The user hasn't associated " +
                        "any biometric credentials with their account.");
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initViews();
        checkBiometrics();
    }

    private void listenToBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.clHome);
        if (fragment instanceof OnBackPress) {
            ((OnBackPress) fragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        listenToBackPressed();
    }

    private void promtBiometric(){
        Executor executor = ContextCompat.getMainExecutor(this);
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString){
                super.onAuthenticationError(errorCode, errString);
                Log.e(ETAG, errString.toString());
                showViews();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result){
                super.onAuthenticationSucceeded(result);
                //this is not neccesary
                BiometricPrompt.CryptoObject authenticatedCryptoObject = result.getCryptoObject();
                hideViews();
                openFragment();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.e(ETAG, "authentication failed");
                showViews();
            }

        });
        biometricPrompt.authenticate(promptInfo);
    }

    @OnClick(R.id.bnSignIn)
    public void onViewClicked() {
        pbrMain.setVisibility(View.VISIBLE);
        if (promptInfo != null) promtBiometric();
    }
}
