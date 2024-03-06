package com.example.a2048_sergi_batle;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LoginActivity extends AppCompatActivity {
    TextView email, pass, result, title;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SharedPreferences sharedPreferences = getSharedPreferences("ajustes", Context.MODE_PRIVATE);
        String mailTrue = getResources().getString(R.string.email);
        String passTrue = getResources().getString(R.string.pass);

        title = findViewById(R.id.login);
        setTextViewColor(title,
                getResources().getColor(R.color.buttonPurple),
                getResources().getColor(R.color.buttonBlue)
        );


        Log.d("mailTrue", mailTrue);
        Log.d("passTrue", passTrue);


        email = findViewById(R.id.emailInput);
        pass = findViewById(R.id.passInput);
        result = findViewById(R.id.result);


        Intent intent = new Intent(this, MainActivity.class);
        findViewById(R.id.enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mailInt = email.getText().toString();
                String passInt = pass.getText().toString();
                Log.d("mailInt", mailInt);
                Log.d("passInt", passInt);

                if (mailTrue.equals(mailInt) && passInt.equals(passTrue)) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();

                    editor.putString("username", getResources().getString(R.string.username));
                    editor.putString("email", mailTrue);
                    editor.putString("pass", passTrue);
                    editor.putInt("timer", Integer.parseInt(getResources().getString(R.string.senkuTimer)));
                    editor.putInt("undo", Integer.parseInt(getResources().getString(R.string.undoTickets)));
                    editor.putString("dosmilScore", getResources().getString(R.string.dosmilScore));

                    editor.apply();
                    startActivity(intent);
                } else {
                    result.setText("Correo o contraseña incorrectos");
                }
            }
        });
    }


    public void setTextViewColor(TextView textView, int...color) {
        TextPaint paint = textView.getPaint();
        float width = paint.measureText(textView.getText().toString());

        Shader shader = new LinearGradient(0,0,width, textView.getTextSize(), color, null, Shader.TileMode.CLAMP);
        textView.getPaint().setShader(shader);
        textView.setTextColor(color[0]);

    }
}
