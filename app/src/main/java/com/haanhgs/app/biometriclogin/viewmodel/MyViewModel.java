package com.haanhgs.app.biometriclogin.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MyViewModel extends AndroidViewModel {

    private MutableLiveData<Boolean> isLogin = new MutableLiveData<>();

    public MyViewModel(@NonNull Application application) {
        super(application);
        isLogin.setValue(false);
    }

    public MutableLiveData<Boolean> getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(Boolean isLogin) {
        this.isLogin.setValue(isLogin);
    }
}
