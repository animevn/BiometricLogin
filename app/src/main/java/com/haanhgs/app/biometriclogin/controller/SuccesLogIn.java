package com.haanhgs.app.biometriclogin.controller;

import androidx.biometric.BiometricPrompt;

public interface SuccesLogIn {
    void onSuccessLogIn(BiometricPrompt.AuthenticationResult result);
}
