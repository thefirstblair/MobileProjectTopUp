package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<Model> modelList;
    RecyclerView recyclerView;
    OrderAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        modelList = new ArrayList<>();
        modelList.add(new Model(getString(R.string.product1Name), getString(R.string.product1Description), R.drawable.roblox10giftcard ));
        modelList.add(new Model(getString(R.string.product2Name), getString(R.string.product2Description), R.drawable.roblox25giftcard));
        modelList.add(new Model(getString(R.string.product3Name), getString(R.string.product3Description), R.drawable.roblox50giftcard));
        modelList.add(new Model(getString(R.string.product4Name), getString(R.string.product4Description), R.drawable.roblox100giftcard));
        modelList.add(new Model(getString(R.string.product5Name), getString(R.string.product5Description), R.drawable.korblox));
        modelList.add(new Model(getString(R.string.product6Name), getString(R.string.product6Description), R.drawable.bacon));
        modelList.add(new Model(getString(R.string.product7Name), getString(R.string.product7Description), R.drawable.womanface));

        // recyclerview
        recyclerView = findViewById(R.id.recyclerViewHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        // adapter
        mAdapter = new OrderAdapter(this, modelList);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.history)
        {
            startActivity(new Intent(getApplicationContext(), History.class));

        }
        else if (id == R.id.top_up)
        {
            startActivity(new Intent(getApplicationContext(), TopUpMoney.class));
        }
        else if (id == R.id.data)
        {
            startActivity(new Intent(getApplicationContext(), Data.class));
        }
        else if (id == R.id.logout)
        {
                FirebaseAuth.getInstance().signOut(); // logout
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
        }
        return true;
    }

}