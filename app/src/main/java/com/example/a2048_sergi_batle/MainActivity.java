package com.example.a2048_sergi_batle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private LinearLayout verticalLayout;
    private TextView helloText;
    private TextView usernameText;
    private TextView appTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar vistas
        helloText = findViewById(R.id.hello);
        usernameText = findViewById(R.id.textView3);
        appTitle = findViewById(R.id.tittleApp);
        verticalLayout = findViewById(R.id.vertical_layout);

        // Animación de fundido (fade)
        fadeIn(helloText);
        fadeIn(usernameText);
        fadeIn(appTitle);
        fadeIn(verticalLayout);

        // Resto del código
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

    // Método para realizar animación de fundido (fade) en una vista
    private void fadeIn(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000); // Duración de la animación en milisegundos
        view.startAnimation(anim);
    }
}
