package com.example.mobileproject;
import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<Model> modelList;
    Context context;

    public OrderAdapter(Context context, List<Model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.listitem, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder( ViewHolder holder, int position) {

        String nameOfProduct = modelList.get(position).getProductName();
        String descriptionOfProduct = modelList.get(position).getProductDetail();
        int images = modelList.get(position).getProductPhoto();

        holder.productName.setText(nameOfProduct);
        holder.productDescription.setText(descriptionOfProduct);
        holder.imageView.setImageResource(images);

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView productName, productDescription;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.productImage);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            // lets get the position of the view in list and then work on it

            int position = getAdapterPosition();

            if (position == 0) {
                Intent intent = new Intent(context, Product1Activity.class);
                context.startActivity(intent);
            }

            if (position == 1) {
                Intent intent2 = new Intent(context, Product2Activity.class);
                context.startActivity(intent2);
            }

            if (position == 2) {
                Intent intent3 = new Intent(context, Product3Activity.class);
                context.startActivity(intent3);
            }

            if (position == 3){
                Intent intent4 = new Intent(context, Product4Activity.class);
                context.startActivity(intent4);
            }

            if (position == 4){
                Intent intent5 = new Intent(context, Product5Activity.class);
                context.startActivity(intent5);
            }
            if (position == 5){
                Intent intent6 = new Intent(context, Product6Activity.class);
                context.startActivity(intent6);
            }

            if (position == 6){
                Intent intent7= new Intent(context, Product7Activity.class);
                context.startActivity(intent7);
            }
        }
    }
}