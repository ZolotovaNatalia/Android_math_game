package com.example.mathgame;

import static com.example.mathgame.GameConstants.GAME_MODE_ADDITION;
import static com.example.mathgame.GameConstants.LIFE_NUMBER_PROP;
import static com.example.mathgame.GameConstants.TIMER_PROP;
import static com.example.mathgame.GameConstants.TRAINING_MODE_PROP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class Settings extends AppCompatActivity {

    TextView life;
    TextView lifeLabel;
    TextView time;
    TextView timeLabel;
    Button save;
    CheckBox trainingMode;

    int lifeNumber = 5;
    int timer = 10000;
    boolean isTrainingMode = true;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        life = findViewById(R.id.lifeNumber);
        lifeLabel = findViewById(R.id.textViewLifeLabel);
        time = findViewById(R.id.time);
        timeLabel = findViewById(R.id.textViewTimeLabel);
        save = findViewById(R.id.save);

        trainingMode = findViewById(R.id.checkBoxTrain);

        trainingMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    disableLifeAndTimeTextViews();
                }else {
                    enableLifeAndTimeTextViews();
                }
            }
        });

        retrieveData();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void enableLifeAndTimeTextViews() {
        lifeLabel.setTextColor(Color.parseColor("#3D6BF3"));
        life.setEnabled(true);
        life.setTextColor(Color.parseColor("#3D6BF3"));

        time.setEnabled(true);
        timeLabel.setTextColor(Color.parseColor("#3D6BF3"));
        time.setTextColor(Color.parseColor("#3D6BF3"));
    }

    private void disableLifeAndTimeTextViews() {
        life.setTextColor(Color.GRAY);
        lifeLabel.setTextColor(Color.GRAY);
        life.setEnabled(false);

        time.setTextColor(Color.GRAY);
        time.setEnabled(false);
        timeLabel.setTextColor(Color.GRAY);
    }

    private void saveData() {
        lifeNumber = Integer.valueOf(life.getText().toString());
        timer = Integer.valueOf(time.getText().toString());

        sharedPreferences = getSharedPreferences("saveData", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(LIFE_NUMBER_PROP, lifeNumber);
        editor.putInt(TIMER_PROP, timer);
        editor.putBoolean(TRAINING_MODE_PROP, trainingMode.isChecked());
        editor.commit();
    }

    private void retrieveData() {
        sharedPreferences = getSharedPreferences("saveData", Context.MODE_PRIVATE);
        isTrainingMode = sharedPreferences.getBoolean(TRAINING_MODE_PROP, false);
        trainingMode.setChecked(isTrainingMode);

        if (isTrainingMode) {
            disableLifeAndTimeTextViews();
        }else {
            enableLifeAndTimeTextViews();
            lifeNumber = sharedPreferences.getInt(LIFE_NUMBER_PROP, 5);
            timer = sharedPreferences.getInt(TIMER_PROP, 10);
            life.setText(String.format("%s", lifeNumber));
            time.setText(String.format("%s", timer));
        }
    }

}