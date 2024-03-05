package com.example.a2048_sergi_batle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class SenkuFragment extends AppCompatActivity {
    int select;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_senku);
        select = 0;

        findViewById(R.id.mode1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 1;
                startGame();
            }
        });

        findViewById(R.id.mode2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 2;
                startGame();
            }
        });

        findViewById(R.id.mode3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 3;
                startGame();
            }
        });

        findViewById(R.id.mode4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = 4;
                startGame();
            }
        });

    }

    private void startGame() {
        SharedPreferences sharedPreferences = getSharedPreferences("ajustes", Context.MODE_PRIVATE);

        int undo_tickets = sharedPreferences.getInt("undo", 0);
        int timer = sharedPreferences.getInt("timer", 0);

        Intent intent = new Intent(this, GameSenku.class);
        intent.putExtra("mode", select);
        intent.putExtra("undo", undo_tickets);
        intent.putExtra("timer", timer);
        startActivity(intent);
    }
}
