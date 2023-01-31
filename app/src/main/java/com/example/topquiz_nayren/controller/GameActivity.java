package com.example.topquiz_nayren.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.topquiz_nayren.R;

import java.util.Arrays;

import model.Question;
import model.QuestionBank;

/**
 * elle qui sera en charge d'afficher les différentes questions à l'utilisateur.
 */
public class GameActivity extends AppCompatActivity {

    private TextView mQuestionsTextView;
    private Button mFirtsChoiceButton;
    private Button mecondChoiceButton;
    private Button mThirdChoiceButton;
    private Button mFourChoiceButton;

    private final QuestionBank mQuestionBank = generateQuestionBank();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mQuestionsTextView = findViewById(R.id.game_activity_question);
        mFirtsChoiceButton = findViewById(R.id.game_activity_button_1);
        mecondChoiceButton = findViewById(R.id.game_activity_button_2);
        mThirdChoiceButton = findViewById(R.id.game_activity_button_3);
        mFourChoiceButton = findViewById(R.id.game_activity_button_4);

    }

    public QuestionBank generateQuestionBank() {
        Question question1 = new Question(
                "Who is the creator of Android?",
                Arrays.asList(
                        "Andy Rubin",
                        "Steve Wozniak",
                        "Jake Wharton",
                        "Paul Smith"
                ),
                0
        );
        Question question2 = new Question(
                "When did the first man land on the moon?",
                Arrays.asList(
                        "1958",
                        "1962",
                        "1967",
                        "1969"
                ),
                3
        );
        Question question3 = new Question(
                "What is the house number of The Simpsons?",
                Arrays.asList(
                        "42",
                        "101",
                        "666",
                        "742"
                ),
                3
        );
        return new QuestionBank(Arrays.asList(question1, question2, question3));
    }
}