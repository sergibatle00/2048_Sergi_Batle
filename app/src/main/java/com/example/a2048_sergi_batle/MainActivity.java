package com.example.a2048_sergi_batle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button game1;
    Button game2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        game1 = findViewById(R.id.game1);
        game2 = findViewById(R.id.game2);
        game1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cuando se hace clic en el botón, se inicia la segunda actividad
                startActivity(new Intent(MainActivity.this, Game1Activity.class));
            }
        });
        game2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cuando se hace clic en el botón, se inicia la segunda actividad
                startActivity(new Intent(MainActivity.this, Game2Activity.class));
            }
        });
    }
}
