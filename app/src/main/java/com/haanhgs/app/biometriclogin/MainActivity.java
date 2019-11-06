package com.haanhgs.app.biometriclogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private Button bnSignIn;
    private ProgressBar pbrMain;

    private void initViews(){
        bnSignIn = findViewById(R.id.bnSignIn);
        pbrMain = findViewById(R.id.pbrMain);
        pbrMain.setVisibility(View.INVISIBLE);
    }

    private void hideViews(){
        bnSignIn.setVisibility(View.INVISIBLE);
        pbrMain.setVisibility(View.INVISIBLE);
    }

    public void showViews(){
        bnSignIn.setVisibility(View.VISIBLE);
        pbrMain.setVisibility(View.INVISIBLE);
    }

    private Executor executor = new Executor() {
        @Override
        public void execute(@NonNull Runnable command) {
            handler.post(command);
        }
    };

    private void openFragment(){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("home");
        if (fragment == null){
            Home home = new Home();
            ft.replace(R.id.clHome, home, "home");
            ft.commit();
        } else {
            ft.attach(fragment);
        }
    }


    private void showBiometricPrompt() {
        BiometricPrompt.PromptInfo promptInfo =
                new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Login with Biometrics")
                        .setSubtitle("Log in using your credentials")
                        .setNegativeButtonText("Cancel")
//                        .setConfirmationRequired(true)
                        //this will set other way to login
                        //but have to remove setNegativeButton :)
//                        .setDeviceCredentialAllowed(true)
                        .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {

            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                BiometricPrompt.CryptoObject authenticatedCryptoObject = result.getCryptoObject();
                hideViews();
                openFragment();

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        biometricPrompt.authenticate(promptInfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        bnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pbrMain.setVisibility(View.VISIBLE);
                showBiometricPrompt();
            }
        });
    }

    private void listenToBackPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.clHome);
        if (fragment instanceof OnBackPress){
            ((OnBackPress)fragment).onBackPressed();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        listenToBackPressed();
    }
}
