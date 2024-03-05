package com.example.a2048_sergi_batle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import java.io.IOException;

public class SettingsFragment extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    Button reset, apply;
    TextView senkuScore, dosmilScore, senkuTimer, undoTickets;
    EditText undo, timer, username;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_settings);

        reset = findViewById(R.id.resetScores);
        dosmilScore = findViewById(R.id.dosmilScore);
        senkuTimer =  findViewById(R.id.senkuTimer);
        apply =  findViewById(R.id.apply);
        timer =  findViewById(R.id.senkuTimer);
        undo =  findViewById(R.id.undo);
        username =  findViewById(R.id.username);

        SharedPreferences sharedPreferences = getSharedPreferences("ajustes", Context.MODE_PRIVATE);


        dosmilScore.setText(sharedPreferences.getString("dosmilScore", ""));
        timer.setText(String.valueOf(sharedPreferences.getInt("timer", 0)));
        undo.setText(String.valueOf(sharedPreferences.getInt("undo", 0)));
        username.setText(sharedPreferences.getString("username", ""));



        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dosmilScore.setText("0");
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putString("username", username.getText().toString());
                editor.putString("timer", timer.getText().toString());
                editor.putString("undo", undo.getText().toString());
                editor.putString("dosmilScore", dosmilScore.getText().toString());

                editor.apply();

            }
        });
    }

    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageButton imageButton = findViewById(R.id.imageButton);
                imageButton.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
