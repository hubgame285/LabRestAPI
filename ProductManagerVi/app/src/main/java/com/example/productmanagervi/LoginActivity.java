package com.example.productmanagervi;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.productmanagervi.model.Response;
import com.example.productmanagervi.model.User;
import com.example.productmanagervi.services.HttpRequest;

import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity {
    String TAG = "//===LoginActivity===";
    HttpRequest httpRequest;
    Button btnLogin;

    EditText edUserName, edPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        btnLogin = findViewById(R.id.btnLogin);
        edUserName = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edPassword);
        httpRequest = new HttpRequest();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strUserName = edUserName.getText().toString();
                String strPass = edPassword.getText().toString();
                if (strUserName.isEmpty() && strPass.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Khong duoc de trong", Toast.LENGTH_SHORT).show();
                }else {
                    User user = new User();
                    user.setUsername(strUserName);
                    user.setPassword(strPass);
                    httpRequest.callAPI().checkLogin(user).enqueue(responseCheckLogin);
                }

            }
        });
    }
    Callback<Response<User>> responseCheckLogin = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()){
                Log.i(TAG, "//==responseCheckLogin"+ response.body().toString());
                if (response.body().getStatus() == 200){
                    Toast.makeText(getApplicationContext(), "Dang nhap thanh cong", Toast.LENGTH_SHORT).show();
                    SharedPreferences sharedPreferences = getSharedPreferences("CHECK_LOGIN", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("TOKEN", response.body().getToken());
                    editor.putString("REFRESH_TOKEN", response.body().getRefreshToken());
                    editor.putString("USERNAME", response.body().getData().getUsername());
                    editor.putString("ID", response.body().getData().get_id());
                    editor.commit();
                    finish();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable throwable) {
            Toast.makeText(getApplicationContext(), "Dang nhap that bai", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "//==responseCheckLogin ERROR:"+ throwable.getMessage());
        }
    };
}