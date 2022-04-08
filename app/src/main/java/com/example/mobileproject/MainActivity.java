package com.example.mobileproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.money)
        {
            Toast.makeText(getApplicationContext(), "Click Money", Toast.LENGTH_SHORT).show();
        }
        else if (id == R.id.top_up)
        {
            Toast.makeText(getApplicationContext(), "Click Top up", Toast.LENGTH_SHORT).show();
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