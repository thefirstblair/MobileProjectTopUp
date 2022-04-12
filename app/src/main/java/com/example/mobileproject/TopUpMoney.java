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

import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tuples.generated.Tuple2;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
                    editTextTopUpMoney.setError("Minimum 1 Dollar");
                    return;
                }

                long m2 = Integer.parseInt(topUpAmount);
                Log.i("Test","เงินทั้งหมด : " + m2);
                UpdateData(m2);
                store();
                retrieve();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
    }

    public void store(){
        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/a87b52058d2545f18ded8f67bf4a588b"));
        Credentials credentials = Credentials.create("22741da2fe6cc501598e66a258c09d0fb218e73ece9f4cb59111418b3301e0e6");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        Wrapper a = Wrapper.load("0x2ad55a4EC9487429B712d4eE060637e409aaf484", web3, credentials, contractGasProvider);

        a.store(userEmail,new BigInteger(editTextTopUpMoney.getText().toString())).flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<TransactionReceipt>() {
            @Override
            public void accept(TransactionReceipt transactionReceipt) throws Exception {
                Log.i("store", "" + userEmail + " + " + editTextTopUpMoney.getText().toString());
            }
        });
    }

    public void retrieve(){
        final String[] s = new String[1];
        final String[] cost = new String[1];
        Web3j web3 = Web3j.build(new HttpService("https://goerli.infura.io/v3/a87b52058d2545f18ded8f67bf4a588b"));
        Credentials credentials = Credentials.create("22741da2fe6cc501598e66a258c09d0fb218e73ece9f4cb59111418b3301e0e6");
        ContractGasProvider contractGasProvider = new DefaultGasProvider();
        Wrapper a = Wrapper.load("0x2ad55a4EC9487429B712d4eE060637e409aaf484", web3, credentials, contractGasProvider);

        a.retrieve().flowable().subscribeOn(Schedulers.io()).subscribe(new Consumer<Tuple2<String, BigInteger>>() {
            @Override
            public void accept(Tuple2<String, BigInteger> stringBigIntegerTuple2) throws Exception {
                s[0] = stringBigIntegerTuple2.component1().toString();
                cost[0] = stringBigIntegerTuple2.component2().toString();
                Log.i("retrieve","r: " + s[0] + " , " + cost[0]);
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