package com.mgok.my_lab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.HttpException;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnClick {

    private RecyclerView rcvDistributor;
    private DistributorAdapter distributorAdapter;
    private EditText edtQuery;
    private FloatingActionButton fabAdd;
    private Dialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        initView();
        getData();
        search();
        actionAdd();
    }

    private void actionAdd() {
        fabAdd.setOnClickListener((v) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View view = LayoutInflater.from(this).inflate(R.layout.add_layout, null);
            EditText editText = view.findViewById(R.id.edt_name);
            Button button = view.findViewById(R.id.btn_add);
            button.setOnClickListener((v1) -> {
                String name = editText.getText().toString().trim();
                if (name.isEmpty()) {
                    Toast.makeText(v1.getContext(), "Tên nhà phân phối trống", Toast.LENGTH_SHORT).show();
                } else {
                    if (dialog != null) dialog.dismiss();
                    addDistributor(name);
                }
            });
            builder.setView(view);
            dialog = builder.create();
            dialog.show();

        });
    }

    private void addDistributor(String name) {
        Thread thread = new Thread(() -> {
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", name);
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());
                Response<MyResponse<Distributor>> response = DistributorApi.api.addDistributor(requestBody).execute();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 500) {
                        showToast(response.body().getMessage());
                        return;
                    }
                    runOnUiThread(() -> {
                        distributorAdapter.addData(response.body().getData());
                    });
                }
            } catch (IOException e) {
                showToast("Không có kết nối mạng");
            } catch (HttpException e) {
                showToast("Đã xảy ra lỗi trong quá trình lấy dữ liệu");
            } catch (JSONException e) {
                showToast("Đã xảy ra lỗi trong quá trình gửi dữ liệu");
            }
        });
        thread.start();
    }

    private void search() {
        edtQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                findData();
                return true;
            }
            return false;
        });

        edtQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()){
                    getData();
                }else {
                    findData();
                }
            }
        });
    }

    private void findData() {
        Thread thread = new Thread(() -> {
            try {
                String query = edtQuery.getText().toString().trim();
                Response<MyResponse<ArrayList<Distributor>>> response = DistributorApi.api.searchDistributor(query).execute();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 500) {
                        showToast(response.body().getMessage());
                        return;
                    }
                    runOnUiThread(() -> {
                        distributorAdapter.setData(response.body().getData());
                    });
                }
            } catch (IOException e) {
                showToast("Không có kết nối mạng");
            } catch (HttpException e) {
                showToast("Đã xảy ra lỗi trong quá trình lấy dữ liệu");
            }
        });
        thread.start();
    }

    private void getData() {
        Thread thread = new Thread(() -> {
            try {
                Response<MyResponse<ArrayList<Distributor>>> response = DistributorApi.api.getListDistributor().execute();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().getStatus() == 500) {
                        showToast(response.body().getMessage());
                        return;
                    }
                    runOnUiThread(() -> {
                        distributorAdapter.setData(response.body().getData());
                    });
                }
            } catch (IOException e) {
                showToast("Không có kết nối mạng");
            } catch (HttpException e) {
                showToast("Đã xảy ra lỗi trong quá trình lấy dữ liệu");
            }
        });
        thread.start();
    }

    private void showToast(String msg) {
        runOnUiThread(() -> {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });
    }

    private void initView() {
        rcvDistributor = findViewById(R.id.rcv_distributor);
        edtQuery = findViewById(R.id.edt_query);
        fabAdd = findViewById(R.id.fab_add);
        distributorAdapter = new DistributorAdapter(this);
        rcvDistributor.setAdapter(distributorAdapter);
    }

    @Override
    public void delete(String id,int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa nhà phân phối");
        builder.setMessage("Bạn có muốn xóa không?");
        builder.setNegativeButton("HỦY", null);
        builder.setPositiveButton("ĐỒNG Ý", (dialog, which) -> {
            deleteDistributor(id,position);
        });
        builder.create().show();
    }

    private void deleteDistributor(String id, int position) {
        Thread thread = new Thread(() -> {
            try {
                Response<MyResponse<Distributor>> response = DistributorApi.api.deleteDistributorById(id).execute();
                if (response.isSuccessful() && response.body() != null) {
                    showToast(response.body().getMessage());
                    if (response.body().getStatus() == 200){
                        runOnUiThread(()->{
                            distributorAdapter.delete(position);
                        });
                    }
                }

            } catch (IOException e) {
                showToast("Không có kết nối mạng");
            } catch (HttpException e) {
                showToast("Đã xảy ra lỗi trong quá trình lấy dữ liệu");
            }
        });
        thread.start();
    }
}