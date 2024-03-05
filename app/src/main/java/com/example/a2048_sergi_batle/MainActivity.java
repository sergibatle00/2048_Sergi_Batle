package com.example.a2048_sergi_batle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences("ajustes", Context.MODE_PRIVATE);


        fab = findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SettingsFragment.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnStartGame2048).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start2048Game();
            }
        });

        findViewById(R.id.btnStartSenku).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSenkuGame();
            }
        });
    }

    private void start2048Game() {
        Intent intent = new Intent(this, Game2048.class);
        startActivity(intent);
    }

    private void startSenkuGame() {
        Intent intent = new Intent(this, SenkuFragment.class);
        startActivity(intent);
    }
}