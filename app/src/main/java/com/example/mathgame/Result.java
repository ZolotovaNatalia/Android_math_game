package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class Result extends AppCompatActivity {

    TextView result;
    Button playAgain;
    Button exit;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        result = findViewById(R.id.result);
        playAgain = findViewById(R.id.buttonPlayAgain);
        exit = findViewById(R.id.buttonExit);

        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);

        result.setText(String.format(Locale.getDefault(), "Your score: %s", score));

        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Result.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        });

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}