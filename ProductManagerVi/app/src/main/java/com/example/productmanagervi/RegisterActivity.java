package com.example.productmanagervi;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.productmanagervi.model.Distributor;
import com.example.productmanagervi.model.Response;
import com.example.productmanagervi.model.User;
import com.example.productmanagervi.services.HttpRequest;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RegisterActivity extends AppCompatActivity {
    Button btnRegist;
    TextInputEditText edUserName, edPassword, edEmail, edName, edAge;
    ImageView imageView;
    private HttpRequest httpRequest;
    File file;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        edUserName = findViewById(R.id.edUserName);
        edPassword = findViewById(R.id.edPassword);
        edEmail = findViewById(R.id.edEmail);
        edName = findViewById(R.id.edName);
        edAge = findViewById(R.id.edAge);
        imageView = findViewById(R.id.imvAvatar);
        btnRegist = (Button) findViewById(R.id.btnRegister);
        httpRequest = new HttpRequest();

        btnRegist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RequestBody _username = RequestBody.create(MediaType.parse("multipart/form-data"), edUserName.getText().toString().trim());
                RequestBody _password = RequestBody.create(MediaType.parse("multipart/form-data"), edPassword.getText().toString().trim());
                RequestBody _email = RequestBody.create(MediaType.parse("multipart/form-data"), edEmail.getText().toString().trim());
                RequestBody _name = RequestBody.create(MediaType.parse("multipart/form-data"), edName.getText().toString().trim());
                RequestBody _age = RequestBody.create(MediaType.parse("multipart/form-data"), edAge.getText().toString().trim());
                RequestBody _available = RequestBody.create(MediaType.parse("multipart/form-data"), "true");

                MultipartBody.Part muPart;
                if (file != null){
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                    muPart = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);
                }else {
                    muPart = null;
                }
                httpRequest.callAPI().register(_username,_password,_email,_name,_age,_available,muPart).enqueue(registerUser);
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });
    }

    private File createFileFromUri(Uri path, String name){
        File _file = new File(RegisterActivity.this.getCacheDir(), name+ ".png");
        try {
            InputStream in = RegisterActivity.this.getContentResolver().openInputStream(path);
            OutputStream out = new FileOutputStream(_file);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0){
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
            return _file;
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    ActivityResultLauncher<Intent> getImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data != null){
                            Uri imagePath = data.getData();

                            file = createFileFromUri(imagePath, "avatar");

                            Glide.with(RegisterActivity.this)
                                    .load(file)
                                    .thumbnail(Glide.with(RegisterActivity.this).load(R.drawable.img_1))
                                    .centerCrop()
                                    .circleCrop()
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .skipMemoryCache(true)
                                    .into(imageView);
                        }
                    }
                }
            });
    private void chooseImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        getImage.launch(intent);
    }
    Callback<Response<User>> registerUser = new Callback<Response<User>>() {
        @Override
        public void onResponse(Call<Response<User>> call, retrofit2.Response<Response<User>> response) {
            if (response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    Toast.makeText(getApplicationContext(), "Dang ki thanh cong", Toast.LENGTH_SHORT).show();

                }
            }
        }

        @Override
        public void onFailure(Call<Response<User>> call, Throwable throwable) {
            Log.i("TAG","//==Error="+throwable.getMessage());
        }
    };
 }