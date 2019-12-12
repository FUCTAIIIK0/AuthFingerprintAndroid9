package com.example.authfingerprintandroid9;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

import java.util.concurrent.Executor;

import static androidx.biometric.BiometricConstants.ERROR_NEGATIVE_BUTTON;




import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "main";

    @BindView(R.id.main_fingerBtn)
    Button fingerprintBtn;

    private BiometricPrompt biometricPrompt = null;
    private Executor executor = new MainThreadExecutor();

    private BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback(){
        @Override
        public void onAuthenticationError(int errorCode, CharSequence errString) {
            super.onAuthenticationError(errorCode, errString);
            if(errorCode == ERROR_NEGATIVE_BUTTON && biometricPrompt !=null){
               biometricPrompt.cancelAuthentication();
               toast(errString.toString());
            }
        }

        @Override
        public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            toast("Authentication succeed");

        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            toast("Application did not recognize the placed finger print. Please try again!");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if(biometricPrompt == null){
            biometricPrompt = new BiometricPrompt(this,executor,callback);
        }

        fingerprintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Fingerprint");
                BiometricPrompt.PromptInfo promptInfo = buildBiometricPrompt();
                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    private androidx.biometric.BiometricPrompt.PromptInfo buildBiometricPrompt() {
        return new androidx.biometric.BiometricPrompt.PromptInfo.Builder()
                .setTitle("Login")
                .setSubtitle("Login into your account")
                .setDescription("Touch your finger on the finger print sensor to authorise your account.")
                .setNegativeButtonText("Cancel")
                .build();
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}
