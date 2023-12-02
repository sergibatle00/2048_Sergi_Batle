package com.example.a2048_sergi_batle;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class Game2Activity extends AppCompatActivity {
    private int[][] game = new int[7][7];
    Button goBack;
    GridLayout gridLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createLayout();

        visualizarArrayEnLogcat();
    }


    private void startGame() {
        int[][] posiciones = {
                {0, 0}, {0, 1}, {0, 5}, {0, 6},
                {1, 0}, {1, 1}, {1, 5}, {1, 6},
                {5, 0}, {5, 1}, {5, 5}, {5, 6},
                {6, 0}, {6, 1}, {6, 5}, {6, 6},
        };

        for (int[] position : posiciones) {
            int fila = position[0];
            int columna = position[1];

            int index = fila * gridLayout.getColumnCount() + columna;

            if (index >= 0 && index < gridLayout.getChildCount()) {
                View view = gridLayout.getChildAt(index);
                TextView textView = (TextView) view;
                textView.setLayoutParams(new GridLayout.LayoutParams(
                        GridLayout.spec(GridLayout.UNDEFINED, 1f),
                        GridLayout.spec(GridLayout.UNDEFINED, 1f)));
                textView.setHeight(150);

                GridLayout.LayoutParams params = (GridLayout.LayoutParams) textView.getLayoutParams();
                params.setMargins(3, 3, 3, 3);
                textView.setLayoutParams(params);

                textView.setBackground(getResources().getDrawable(R.drawable.game2background_rectangle));
                textView.setText("");
                textView.setGravity(android.view.Gravity.CENTER);
                textView.setOnClickListener(null);
            }
        }
    }


    private void createLayout() {
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        mainLayout.setGravity(Gravity.CENTER_VERTICAL);

        TextView gameTitle = new TextView(this);
        gameTitle.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        gameTitle.setText("Senku");
        gameTitle.setTextSize(50);
        gameTitle.setTextColor(Color.BLACK);
        gameTitle.setPadding(100, 10, 10, 50);
        mainLayout.addView(gameTitle);

        gridLayout = new GridLayout(this);
        gridLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        gridLayout.setRowCount(7);
        gridLayout.setColumnCount(7);
        gridLayout.setPadding(10, 10, 10, 10);
        gridLayout.setBackground(getResources().getDrawable(R.drawable.game2background_rectangle));

        for (int i = 0; i < 49; i++) {
            final int row = i / 7;
            final int col = i % 7;

            TextView textView = new TextView(this);
            textView.setLayoutParams(new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f)));
            textView.setHeight(150);

            GridLayout.LayoutParams params = (GridLayout.LayoutParams) textView.getLayoutParams();
            params.setMargins(3, 3, 3, 3);
            textView.setLayoutParams(params);

            textView.setBackground(getResources().getDrawable(R.drawable.circle_checked));
            textView.setTextSize(20);
            textView.setTextColor(Color.BLACK);
            textView.setText("");
            textView.setGravity(android.view.Gravity.CENTER);
            textView.setId(View.generateViewId());

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView clickedTextView = (TextView) v;
                    if (clickedTextView.getBackground().getConstantState().equals(getResources().getDrawable(R.drawable.circle_unchecked).getConstantState())) {
                        clickedTextView.setBackground(getResources().getDrawable(R.drawable.circle_checked));
                        game[row][col] = 1;
                    } else{
                        clickedTextView.setBackground(getResources().getDrawable(R.drawable.circle_unchecked));
                        game[row][col] = 0;
                    }
                    visualizarArrayEnLogcat();
                }
            });
            gridLayout.addView(textView);
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
                startActivity(new Intent(Game2Activity.this, MainActivity.class));
            }
        });
        mainLayout.addView(goBack);
        startGame();
        setContentView(mainLayout);
    }


    private void visualizarArrayEnLogcat() {
        StringBuilder arrayStr = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                arrayStr.append(game[i][j]).append(" ");
            }
            arrayStr.append("\n");
        }
        Log.d("ArrayLog", arrayStr.toString());
    }
}
