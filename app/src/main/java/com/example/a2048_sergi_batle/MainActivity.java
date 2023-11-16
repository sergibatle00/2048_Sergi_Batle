package com.example.a2048_sergi_batle;
import android.content.res.ColorStateList;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<TextView> cells;
    private TextView cell_rectangle_0;
    private TextView cell_rectangle_1;
    private TextView cell_rectangle_2;
    private TextView cell_rectangle_3;
    private TextView cell_rectangle_4;
    private TextView cell_rectangle_5;
    private TextView cell_rectangle_6;
    private TextView cell_rectangle_7;
    private TextView cell_rectangle_8;
    private TextView cell_rectangle_9;
    private TextView cell_rectangle_10;
    private TextView cell_rectangle_11;
    private TextView cell_rectangle_12;
    private TextView cell_rectangle_13;
    private TextView cell_rectangle_14;
    private TextView cell_rectangle_15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cells = new ArrayList<>();
        cell_rectangle_0 = findViewById(R.id.tv_00);
        cell_rectangle_1 = findViewById(R.id.tv_01);
        cell_rectangle_2 = findViewById(R.id.tv_02);
        cell_rectangle_3 = findViewById(R.id.tv_03);
        cell_rectangle_4 = findViewById(R.id.tv_04);
        cell_rectangle_5 = findViewById(R.id.tv_05);
        cell_rectangle_6 = findViewById(R.id.tv_06);
        cell_rectangle_7 = findViewById(R.id.tv_07);
        cell_rectangle_8 = findViewById(R.id.tv_08);
        cell_rectangle_9 = findViewById(R.id.tv_09);
        cell_rectangle_10 = findViewById(R.id.tv_10);
        cell_rectangle_11 = findViewById(R.id.tv_11);
        cell_rectangle_12 = findViewById(R.id.tv_12);
        cell_rectangle_13 = findViewById(R.id.tv_13);
        cell_rectangle_14 = findViewById(R.id.tv_14);
        cell_rectangle_15 = findViewById(R.id.tv_15);

        cells.add(cell_rectangle_0);
        cells.add(cell_rectangle_1);
        cells.add(cell_rectangle_2);
        cells.add(cell_rectangle_3);
        cells.add(cell_rectangle_4);
        cells.add(cell_rectangle_5);
        cells.add(cell_rectangle_6);
        cells.add(cell_rectangle_7);
        cells.add(cell_rectangle_8);
        cells.add(cell_rectangle_9);
        cells.add(cell_rectangle_10);
        cells.add(cell_rectangle_11);
        cells.add(cell_rectangle_12);
        cells.add(cell_rectangle_13);
        cells.add(cell_rectangle_14);
        cells.add(cell_rectangle_15);
        asignarValoresATextViews();
    }

    private void asignarValoresATextViews() {
        for (TextView cell : cells) {
            asignarValorATextView(cell);
        }
    }

    private void asignarValorATextView(TextView cell) {
        ColorStateList color;

        Drawable background = cell.getBackground();
        int colorValue = 0;

        if (background instanceof ShapeDrawable) {
            // Haz algo si el background es ShapeDrawable
        } else if (background instanceof GradientDrawable) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                color = ((GradientDrawable) background).getColor();
                int[][] states = new int[][] {
                        new int[] { android.R.attr.state_pressed},
                        new int[] { -android.R.attr.state_pressed}
                };
                colorValue = color.getColorForState(states[0], color.getDefaultColor());
            }
        } else if (background instanceof ColorDrawable) {
            colorValue = ((ColorDrawable) background).getColor();
        }

        String colorHex = String.format("#%06X", (0xFFFFFF & colorValue));
        Log.i("Color hexadecimal: ", colorHex);

        switch (colorHex) {
            case "#EEE4DA":
                cell.setText("2");
                break;
            case "#EDE0C8":
                cell.setText("4");
                break;
            case "#F2B179":
                cell.setText("8");
                break;
            case "#F59563":
                cell.setText("16");
                break;
            case "#F67C5F":
                cell.setText("32");
                break;
            case "#F65E3B":
                cell.setText("64");
                break;
            case "#EDCF72":
                cell.setText("128");
                break;
            case "#EDCC61":
                cell.setText("256");
                break;
            case "#EDC850":
                cell.setText("512");
                break;
            case "#EDC53F":
                cell.setText("1024");
                break;
        }
    }
}
