package com.haanhgs.app.biometriclogin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bnSignIn)
    Button bnSignIn;
    @BindView(R.id.pbrMain)
    ProgressBar pbrMain;

    public void showViews() {
        bnSignIn.setVisibility(View.VISIBLE);
        pbrMain.setVisibility(View.INVISIBLE);
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

    @OnClick(R.id.bnSignIn)
    public void onViewClicked() {
    }
}
