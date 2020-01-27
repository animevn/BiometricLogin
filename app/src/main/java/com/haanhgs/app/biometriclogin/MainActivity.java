package com.haanhgs.app.biometriclogin;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import java.util.concurrent.Executor;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
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

    private static final String DTAG = "D.MainActivity";
    private static final String ETAG = "E.MainActivity";
    private BiometricPrompt.PromptInfo prompt;
    private BiometricPrompt biometricPrompt;

    public void showViews() {
        bnSignIn.setVisibility(View.VISIBLE);
        pbrMain.setVisibility(View.INVISIBLE);
    }

    private void hideViews() {
        bnSignIn.setVisibility(View.INVISIBLE);
        pbrMain.setVisibility(View.INVISIBLE);
    }

    private void getPromptInfo(){
        prompt = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setConfirmationRequired(true)
                .setDeviceCredentialAllowed(true)
                .build();
    }

    private void checkBiometricsHardware(){
        BiometricManager manager = BiometricManager.from(this);
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        showViews();
        checkBiometricsHardware();
    }

    private void listenToBackPressedWhenInFragment() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.clHome);
        if (fragment instanceof BackPressed) {
            ((BackPressed) fragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        listenToBackPressedWhenInFragment();
    }

    private void openFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("home");
        if (fragment == null){
            FragmentHome fragmentHome = new FragmentHome();
            ft.replace(R.id.clHome, fragmentHome, "home").commit();
        }else {
            ft.attach(fragment);
        }
    }

    private void getBioMetricsPrompt(){
        Executor executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(this, executor,
                new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Log.e(ETAG, errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                //for encrypt user data or sth, here is unneccessary
                BiometricPrompt.CryptoObject authenticatedCryptoObject = result.getCryptoObject();
                hideViews();
                openFragment();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.e(ETAG, "authentication failed");
            }
        });
    }

    @OnClick(R.id.bnSignIn)
    public void onViewClicked() {
        if (prompt != null) {
            getBioMetricsPrompt();
            biometricPrompt.authenticate(prompt);
        }
    }
}
