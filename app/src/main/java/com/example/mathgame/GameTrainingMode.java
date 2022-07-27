package com.example.mathgame;

import static com.example.mathgame.GameConstants.FIRST_NUMBER_BOUND;
import static com.example.mathgame.GameConstants.GAME_MODE_ADDITION;
import static com.example.mathgame.GameConstants.GAME_MODE_MULTIPLICATION;
import static com.example.mathgame.GameConstants.GAME_MODE_SUBSTRUCTION;
import static com.example.mathgame.GameConstants.SECOND_NUMBER_BOUND;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class GameTrainingMode extends AppCompatActivity {

    TextView question;
    EditText answer;

    Button ok;
    Button next;

    Random random = new Random();
    int number1;
    int number2;
    int userAnswer;
    int realAnswer;

    String originalQuestion;
    String gameMode = "addition";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_training_mode);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        question = findViewById(R.id.textViewQuestion);
        answer = findViewById(R.id.editTextAnswer);
        answer.setInputType(InputType.TYPE_CLASS_PHONE);

        ok = findViewById(R.id.buttonOk);
        next = findViewById(R.id.buttonNext);

        gameContinue();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (answer.getText().toString().equals("")) {
                    return;
                }
                userAnswer = Integer.valueOf(answer.getText().toString());

                if (userAnswer == realAnswer) {
                    question.setText("Correct!");
                    ok.setEnabled(false);
                } else {
                    question.setText(String.format("Wrong! Try again\n %s", originalQuestion));
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
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(GameTrainingMode.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
}