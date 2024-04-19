package com.example.productmanagervi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productmanagervi.adapter.DistributorAdapter;
import com.example.productmanagervi.model.Distributor;
import com.example.productmanagervi.model.Response;
import com.example.productmanagervi.services.HttpRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class DistributorActivity extends AppCompatActivity {
    EditText edSearch;
    List<Distributor> list;
    HttpRequest httpRequest;
    Dialog dialog;
    EditText edNameDistributor;
    Button btnSaveDialog, btnCancelDialog;
    FloatingActionButton floatingActionButton;
    String TAG = "//==DistributorActivity";
    private RecyclerView recyclerView;
    private DistributorAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpRequest = new HttpRequest();
        setContentView(R.layout.activity_distributor);
        edSearch = findViewById(R.id.edSearch);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatActionButton);
        recyclerView = (RecyclerView) findViewById(R.id.rcvDistributor);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new DistributorAdapter();
        adapter.setOnItemClickListener(new DistributorAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String id) {
                showDialogDelete(id);
            }

            @Override
            public void updateItem(String id, String name) {
                openDialog(id, name);
            }
        });
        onResume();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog("", "");
            }
        });

        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionID, KeyEvent keyEvent) {
                if (actionID == EditorInfo.IME_ACTION_SEARCH){
                    String key = edSearch.getText().toString().trim();
                    httpRequest.callAPI().searchDistributor(key).enqueue(searchDistributor);
                }
                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        httpRequest.callAPI().getListDistributor().enqueue(getListDistributor);
    }
    //callback hien thi list
    Callback<Response<ArrayList<Distributor>>> getListDistributor = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    list =new ArrayList<>();
                    list = response.body().getData();
                    adapter.setData(list);
                    recyclerView.setAdapter(adapter);
                    for (Distributor item : list ) {
                        Log.i(TAG,"//==="+item.toString());
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable throwable) {
            Log.i(TAG,"//==Error="+throwable.getMessage());
        }
    };
    //them distributor
    Callback<Response<Distributor>> addDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    Toast.makeText(getApplicationContext(), "Them thanh cong", Toast.LENGTH_SHORT).show();
                    onResume();
                    dialog.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable throwable) {
            Log.i(TAG,"//==Error="+throwable.getMessage());
        }
    };
    //update
    Callback<Response<Distributor>> updateDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    Toast.makeText(getApplicationContext(), "Sua thanh cong", Toast.LENGTH_SHORT).show();
                    onResume();
                    dialog.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable throwable) {
            Log.i(TAG,"//==Error="+throwable.getMessage());
        }
    };
    public void openDialog(String id, String name){
        dialog = new Dialog(DistributorActivity.this);
        dialog.setContentView(R.layout.dialog_distributor);
        edNameDistributor = dialog.findViewById(R.id.edName);
        edNameDistributor.setText(name);
        btnSaveDialog = dialog.findViewById(R.id.btnSave);
        btnCancelDialog = dialog.findViewById(R.id.btnCancel);
        btnCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSaveDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strName = edNameDistributor.getText().toString().trim();
                if (strName.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Vui long nhap du lieu", Toast.LENGTH_SHORT).show();
                }else {
                    Distributor distributor = new Distributor();
                    distributor.setName(strName);
                    if (id.isEmpty()){
                        httpRequest.callAPI().addDistributor(distributor).enqueue(addDistributor);
                    }else {
                        httpRequest.callAPI().updateDistributor(id, distributor).enqueue(updateDistributor);

                    }
                }
            }
        });
        dialog.show();
    }
    //xoa
    Callback<Response<Distributor>> deleteDistributor = new Callback<Response<Distributor>>() {
        @Override
        public void onResponse(Call<Response<Distributor>> call, retrofit2.Response<Response<Distributor>> response) {
            if (response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    Toast.makeText(getApplicationContext(), "Xoa thanh cong", Toast.LENGTH_SHORT).show();
                    onResume();
                    dialog.dismiss();
                }
            }
        }

        @Override
        public void onFailure(Call<Response<Distributor>> call, Throwable throwable) {
            Log.i(TAG,"//==Error="+throwable.getMessage());
        }
    };
    public void showDialogDelete(String id){
        AlertDialog.Builder builder = new AlertDialog.Builder(DistributorActivity.this);
        builder.setTitle("Delete Distributor");
        builder.setMessage("Ban co chac chan khong?");
        builder.setPositiveButton("Xoa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                httpRequest.callAPI().deleteDistributor(id).enqueue(deleteDistributor);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Khong", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    //tim kiem
    Callback<Response<ArrayList<Distributor>>> searchDistributor = new Callback<Response<ArrayList<Distributor>>>() {
        @Override
        public void onResponse(Call<Response<ArrayList<Distributor>>> call, retrofit2.Response<Response<ArrayList<Distributor>>> response) {
            if (response.isSuccessful()){
                if (response.body().getStatus() == 200){
                    list =new ArrayList<>();
                    list = response.body().getData();
                    adapter.setData(list);
                    recyclerView.setAdapter(adapter);
                    for (Distributor item : list ) {
                        Log.i(TAG,"//==="+item.toString());
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<Response<ArrayList<Distributor>>> call, Throwable throwable) {
            Log.i(TAG,"//==Error="+throwable.getMessage());
        }
    };
}