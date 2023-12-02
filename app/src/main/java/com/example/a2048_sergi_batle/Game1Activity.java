package com.example.a2048_sergi_batle;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Game1Activity extends AppCompatActivity implements View.OnTouchListener {
    Button goBack;
    private int[][] game = new int[4][4];
    LinearLayout mainLayout;
    GridLayout gridLayout;
    TextView gameTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createLayout();

        gridLayout.setOnTouchListener(new View.OnTouchListener() {
            private float xStart, yStart, xEnd, yEnd;
            private static final int MIN_DISTANCE = 150;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        xStart = event.getX();
                        yStart = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        xEnd = event.getX();
                        yEnd = event.getY();

                        float deltaX = xEnd - xStart;
                        float deltaY = yEnd - yStart;

                        // Detectar deslizamiento horizontal
                        if (Math.abs(deltaX) > MIN_DISTANCE) {
                            if (xEnd > xStart) {
                                // Deslizamiento hacia la derecha
                                Log.d("Swipe", "Right");
                                updateArrayRight();
                            } else {
                                // Deslizamiento hacia la izquierda
                                Log.d("Swipe", "Left");
                                updateArrayLeft();
                            }
                        }
                        // Detectar deslizamiento vertical
                        else if (Math.abs(deltaY) > MIN_DISTANCE) {
                            if (yEnd > yStart) {
                                // Deslizamiento hacia abajo
                                Log.d("Swipe", "Down");
                                updateArrayDown();
                            } else {
                                // Deslizamiento hacia arriba
                                Log.d("Swipe", "Up");
                                updateArrayUp();
                            }
                        }
                        break;
                }
                updateLayout();
                return true;
            }
        });
    }


    private void startGame() {
        int two = (int) (int) (Math.random() * 2) + 1;;
        int four = (int) (Math.random() * 1) + 1;
        boolean exit = false;
        while (two>=0) {
            int randomRow = (int) (Math.random() * 4);
            int randomCol = (int) (Math.random() * 4);
            if (game[randomRow][randomCol] == 0) {
                game[randomRow][randomCol] = 2;
                exit = true;
            }
            two--;
        }
        while (four>=0) {
            int randomRow = (int) (Math.random() * 4);
            int randomCol = (int) (Math.random() * 4);
            if (game[randomRow][randomCol] == 0) {
                game[randomRow][randomCol] = 4;
                exit = false;
            }
            four--;
        }
    }


    private void updateLayout() {
        gridLayout.removeAllViews(); // Elimina todas las vistas actuales del GridLayout

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final int row = i;
                final int col = j;

                TextView textView = new TextView(this);
                textView.setLayoutParams(new GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, 1f)));
                textView.setHeight(250);
                textView.setWidth(100);
                textView.setTypeface(null, Typeface.BOLD);

                GridLayout.LayoutParams params = (GridLayout.LayoutParams) textView.getLayoutParams();
                params.setMargins(20, 15, 20, 15);
                textView.setLayoutParams(params);

                textView.setBackground(getResources().getDrawable(R.drawable.cell_rectangle));
                textView.setTextSize(20);
                textView.setTextColor(Color.BLACK);
                textView.setGravity(android.view.Gravity.CENTER);
                textView.setId(View.generateViewId());

                // Verifica si el elemento del array game no es cero y establece el texto
                if (game[i][j] != 0) {
                    textView.setText(String.valueOf(game[i][j]));

                    switch (game[i][j]) {
                        case 2:
                            textView.setBackground(getResources().getDrawable(R.drawable.cell_rectangle_2));
                            break;
                        case 4:
                            textView.setBackground(getResources().getDrawable(R.drawable.cell_rectangle_4));
                            break;
                        // Agrega más casos según los valores de tu juego
                    }
                }
                gridLayout.addView(textView); // Agrega el TextView al GridLayout
            }
        }
    }



    private void updateArrayUp() {
        boolean repeat = true;

        while (repeat) {
            repeat = false;

            for (int j = 0; j < 4; j++) { // Recorre todas las columnas
                for (int i = 1; i < 4; i++) { // Comienza desde el segundo índice y recorre hacia abajo
                    if (game[i][j] != 0 && game[i - 1][j] == 0) { // Si el elemento actual no es cero y el de arriba es cero
                        int aux = game[i - 1][j]; // Intercambia los valores para mover hacia arriba
                        game[i - 1][j] = game[i][j];
                        game[i][j] = aux;
                        repeat = true;
                    }
                }
            }
        }
    }



    private void updateArrayDown() {
        boolean repeat = true;

        while (repeat) {
            repeat = false;

            for (int j = 0; j < 4; j++) { // Recorre todas las columnas
                for (int i = 2; i >= 0; i--) { // Comienza desde el penúltimo índice y recorre hacia arriba
                    if (game[i][j] != 0 && game[i + 1][j] == 0) { // Si el elemento actual no es cero y el de abajo es cero
                        int aux = game[i + 1][j]; // Intercambia los valores para mover hacia abajo
                        game[i + 1][j] = game[i][j];
                        game[i][j] = aux;
                        repeat = true;
                    }
                }
            }
        }
    }


    private void updateArrayRight() {
        boolean repeat = true;

        while (repeat) {
            repeat = false;

            for (int i = 0; i < 4; i++) { // Recorre todas las filas
                for (int j = 2; j >= 0; j--) { // Comienza desde la penúltima columna y se mueve hacia la izquierda
                    if (game[i][j] != 0 && game[i][j + 1] == 0) { // Si el elemento actual no es cero y el de la derecha es cero
                        int aux = game[i][j + 1]; // Intercambia los valores para mover hacia la derecha
                        game[i][j + 1] = game[i][j];
                        game[i][j] = aux;
                        repeat = true;
                    }
                }
            }
        }
    }


    private void updateArrayLeft() {
        boolean repeat = true;

        while (repeat) {
            repeat = false;

            for (int i = 0; i < 4; i++) { // Recorre todas las filas
                for (int j = 1; j < 4; j++) { // Comienza desde la segunda columna y se mueve hacia la derecha
                    if (game[i][j] != 0 && game[i][j - 1] == 0) { // Si el elemento actual no es cero y el de la izquierda es cero
                        int aux = game[i][j - 1]; // Intercambia los valores para mover hacia la izquierda
                        game[i][j - 1] = game[i][j];
                        game[i][j] = aux;
                        repeat = true;
                    }
                }
            }
        }
    }


    private void createLayout() {
        startGame();

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mainLayout.setGravity(Gravity.CENTER_VERTICAL);

        gameTitle = new TextView(this);
        gameTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        gameTitle.setText("2048");
        gameTitle.setTextSize(50);
        gameTitle.setTextColor(Color.BLACK);
        gameTitle.setPadding(100, 10, 10, 50);
        mainLayout.addView(gameTitle);

        gridLayout = new GridLayout(this);
        gridLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        gridLayout.setRowCount(4);
        gridLayout.setColumnCount(4);
        gridLayout.setPadding(10, 10, 10, 10);
        gridLayout.setBackground(getResources().getDrawable(R.drawable.game1background_rectangle));

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, 1f)));
                textView.setHeight(250);
                textView.setWidth(100);
                textView.setTypeface(null, Typeface.BOLD);

                GridLayout.LayoutParams params = (GridLayout.LayoutParams) textView.getLayoutParams();
                params.setMargins(20, 15, 20, 15);
                textView.setLayoutParams(params);

                textView.setBackground(getResources().getDrawable(R.drawable.cell_rectangle));
                textView.setTextSize(20);
                textView.setTextColor(Color.BLACK);
                textView.setGravity(android.view.Gravity.CENTER);
                textView.setId(View.generateViewId());

                // Verifica si el elemento del array game no es cero y establece el texto
                if (game[i][j] != 0) {
                    textView.setText(String.valueOf(game[i][j]));

                    switch (game[i][j]) {
                        case 2:
                            textView.setBackground(getResources().getDrawable(R.drawable.cell_rectangle_2));
                            break;
                        case 4:
                            textView.setBackground(getResources().getDrawable(R.drawable.cell_rectangle_4));
                            break;
                    }
                }

                gridLayout.addView(textView);
            }
        }
        mainLayout.addView(gridLayout);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        buttonParams.setMargins(100, 0, 0, 0);
        goBack = new Button(this);
        goBack.setLayoutParams(buttonParams);
        goBack.setText("GO BACK");
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Game1Activity.this, MainActivity.class));
            }
        });

        mainLayout.addView(goBack);
        setContentView(mainLayout);
    }


    private void visualizarArrayEnLogcat() {
        StringBuilder arrayStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                arrayStr.append(game[i][j]).append(" ");
            }
            arrayStr.append("\n");
        }
        Log.d("Array 2048", arrayStr.toString());
    }


    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
