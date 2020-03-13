package com.haanhgs.app.biometriclogin.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ProgressBar;
import com.haanhgs.app.biometriclogin.R;
import com.haanhgs.app.biometriclogin.controller.OnSuccesLogIn;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements OnSuccesLogIn {

    @BindView(R.id.bnSignIn)
    Button bnSignIn;
    @BindView(R.id.pbrMain)
    ProgressBar pbrMain;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    private void listenToBackPressInFragment(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.clHome);
        if (fragment instanceof BackPressed){
            ((BackPressed)fragment).onBackPressed();
        }else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        listenToBackPressInFragment();
    }

    @OnClick(R.id.bnSignIn)
    public void onViewClicked() {
    }

    @Override
    public void onSuccess(BiometricPrompt.AuthenticationResult result) {

    }
}
