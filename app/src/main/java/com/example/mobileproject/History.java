package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<UserProduct> userProductArrayList;
    MyAdapter myAdapter;
    FirebaseFirestore db;
    FirebaseAuth fAuth;

    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        email = fAuth.getCurrentUser().getEmail();

        userProductArrayList = new ArrayList<UserProduct>();
        myAdapter = new MyAdapter(History.this, userProductArrayList);

        recyclerView.setAdapter(myAdapter);

        db.collection("userProducts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getString("userEmail"));
                    }
                    Log.i("My Products: ", list.toString());
                } else {
                    Log.i("My Products: ", "Error getting documents: ", task.getException());
                }
            }
        });
        EventChangeListener();
    }

    private void EventChangeListener() {
        db.collection("userProducts").whereEqualTo("userEmail", email)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                if(error != null)
                {
                    Log.i("Firestore Error", error.getMessage());
                    return;
                }

                for(DocumentChange dc : value.getDocumentChanges()){
                    if (dc.getType() == DocumentChange.Type.ADDED)
                    {
                        userProductArrayList.add(dc.getDocument().toObject(UserProduct.class));
                    }
                    myAdapter.notifyDataSetChanged();
                }
            }
        });
    }
}