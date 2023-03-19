package com.example.topquiz_nayren.controller;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.topquiz_nayren.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import BDD.DatabaseUser;
import model.User;

public class MainActivity extends AppCompatActivity {
    private static final String SHARED_PREF_USER_INFO_NAME = "" ;
    private static final String SHARED_PREF_USER_INFO_SCORE = "SHARED_PREF_USER_INFO_SCORE";
    private static final String SHARED_PREF_USER_INFO = "SHARED_PREF_USER_INFO";
    //préfixer les attributs avec la lettre m (pour member en anglais)
    //les variables statiques sont préfixées par la lettre s.

    private TextView mGreetingTextView;
    private EditText mNameEditText;
    private Button mPlayButton;
    private User mUser;
    private Button buttonImage;
    private ImageView imageView;
    private static final int REQUEST_ID_IMAGE_CAPTURE = 100;
    private static final int REQUEST_CODE_GAME_ACTIVITY = 42;

    //Save fichier
    private Button saveButton;
    // Is a simple file name.
    // Note!! Do not allow the path.
    private String simpleFileName = "note.txt";

    //Save BDD
    private Button saveBddBouton;
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
        this.mGreetingTextView = findViewById(R.id.main_textview_greeting);
        this.mNameEditText =findViewById(R.id.main_edittext_name);
        this.mPlayButton = findViewById(R.id.main_button_play);
        this.saveButton = (Button) this.findViewById(R.id.button_save);
        this.saveBddBouton = (Button) this.findViewById(R.id.button_save_bdd);
        this.mPlayButton.setEnabled(false);

        mUser = new User();
        //On crer la BDD user
        DatabaseUser dbUser = new DatabaseUser(this);
        dbUser.createDefaultUsersIfNeed();
        dbUser.addUser(mUser);
        //List<User> userList = dbUser.getAllUser();

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
                mUser.setFirstName(mNameEditText.getText().toString()); //On change le prénom du joueur
                dbUser.updateUser(mUser);

                // The user just clicked
                getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE)
                        .edit()
                        .putString(SHARED_PREF_USER_INFO_NAME, mNameEditText.getText().toString())
                        .apply();
                startActivityForResult(new Intent(MainActivity.this, GameActivity.class), REQUEST_CODE_GAME_ACTIVITY);
            }
        });
        greetUser();//pour que meme si on ferme l'app on garde en cache les infos de l'useur


        //Camera
        this.buttonImage = (Button) this.findViewById(R.id.button_image);
        this.imageView = (ImageView) this.findViewById(R.id.imageView);
        this.buttonImage.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });
        //save fichier
        this.saveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }
    private void captureImage() {
        // Create an implicit intent, for image capture.
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Start camera and wait for the results.
        this.startActivityForResult(intent, REQUEST_ID_IMAGE_CAPTURE);
    }

    //save data
    private String saveData() {
        String data = this.mNameEditText.getText().toString();
        try {
            // Open Stream to write file.
            FileOutputStream out = this.openFileOutput(simpleFileName, MODE_PRIVATE);
            out.write(data.getBytes());
            out.close();
            Toast.makeText(this,"File saved!",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return data;
    }

    private void saveImage(Bitmap bp, String nomFichier){

        try  { // use the absolute file path here

            FileOutputStream out = this.openFileOutput(nomFichier, MODE_PRIVATE);
            bp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            out.close();
            Toast.makeText(this,"File saved!",Toast.LENGTH_SHORT).show();

            // PNG is a lossless format, the compression factor (100) is ignored
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readImage() {
        Bitmap bitmap = null;
        try {
            String data = this.mNameEditText.getText().toString();

            // Open stream to read file.
            FileInputStream in = new FileInputStream(this.getFilesDir()+"/"+data);

            // Decode file input stream into a bitmap.
            bitmap = BitmapFactory.decodeStream(in);
            this.imageView.setImageBitmap(bitmap);

            // Close the input stream.
            in.close();
        } catch (Exception e) {
            Toast.makeText(this,"Error Impossible c'est une nouvelle instance de l'app:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    private void readData() {
        try {
            // Open stream to read file.
            FileInputStream in = this.openFileInput(simpleFileName);

            BufferedReader br= new BufferedReader(new InputStreamReader(in));

            StringBuilder sb= new StringBuilder();
            String s= null;
            while((s= br.readLine())!= null)  {
                sb.append(s).append("\n");
            }
            this.mGreetingTextView.setText(sb.toString());
            Toast.makeText(this,"Yes :"+ sb.toString() ,Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("StringFormatInvalid")
    private void greetUser() {
        //Avec les getSharedPreferences
        // String firstName = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getString(SHARED_PREF_USER_INFO_NAME, null);
        // int score = getSharedPreferences(SHARED_PREF_USER_INFO, MODE_PRIVATE).getInt(SHARED_PREF_USER_INFO_SCORE, -1); // -1 pour verifier si la case n'est pas null

        //Avec recuperation des infis de la BDD
        DatabaseUser dbUser = new DatabaseUser(this);
        User userBDD =  dbUser.getUser(this.mUser.getUserId());
        String firstName = userBDD.getFirstName();
        int score = userBDD.getScoreJoueur();

        if (firstName != null) {
            if (score != -1) {
                readData();
                mGreetingTextView.setText(getString(R.string.welcome_back_with_score) +" "+ firstName + " " + score);
                readImage();
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
                String nomFichier =  saveData();
                saveImage(bp,nomFichier);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Action canceled", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Action Failed", Toast.LENGTH_LONG).show();
            }
        }
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

}