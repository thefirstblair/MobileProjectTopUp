package com.example.mobileproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Product3Activity extends AppCompatActivity {

    ImageView imageView;
    TextView productName, productPrice, productDescription;
    Button buyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        imageView = findViewById(R.id.imageViewInfo);
        productName = findViewById(R.id.productNameInInfo);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.descriptionInfo);
        buyBtn = findViewById(R.id.buyBtn);

        productName.setText("$50 Roblox Gift Card");
        productPrice.setText("$50");
        productDescription.setText(getString(R.string.product3));
        imageView.setImageResource(R.drawable.roblox50giftcard);

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(InfoActivity.this, SummaryActivity.class);
//                startActivity(intent);
//                SaveCart();
                Toast.makeText(Product3Activity.this, "Click Buy!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}