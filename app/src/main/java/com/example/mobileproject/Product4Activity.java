package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Product4Activity extends AppCompatActivity {
    ImageView imageView;
    TextView productName, productPrice, productDescription;
    Button buyBtn;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    String userId, userEmail, productCode;
    long myMoney;


    LocalDateTime myDateOj;
    DateTimeFormatter myFormatObj;

    String formattedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        myDateOj = LocalDateTime.now();
        myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        formattedDate = myDateOj.format(myFormatObj);

        imageView = findViewById(R.id.imageViewInfo);
        productName = findViewById(R.id.productNameInInfo);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.descriptionInfo);
        buyBtn = findViewById(R.id.buyBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        userEmail = fAuth.getCurrentUser().getEmail();

        productName.setText(getString(R.string.product4Name));
        productPrice.setText("$100");
        productDescription.setText(getString(R.string.product4Description));
        imageView.setImageResource(R.drawable.roblox100giftcard);

        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                long m1 = documentSnapshot.getLong("money");
                myMoney = m1;
            }
        });

        buyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMoney(100) == true)
                {
                    createProduct();
                    createUserProduct();
                    updateData(100);
                    Toast.makeText(Product4Activity.this, "ซื้อสินค้าเรียบร้อย", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Summary.class));
                }
                else{
                    Toast.makeText(Product4Activity.this, "ไม่สามารถซื้อสินค้าได้ โปรดตรวจสอบยอดเงินอีกครั้ง", Toast.LENGTH_SHORT).show();
                }
            }
        }
        );

    }

    public boolean checkMoney(int productCost) {
        if (myMoney >= productCost)
        {
            return true;
        }
        else {
            return false;
        }
    }
    public void createProduct(){
        DocumentReference documentReference = fStore.collection("products").document();
        Map<String,Object> product = new HashMap<>();
        product.put("name", getString(R.string.product4Name));
        product.put("price", 100);
        product.put("detail", getString(R.string.product4Description));
        productCode = getRandomString();
        product.put("code", productCode);

        documentReference.set(product).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("Product", "onSuccess: product is created!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("Product", "onFailure: " + e.toString());
            }
        });
    }

    public void createUserProduct(){
        DocumentReference documentReference_user = fStore.collection("userProducts").document();
        Map<String,Object> userProduct = new HashMap<>();
        userProduct.put("userEmail", userEmail);
        userProduct.put("productCode", productCode);
        userProduct.put("productName", getString(R.string.product4Name));
        userProduct.put("productTime", formattedDate);

        documentReference_user.set(userProduct).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.i("userProduct", "onSuccess: user product is created!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("userProduct", "onFailure: " + e.toString());
            }
        });
    }

    public void updateData(int productCost){
        Map<String, Object> map = new HashMap<>();
        long moneyAfterBuy = myMoney - productCost;
        map.put("money", moneyAfterBuy);
        fStore.collection("users").whereEqualTo("money", myMoney).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()){
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    fStore.collection("users").document(userId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                        }
                    });
                }else {
                    Toast.makeText(Product4Activity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String getRandomString(){
        String randomString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * randomString.length());
            salt.append(randomString.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}