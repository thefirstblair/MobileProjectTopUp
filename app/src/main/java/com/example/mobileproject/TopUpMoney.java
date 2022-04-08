package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

public class TopUpMoney extends AppCompatActivity {

    TextView money;
    EditText editTextTopUpMoney;
    Button backBtn, topUpBtn;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId, userEmail;

    long oldMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up_money);

        money = findViewById(R.id.balance);
        editTextTopUpMoney = findViewById(R.id.top_up_money);
        backBtn = findViewById(R.id.backBtn);
        topUpBtn = findViewById(R.id.topUpBtn);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userId = fAuth.getCurrentUser().getUid();
        userEmail = fAuth.getCurrentUser().getEmail();

        // Show Balance
        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
               long m1 = documentSnapshot.getLong("money");
               oldMoney = m1;
               money.setText("Your Balance: " + m1);
            }
        });

        Log.i("Test: ", "Old Money : " + oldMoney);
        // Button
        topUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String topUpAmount = editTextTopUpMoney.getText().toString().trim();
                Log.i("Test","Email: " + userEmail + " " + userId);
                if (TextUtils.isEmpty(topUpAmount)){
                    editTextTopUpMoney.setError("Required Field");
                    return;
                }
                if (Integer.parseInt(topUpAmount) <= 0){
                    editTextTopUpMoney.setError("Minimum 0 baht");
                    return;
                }

                long m2 = Integer.parseInt(topUpAmount);
                Log.i("Test","เงินทั้งหมด : " + m2);
                UpdateData(m2);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    private void UpdateData(long topUp){
        Map<String, Object> map = new HashMap<>();
        long newMoney = oldMoney + topUp;
        map.put("money", newMoney);
        fStore.collection("users").whereEqualTo("money", oldMoney).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful() && !task.getResult().isEmpty()){
                    DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                    fStore.collection("users").document(userId).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(TopUpMoney.this, "เติมเงินสำเร็จ !", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(TopUpMoney.this, "เติมเงินไม่สำเร็จ!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else {
                    Toast.makeText(TopUpMoney.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}