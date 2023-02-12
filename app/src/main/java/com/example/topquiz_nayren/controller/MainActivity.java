package com.example.topquiz_nayren.controller;

import static com.example.topquiz_nayren.controller.GameActivity.BUNDLE_EXTRA_SCORE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.topquiz_nayren.R;

import model.User;

public class MainActivity extends AppCompatActivity {
    //préfixer les attributs avec la lettre m (pour member en anglais)
    //les variables statiques sont préfixées par la lettre s.
    private TextView mGreetingTextView;
    private EditText mNameEditText;
    private Button mPlayButton;

    private User mUser;

    private static final int GAME_ACTIVITY_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//permet de déterminer quel fichier layout utiliser. R.layout.nom_du_fichier (sans l'extension XML)
        /**
         * On ne peut utiliser la méthode findViewById() qu’après avoir utilisé la
         * méthode setContentView().
         *
         * Pour obtenir les Widgets dans l’Activity, la méthode à appeler
         * est findViewById().
         *
         * Un Widget doit avoir un attribut id dans le layout pour être référençable dans
         * l’Activity.
         */
        mGreetingTextView = findViewById(R.id.main_textview_greeting);
        mNameEditText =findViewById(R.id.main_edittext_name);
        mPlayButton = findViewById(R.id.main_button_play);

        mPlayButton.setEnabled(false);
        mUser = new User();
        /**
         * il faut pouvoir être notifié lorsque l'utilisateur commence à saisir du texte
         * dans le champ EditText correspondant
         */
        mNameEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                i=0;
                i1=0;
                i2=0;
            }

            /**
             * This is where we'll check the user input
             * La méthode à utiliser pour détecter un changement de texte dans une EditText
             * est afterTextChanged().
             */
            @Override
            public void afterTextChanged(Editable s) {
                mPlayButton.setEnabled(!s.toString().isEmpty());
            }
        });


        /**
         * Pour détecter que l'utilisateur a cliqué sur le bouton, il est nécessaire
         * d'implémenter un View.OnClickListener
         */
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            // The user just clicked
            @Override
            public void onClick(View view) {
                Intent gameActivityIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivityForResult(gameActivityIntent, GAME_ACTIVITY_REQUEST_CODE);
                mUser.setFisrtName(mNameEditText.getText().toString()); //On change le prénom du joueur
                /**
                 *  Pour créer une nouvelle Activity dans un projet, il faut :
                 * o créer les fichiers ;
                 * o créer le layout en XML ;
                 * o modifier le Manifest pour y ajouter un élément “<activity>” portant son nom.
                 *  Les Intents permettent de lancer de nouvelles Activity grâce à la
                 * méthode startActivity().
                 */
            }

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(Integer.parseInt(BUNDLE_EXTRA_SCORE), resultCode, data);

        if (GAME_ACTIVITY_REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);
        }
    }
}