package com.lab1.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnRegister;
    private ImageView imvBack;
    private FirebaseAuth mAuth;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        initView();
        register();
        back();
    }

    private void back() {
        imvBack.setOnClickListener((v) -> {
            finish();
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
        btnRegister.setOnClickListener((v) -> {
            showDialog();
            String email = edtUsername.getText().toString();
            String password = edtPassword.getText().toString();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
                                dialog.cancel();
                                finish();
                            } else {
                                dialog.dismiss();
                                Toast.makeText(RegisterActivity.this, task.getException().getLocalizedMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
    }

    private void initView() {
        edtPassword = findViewById(R.id.edt_password);
        edtUsername = findViewById(R.id.edt_username);
        btnRegister = findViewById(R.id.btn_register);
        imvBack = findViewById(R.id.imv_back);
    }
}