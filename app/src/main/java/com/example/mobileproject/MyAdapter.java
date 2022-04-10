package com.example.mobileproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserProduct> userProductArrayList;

    public MyAdapter(Context context, ArrayList<UserProduct> userProductArrayList) {
        this.context = context;
        this.userProductArrayList = userProductArrayList;
    }

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        UserProduct userProduct = userProductArrayList.get(position);
        holder.productName.setText(userProduct.productName);
        holder.productCode.setText(userProduct.productCode);
        holder.productTime.setText(String.valueOf(userProduct.productTime));
    }

    @Override
    public int getItemCount() {
        return userProductArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView productName, productCode, productTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.tvProductName);
            productCode = itemView.findViewById(R.id.tvProductCode);
            productTime = itemView.findViewById(R.id.tvProductTime);
        }
    }
}
