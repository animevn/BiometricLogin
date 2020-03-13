package com.haanhgs.app.biometriclogin.controller;

import androidx.biometric.BiometricPrompt;

public interface OnSuccesLogIn {
    void onSuccess(BiometricPrompt.AuthenticationResult result);
}
