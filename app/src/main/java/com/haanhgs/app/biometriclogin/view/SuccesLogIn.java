package com.haanhgs.app.biometriclogin.view;

import androidx.biometric.BiometricPrompt;

public interface SuccesLogIn {
    void onSuccessLogIn(BiometricPrompt.AuthenticationResult result);
}
