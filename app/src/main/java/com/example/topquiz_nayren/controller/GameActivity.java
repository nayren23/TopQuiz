package com.example.topquiz_nayren.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.topquiz_nayren.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import model.Question;
import model.QuestionBank;

/**
 * elle qui sera en charge d'afficher les différentes questions à l'utilisateur.
 */
public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String BUNDLE_STATE_SCORE = "BUNDLE_STATE_SCORE";
    private static final String BUNDLE_STATE_QUESTION_COUNT = "BUNDLE_STATE_QUESTION_COUNT";
    private static final String BUNDLE_STATE_QUESTION_BANK = "BUNDLE_STATE_QUESTION_BANK";

    public static final String BUNDLE_STATE_QUESTION = "BUNDLE_STATE_QUESTION";
    public static final String RESULT_SCORE = "RESULT_SCORE";
    private TextView mQuestionsTextView;
    private Button mAnswerButton1;
    private Button mAnswerButton2;
    private Button mAnswerButton3;
    private Button mAnswerButton4;

    private int mScore;
    private QuestionBank getQuestionBank  ;

    private final QuestionBank mQuestionBank = generateQuestionBank();

    private Question questionActuelle;

    private ArrayList<Integer> listetemporaireQuestionUtilise = new ArrayList<Integer>();

    private boolean mEnableTouchEvents;
    private int mRemainingQuestionCount;

    public static final String BUNDLE_EXTRA_SCORE = "BUNDLE_EXTRA_SCORE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRemainingQuestionCount = 2 ;//on initialise le nb de questions
        setContentView(R.layout.activity_game);
        mEnableTouchEvents = true;
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

        getQuestionBank = mQuestionBank;
        System.out.println("liste question départ " +mQuestionBank );
        this.questionActuelle= this.getQuestionBank.getNextQuestion();
        run();

        displayQuestion(this.questionActuelle);
        if (savedInstanceState != null) {
            mScore = savedInstanceState.getInt(BUNDLE_STATE_SCORE);
            mRemainingQuestionCount = savedInstanceState.getInt(BUNDLE_STATE_QUESTION);
        } else {
            mScore = 0;
            mRemainingQuestionCount = 2;
        }
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

        Question question4 = new Question(
                "C'est quoi le meilleur animé ?",
                Arrays.asList(
                        "SNK",
                        "Vinland Saga",
                        "Demons Slayer",
                        "One piece"
                ),
                0,
                1);
        return new QuestionBank(Arrays.asList(question1, question2, question3,question4));
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
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouchEvents && super.dispatchTouchEvent(ev);
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
            System.out.println("le score est de : " + this.mScore);
            Toast.makeText(this, "Correct 😍" + this.mScore , Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Faux 😰" + this.mScore , Toast.LENGTH_SHORT).show();
        }
        mEnableTouchEvents = false;
        suppressionQuestionsListe();

        run();
    }


    /**
     * Retourne un boolean si l'useaur a fait le bon choix
     * @param choixUseur
     * @return
     */
    public boolean answerVerification(int choixUseur){
        return this.questionActuelle.getAnswerIndex() == choixUseur;
    }

    private void endGame() {
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

    public void run() {
        mEnableTouchEvents = true;

        mRemainingQuestionCount--;
        System.out.println("il reste " + mRemainingQuestionCount +"questions");
        if (mRemainingQuestionCount <= 0) {
            endGame();
        } else {
            System.out.println("je passe dans la prochaine question");
            System.out.println("la liste des questions: " + getQuestionBank.getNextQuestion() );
            displayQuestion( this.questionActuelle);
        }
    }

    public  void suppressionQuestionsListe(){
        this.listetemporaireQuestionUtilise.add(this.questionActuelle.getIndiceTemporaire());

        this.getQuestionBank.questionBankShuflle(this.getQuestionBank.getQuestionList());//on mélange les questions
        this.questionActuelle = this.getQuestionBank.getNextQuestion();//on choisit la prochaine question


        //verification de si on a deja poser la question
        while(this.listetemporaireQuestionUtilise.contains(this.questionActuelle.getIndiceTemporaire())){
            this.getQuestionBank.questionBankShuflle(this.getQuestionBank.getQuestionList());//on mélange les questions
            this.questionActuelle = this.getQuestionBank.getNextQuestion();//on choisit la prochaine question
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(BUNDLE_STATE_SCORE, mScore);
        outState.putInt(BUNDLE_STATE_QUESTION_COUNT, mRemainingQuestionCount);
        outState.putSerializable(BUNDLE_STATE_QUESTION_BANK, (Serializable) getQuestionBank);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}