package com.mgok.my_lab;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.DistributorViewHolder> {
    private ArrayList<Distributor> arrayList;
    private OnClick onClick;

    public DistributorAdapter(OnClick onClick) {
        this.onClick = onClick;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Distributor> data) {
        arrayList = data;
        notifyDataSetChanged();
    }

    public void addData(Distributor data) {
        arrayList.add(data);
        notifyItemInserted(arrayList.size() - 1);
    }

    public void delete(int position) {
        arrayList.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public DistributorAdapter.DistributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distributor_item_layout, parent, false);
        return new DistributorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorAdapter.DistributorViewHolder holder, int position) {
        Distributor distributor = arrayList.get(position);
        if (distributor != null) {
            holder.tvOrder.setText(String.valueOf(position));
            holder.tvName.setText(distributor.getName());
            holder.imvDelete.setOnClickListener((v) -> {
                onClick.delete(distributor.getId(), position);
            });
        }
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }


    public static class DistributorViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvOrder;
        private final TextView tvName;
        private final ImageButton imvDelete;

        public DistributorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrder = itemView.findViewById(R.id.tv_order);
            tvName = itemView.findViewById(R.id.tv_name);
            imvDelete = itemView.findViewById(R.id.imv_delete);
        }
    }
}
