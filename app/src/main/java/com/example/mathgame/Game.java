package com.example.mathgame;

import static com.example.mathgame.GameConstants.COUNT_DOWN_INTERVAL;
import static com.example.mathgame.GameConstants.FIRST_NUMBER_BOUND;
import static com.example.mathgame.GameConstants.GAME_MODE_ADDITION;
import static com.example.mathgame.GameConstants.GAME_MODE_EXTRA;
import static com.example.mathgame.GameConstants.GAME_MODE_MULTIPLICATION;
import static com.example.mathgame.GameConstants.GAME_MODE_SUBSTRUCTION;
import static com.example.mathgame.GameConstants.LIFE_NUMBER_PROP;
import static com.example.mathgame.GameConstants.SECOND_NUMBER_BOUND;
import static com.example.mathgame.GameConstants.TIMER_PROP;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;
import java.util.Random;

public class Game extends AppCompatActivity {

    TextView score;
    TextView life;
    TextView time;

    TextView question;
    EditText answer;

    Button ok;
    Button next;
    Random random = new Random();
    int number1;
    int number2;
    int userAnswer;
    int realAnswer;
    int userScore;
    int lifeStart;
    int userLife;

    CountDownTimer timer;
    boolean timerRunning;
    int timerStartMillis;
    long timeLeftInMillis;
    String originalQuestion;
    String gameMode = "addition";
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        score = findViewById(R.id.textViewScore);
        life = findViewById(R.id.textViewLife);
        time = findViewById(R.id.textViewTime);

        question = findViewById(R.id.textViewQuestion);
        answer = findViewById(R.id.editTextAnswer);
        answer.setInputType(InputType.TYPE_CLASS_PHONE);

        ok = findViewById(R.id.buttonOk);
        next = findViewById(R.id.buttonNext);

        getProperties();
        gameContinue();
        startTimer();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.getText().toString().equals("")) {
                    return;
                }
                userAnswer = Integer.valueOf(answer.getText().toString());

                if (userAnswer == realAnswer) {
                    userScore = userScore + 10;
                    score.setText(String.format("%s", userScore));
                    question.setText("Correct!");
                    pauseTimer();
                    ok.setEnabled(false);
                } else {
                    userLife = userLife - 1;
                    question.setText(String.format("Wrong! Try again\n %s", originalQuestion));
                    life.setText(String.format("%s", userLife));
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("NEXT QUESTION");
                answer.setText("");
                answer.setVisibility(View.VISIBLE);
                ok.setEnabled(true);
                gameContinue();

                if(!timerRunning){
                    resetTimer();
                    startTimer();
                }
            }
        });
        lifeTextChangeListener();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(Game.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void lifeTextChangeListener() {
        life.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                int leftLife = Integer.valueOf(s.toString());
                if (leftLife == 0) {
                    pauseTimer();
                    Toast.makeText(getApplicationContext(), "Game over", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Game.this, Result.class);
                    intent.putExtra("score", userScore);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void gameContinue() {
        number1 = random.nextInt(FIRST_NUMBER_BOUND);
        number2 = random.nextInt(SECOND_NUMBER_BOUND);

        switch (gameMode) {
            case GAME_MODE_ADDITION:
                realAnswer = number1 + number2;
                originalQuestion = String.format("%s + %s", number1, number2);
                break;
            case GAME_MODE_SUBSTRUCTION:
                realAnswer = number1 - number2;
                System.out.println("SUBSTRUCTION answer = " + realAnswer);
                originalQuestion = String.format("%s - %s", number1, number2);
                break;
            case GAME_MODE_MULTIPLICATION:
                realAnswer = number1 * number2;
                System.out.println("GAME_MODE_MULTIPLICATION answer = " + realAnswer);
                originalQuestion = String.format("%s * %s", number1, number2);
                break;
        }
        question.setText(originalQuestion);
    }

    private void getProperties() {
        Intent intent = getIntent();
        gameMode = intent.getStringExtra(GAME_MODE_EXTRA);

        sharedPreferences = getSharedPreferences("saveData", Context.MODE_PRIVATE);
        lifeStart = sharedPreferences.getInt(LIFE_NUMBER_PROP, 5);
        life.setText(String.format("%s", lifeStart));
        userLife = lifeStart;

        timerStartMillis = sharedPreferences.getInt(TIMER_PROP, 10) * 1000;
        timeLeftInMillis = timerStartMillis;
        System.out.println("getProperties on timer: leftTimeInSeconds = " + timeLeftInMillis);
        time.setText(String.format(Locale.getDefault(), "%03d", timerStartMillis));
    }

    public void startTimer() {
        timer = new CountDownTimer(timeLeftInMillis, COUNT_DOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int leftTimeInSeconds = (int) (timeLeftInMillis / 1000);

                System.out.println("onTick on timer: leftTimeInSeconds = " + leftTimeInSeconds);
                System.out.println("onTick on timer: timerstart = " + timerStartMillis);

                updateTextTimer(leftTimeInSeconds);
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                pauseTimer();
                userLife = userLife - 1;
                question.setText("Time is up!");
                answer.setVisibility(View.GONE);
                ok.setEnabled(false);
                life.setText(String.format("%s", userLife));
            }
        }.start();

        timerRunning = true;
    }

    private void updateTextTimer(int leftTimeInSeconds) {
        String timeLeft = String.format(Locale.getDefault(), "%03d", leftTimeInSeconds);
        time.setText(timeLeft);
    }

    private void resetTimer() {
        timeLeftInMillis = timerStartMillis;
        int leftTimeInSeconds = (int) (timeLeftInMillis / 1000);

        System.out.println("resetTimer on timer: leftTimeInSeconds = " + leftTimeInSeconds);
        System.out.println("resetTimer on timer: timerstart = " + timerStartMillis);

        updateTextTimer(leftTimeInSeconds);
    }

    private void pauseTimer() {
        timer.cancel();
        timerRunning = false;
    }
}