package com.example.topquiz_nayren.controller;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topquiz_nayren.R;

import java.util.ArrayList;
import java.util.Arrays;

import model.Question;
import model.QuestionBank;

/**
 * elle qui sera en charge d'afficher les différentes questions à l'utilisateur.
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mQuestionsTextView;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private int mScore;
    private QuestionBank getQuestionBank;

    private final QuestionBank mQuestionBank = generateQuestionBank();

    private Question questionActuelle;

    private ArrayList<Integer> listetemporaireQuestionUtilise = new ArrayList<Integer>();

    private int mRemainingQuestionCount;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRemainingQuestionCount = 4 ;//on initialise le nb de questions
        setContentView(R.layout.activity_game);

        mQuestionsTextView = findViewById(R.id.game_activity_question);
        mAnswerButton1 = findViewById(R.id.game_activity_button_1);
        mAnswerButton2 = findViewById(R.id.game_activity_button_2);
        mAnswerButton3 = findViewById(R.id.game_activity_button_3);
        mAnswerButton4 = findViewById(R.id.game_activity_button_4);

        // Use the same listener for the four buttons.
        // The view id value will be used to distinguish the button triggered
        mAnswerButton1.setOnClickListener(this);
        mAnswerButton2.setOnClickListener(this);
        mAnswerButton3.setOnClickListener(this);
        mAnswerButton4.setOnClickListener(this);
        mRemainingQuestionCount--;

        if (mRemainingQuestionCount > 0) {
            this.questionActuelle = mQuestionBank.getNextQuestion();
            displayQuestion(this.questionActuelle);
        } else {
            // No question left, end the game
            Toast.makeText(this, "FIN DU JEUX 😢", Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Well done!").setMessage("Your score is " + mScore)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.putExtra(BUNDLE_EXTRA_SCORE, mScore);
                            setResult(RESULT_OK, intent);
                            finish();                        }
                    })
                    .create()
                    .show();
        }
        this.questionActuelle= this.mQuestionBank.getNextQuestion();

        displayQuestion(this.questionActuelle);

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
                0,
                0);
        Question question2 = new Question(
                "When did the first man land on the moon?",
                Arrays.asList(
                        "1958",
                        "1962",
                        "1967",
                        "1969"
                ),
                3,
                1);
        Question question3 = new Question(
                "What is the house number of The Simpsons?",
                Arrays.asList(
                        "42",
                        "101",
                        "666",
                        "742"
                ),
                3,
                2);
        return new QuestionBank(Arrays.asList(question1, question2, question3));
    }

    /**
     * Cette méthode peut prendre en paramètre une instance de Question puis en afficher les valeurs
     * @param question
     */
    private void displayQuestion(final Question question){
        // Set the text for the question text view and the four buttons

        this.mQuestionsTextView.setText(question.getQuestion());
        this.mAnswerButton1.setText(question.getChoiceList().get(0));
        this.mAnswerButton2.setText(question.getChoiceList().get(1));
        this.mAnswerButton3.setText(question.getChoiceList().get(2));
        this.mAnswerButton4.setText(question.getChoiceList().get(3));
    }

    @Override
    public void onClick(View v) {
        int index;

        if (v == mAnswerButton1) {
            index = 0;
        } else if (v == mAnswerButton2) {
            index = 1;
        } else if (v == mAnswerButton3) {
            index = 2;
        } else if (v == mAnswerButton4) {
            index = 3;
        } else {
            throw new IllegalStateException("Unknown clicked view : " + v);
        }
        //verification reponses
        if(answerVerification(index)){
            this.mScore++;
            Toast.makeText(this, "Correct 😍", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this, "Faux 😰", Toast.LENGTH_LONG).show();

        }
        this.listetemporaireQuestionUtilise.add(this.questionActuelle.getIndiceTemporaire());

        this.mQuestionBank.questionBankShuflle(this.mQuestionBank.getQuestionList());//on mélange les questions
        this.questionActuelle = this.getQuestionBank.getNextQuestion();//on choisit la prochaine question


        //verification de si on a deja poser la question
        while(this.listetemporaireQuestionUtilise.contains(this.questionActuelle.getIndiceTemporaire())){
            this.mQuestionBank.questionBankShuflle(this.mQuestionBank.getQuestionList());//on mélange les questions
            this.questionActuelle = this.getQuestionBank.getNextQuestion();//on choisit la prochaine question
        }

        displayQuestion(this.questionActuelle);//on affiche les questions à l'écran
    }

    public void changementQuestions(){

    }


    /**
     * Retourne un boolean si l'useaur a fait le bon choix
     * @param choixUseur
     * @return
     */
    public boolean answerVerification(int choixUseur){
        return this.questionActuelle.getAnswerIndex() == choixUseur;
    }
}