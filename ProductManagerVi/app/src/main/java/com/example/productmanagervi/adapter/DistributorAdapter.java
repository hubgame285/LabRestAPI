package com.example.productmanagervi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productmanagervi.R;
import com.example.productmanagervi.model.Distributor;

import java.util.List;

public class DistributorAdapter extends RecyclerView.Adapter<DistributorAdapter.DistributorViewHolder>{
    public interface OnItemClickListener{
        void  onItemClick(String id);
        void updateItem(String id, String name);
    }
    public void setOnItemClickListener(OnItemClickListener listener){
        this.click = listener;
    }
    private OnItemClickListener click;
    List<Distributor> list;
    public void setData(List<Distributor> ls){
        this.list = ls;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public DistributorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.distributor_item, parent, false);
        return new DistributorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DistributorViewHolder holder, int position) {
        Distributor dis = list.get(position);
        if (dis == null){
            return;
        }
        holder.tvID.setText(String.valueOf(position+1));
        holder.tvName.setText(dis.getName());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = list.get(holder.getAdapterPosition()).getId();
                String name = list.get(holder.getAdapterPosition()).getName();
                click.updateItem(id, name);
            }
        });

        holder.btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = list.get(holder.getAdapterPosition()).getId();
                click.onItemClick(id);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class DistributorViewHolder extends RecyclerView.ViewHolder{
        private TextView tvID, tvName;
        private ImageButton btnDel, btnEdit;

        public DistributorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvID = itemView.findViewById(R.id.tvID);
            tvName = itemView.findViewById(R.id.tvName);
            btnDel = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);


        }
    }
}
