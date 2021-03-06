package com.haanhgs.app.biometriclogin.view;

import android.os.Bundle;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.haanhgs.app.biometriclogin.R;
import com.haanhgs.app.biometriclogin.viewmodel.MyViewModel;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SuccesLogIn {

    @BindView(R.id.bnSignIn)
    Button bnSignIn;
    @BindView(R.id.pbrMain)
    ProgressBar pbrMain;

    private Repo repo;
    private MyViewModel viewModel;

    public void showViews() {
        bnSignIn.setVisibility(View.VISIBLE);
        pbrMain.setVisibility(View.VISIBLE);
    }

    private void hideViews() {
        bnSignIn.setVisibility(View.INVISIBLE);
        pbrMain.setVisibility(View.INVISIBLE);
    }

    private void hideActionBarInLandscapeMode(){
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        if (rotation == Surface.ROTATION_90 || rotation == Surface.ROTATION_270){
            if (getSupportActionBar() != null) getSupportActionBar().hide();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        hideActionBarInLandscapeMode();
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        viewModel.getIsLogin().observe(this, aBoolean -> {
            if (aBoolean){
                repo.openFragment();
                hideViews();
            }
        });
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
        viewModel.setIsLogin(true);
    }
}
