package com.lab1.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edtUsername, edtPassword;
    private Button btnLogin;
    private TextView tvRegister, tvLoginWithNumberPhone, tvForgotPassword;

    private Dialog dialog;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) startActivity(new Intent(this, MainActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        initView();
        login();
        register();
        loginWithNumberphone();
        forgotPassword();
    }

    private void forgotPassword() {
        tvForgotPassword.setOnClickListener((v) -> {
            String email = edtUsername.getText().toString().trim();
            if (email.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập username", Toast.LENGTH_SHORT).show();
                return;
            }
            showDialog();
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                dialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(this, "Kiểm tra email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Lỗi", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void loginWithNumberphone() {
        tvLoginWithNumberPhone.setOnClickListener((v) -> {
            startActivity(new Intent(this, NumberPhoneActivity.class));
        });
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

    private void register() {
        tvRegister.setOnClickListener((v) -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void login() {
        btnLogin.setOnClickListener((v) -> {
            String email = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Không để trống thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            showDialog();
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(this, MainActivity.class));
                            finish();
                            dialog.cancel();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void initView() {
        edtUsername = findViewById(R.id.edt_username);
        edtPassword = findViewById(R.id.edt_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
        tvLoginWithNumberPhone = findViewById(R.id.tv_login_with_numberphone);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
    }

}