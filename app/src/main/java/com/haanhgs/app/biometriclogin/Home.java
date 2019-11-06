package com.haanhgs.app.biometriclogin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class Home extends Fragment implements OnBackPress{

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onBackPressed() {
        if (getActivity() != null)  ((MainActivity)getActivity()).showViews();
        if (getFragmentManager() != null) {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment fragment = getFragmentManager().findFragmentById(R.id.clHome);
            if (fragment != null) ft.remove(fragment).commit();
        }
    }

}
