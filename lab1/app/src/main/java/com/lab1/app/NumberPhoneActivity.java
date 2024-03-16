package com.lab1.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class NumberPhoneActivity extends AppCompatActivity {
    private EditText edtNumberphone, edtOtp;
    private Button btnConfirm;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String mVerificationId;
    private TextView tvSend;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_phone);
        mAuth = FirebaseAuth.getInstance();
        initView();
        setUp();
        sendOtp();
        login();
    }

    private void showDialog() {
        if (dialog == null) {
            dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.loading_dialog);
        }
        dialog.show();
    }

    private void sendOtp() {
        tvSend.setOnClickListener((v) -> {
            String phone = edtNumberphone.getText().toString().trim();
            if (phone.isEmpty()) {
                Toast.makeText(this, "Số điện thoại trống", Toast.LENGTH_SHORT).show();
                return;
            }
            PhoneAuthOptions options = PhoneAuthOptions
                    .newBuilder(mAuth)
                    .setPhoneNumber("+84"+phone)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(this)
                    .setCallbacks(mCallback)
                    .build();
            PhoneAuthProvider.verifyPhoneNumber(options);
        });
    }

    private void login() {
        btnConfirm.setOnClickListener((v) -> {
            String otp = edtOtp.getText().toString().trim();
            if (otp.length() != 6){
                Toast.makeText(this, "Otp không chính xác", Toast.LENGTH_SHORT).show();
                return;
            }
            showDialog();
            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, otp);
            mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, MainActivity.class));
                    dialog.cancel();
                    finish();
                } else {
                    dialog.dismiss();
                    Toast.makeText(this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setUp() {
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                edtOtp.setText(phoneAuthCredential.getSmsCode());

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                e.printStackTrace();
                Toast.makeText(NumberPhoneActivity.this, "Gửi mã thất bại", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                mVerificationId = s;
            }
        };
    }

    private void initView() {
        edtNumberphone = findViewById(R.id.edt_numberphone);
        edtOtp = findViewById(R.id.edt_otp);
        btnConfirm = findViewById(R.id.btn_confirm);
        tvSend = findViewById(R.id.tv_send);

    }
}