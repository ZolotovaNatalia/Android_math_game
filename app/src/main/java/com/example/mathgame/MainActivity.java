package com.example.mathgame;

import static com.example.mathgame.GameConstants.GAME_MODE_ADDITION;
import static com.example.mathgame.GameConstants.GAME_MODE_EXTRA;
import static com.example.mathgame.GameConstants.GAME_MODE_MULTIPLICATION;
import static com.example.mathgame.GameConstants.GAME_MODE_SUBSTRUCTION;
import static com.example.mathgame.GameConstants.LIFE_NUMBER_PROP;
import static com.example.mathgame.GameConstants.TIMER_PROP;
import static com.example.mathgame.GameConstants.TRAINING_MODE_PROP;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    Button addition;
    Button substraction;
    Button multi;
    int lifeNumber;
    int timer;
    boolean isTrainingMode;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addition = findViewById(R.id.buttonAdd);
        substraction = findViewById(R.id.buttonSub);
        multi = findViewById(R.id.buttonMulti);

        sharedPreferences = getSharedPreferences("saveData", Context.MODE_PRIVATE);
        lifeNumber = sharedPreferences.getInt(LIFE_NUMBER_PROP, 5);
        timer = sharedPreferences.getInt(TIMER_PROP, 10);
        isTrainingMode = sharedPreferences.getBoolean(TRAINING_MODE_PROP, false);
        Class<Game> activityClass = Game.class;


        addition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(isTrainingMode){
                    intent = new Intent(MainActivity.this, GameTrainingMode.class);
                }else {
                    intent = new Intent(MainActivity.this, Game.class);
                }

                intent.putExtra(GAME_MODE_EXTRA, GAME_MODE_ADDITION);
                startActivity(intent);
                finish();
            }
        });

        substraction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(isTrainingMode){
                    intent = new Intent(MainActivity.this, GameTrainingMode.class);
                }else {
                    intent = new Intent(MainActivity.this, Game.class);
                }
                intent.putExtra(GAME_MODE_EXTRA, GAME_MODE_SUBSTRUCTION);
                startActivity(intent);
                finish();
            }
        });

        multi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(isTrainingMode){
                    intent = new Intent(MainActivity.this, GameTrainingMode.class);
                }else {
                    intent = new Intent(MainActivity.this, Game.class);
                }
                intent.putExtra(GAME_MODE_EXTRA, GAME_MODE_MULTIPLICATION);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}