package com.example.topquiz_nayren.controller;

import static com.example.topquiz_nayren.controller.GameActivity.BUNDLE_EXTRA_SCORE;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.topquiz_nayren.R;

import model.User;

public class MainActivity extends AppCompatActivity {
    private static final String SHARED_PREF_USER_INFO_NAME = "" ;
    //préfixer les attributs avec la lettre m (pour member en anglais)
    //les variables statiques sont préfixées par la lettre s.
    private TextView mGreetingTextView;
    private EditText mNameEditText;
    private Button mPlayButton;

    private Button mPrendrePhoto;

    private TextView mwelcome_back_with_score;

    private User mUser;
    private static final String SHARED_PREF_USER_INFO_SCORE = "SHARED_PREF_USER_INFO_SCORE";

    private static final int REQUEST_CODE_GAME_ACTIVITY = 42;
    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";

    private static final int REQUEST_CODE_CAMERA_ACTIVITY = 45;

    private Button buttonImage;
    private Button buttonVideo;
    private VideoView videoView;
    private ImageView imageView;
    private static final int REQUEST_ID_READ_WRITE_PERMISSION = 99;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int REQUEST_ID_VIDEO_CAPTURE = 101;

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
        mPrendrePhoto = findViewById(R.id.button_image);

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
            @Override
            public void onClick(View v) {
                mUser.setFisrtName(mNameEditText.getText().toString()); //On change le prénom du joueur

                // The user just clicked
                getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                        .edit()
                        .putString(SHARED_PREF_USER_INFO_NAME, mNameEditText.getText().toString())
                        .apply();
                startActivityForResult(new Intent(MainActivity.this, GameActivity.class), REQUEST_CODE_GAME_ACTIVITY);
            }
        });
        greetUser();//pour que meme si on ferme l'app on garde en cache les infos de l'useur

        mPrendrePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), REQUEST_CODE_CAMERA_ACTIVITY);
            }
        });

        //Camera
        this.buttonImage = (Button) this.findViewById(R.id.button_image);
        this.buttonVideo = (Button) this.findViewById(R.id.button_video);
        this.videoView = (VideoView) this.findViewById(R.id.videoView);
        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });

    }

    private void captureImage() {
// Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
// Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
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
    @SuppressLint("StringFormatInvalid")
    private void greetUser() {
        String firstName = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME, null);
        int score = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_SCORE, -1); // -1 pour verifier si la case n'est pas null

        if (firstName != null) {
            if (score != -1) {
                mGreetingTextView.setText(getString(R.string.welcome_back_with_score) +" "+ firstName + " " + score);
            } else {
                mGreetingTextView.setText(getString(R.string.welcome_back) + " " + firstName);
            }
            mNameEditText.setText(firstName);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (REQUEST_CODE_GAME_ACTIVITY == requestCode && RESULT_OK == resultCode) {
            // Fetch the score from the Intent
            int score = data.getIntExtra(GameActivity.BUNDLE_EXTRA_SCORE, 0);

            //on change la valeur dans les shared preferences
            getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                    .edit()
                    .putInt(SHARED_PREF_USER_INFO_SCORE, score)
                    .apply();
            greetUser();
        }

        //Camera

        if (requestCode == REQUEST_ID_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                this.imageView.setImageBitmap(bp);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == REQUEST_ID_VIDEO_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Uri videoUri = data.getData();
                Log.i("MyLog", "Video saved to: " + videoUri);
                Toast.makeText(this, "Video saved to:\n" +
                        videoUri, Toast.LENGTH_LONG).show();
                this.videoView.setVideoURI(videoUri);
                this.videoView.start();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action Cancelled.",
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}