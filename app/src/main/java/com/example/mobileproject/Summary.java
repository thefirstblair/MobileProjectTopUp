package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import jnr.ffi.annotations.In;

public class Summary extends AppCompatActivity {

    TextView productName, productCode;
    Button redirectBtn, homeBtn;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;

    String email;
    String userId;
    String[] latestProduct;

    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        productName = findViewById(R.id.productName_Summary);
        productCode = findViewById(R.id.productCode_Summary);
        redirectBtn = findViewById(R.id.redirectBtn);
        homeBtn = findViewById(R.id.homeBtn);

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        email = fAuth.getCurrentUser().getEmail();


       fStore.collection("userProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.get("productName") + "," + document.get("productCode"));
                    }
                    Log.i("Summary", list.toString());
                    Log.i("Summary", list.get(list.size() - 1));
                    latestProduct = list.get(list.size() - 1).split(",");
                    productName.setText(latestProduct[0]);
                    productCode.setText(latestProduct[1]);

                } else {
                    Log.i("Summary", "Error getting documents: ", task.getException());
                }
            }
        });
       redirectBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("https://www.roblox.com/redeem"));
                        startActivity(intent);
                    }
                });
       homeBtn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), MainActivity.class));
           }
       });
    }



}