package com.haanhgs.app.biometriclogin.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.haanhgs.app.biometriclogin.R;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SuccesLogIn {

    @BindView(R.id.bnSignIn)
    Button bnSignIn;
    @BindView(R.id.pbrMain)
    ProgressBar pbrMain;

    private Repo repo;

    public void showViews() {
        bnSignIn.setVisibility(View.VISIBLE);
        pbrMain.setVisibility(View.VISIBLE);
    }

    private void hideViews() {
        bnSignIn.setVisibility(View.INVISIBLE);
        pbrMain.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        repo = new Repo(this);
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
        repo.signIn();
    }

    @Override
    public void onSuccessLogIn(BiometricPrompt.AuthenticationResult result) {
        hideViews();
        repo.openFragment();
    }
}
