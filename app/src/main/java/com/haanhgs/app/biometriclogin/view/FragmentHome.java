package com.haanhgs.app.biometriclogin.view;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.haanhgs.app.biometriclogin.R;
import com.haanhgs.app.biometriclogin.viewmodel.MyViewModel;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FragmentHome extends Fragment implements BackPressed{

    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.tvInfo)
    TextView tvInfo;

    private Context context;
    private FragmentActivity activity;
    private MyViewModel viewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        viewModel = new ViewModelProvider(activity).get(MyViewModel.class);
        viewModel.getIsLogin().observe(activity, aBoolean -> {
            if (!aBoolean){
                closeFragmentWhenBackPressed();
            }
        });
        return view;
    }

    private void closeFragmentWhenBackPressed(){
        ((MainActivity)context).showViews();
        FragmentManager manager = getParentFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.clHome);
        FragmentTransaction ft = manager.beginTransaction();
        if (fragment != null) ft.remove(fragment).commit();
    }

    @Override
    public void onBackPressed() {
        viewModel.setIsLogin(false);
    }
}
