package model;

import java.io.Serializable;

public class User implements Serializable {

    //m signifie member

    private int UserId;

    private String FirstName;
    private int ScoreJoueur =0;

    public User() {

    }
    public User(String fisrtName, int scoreJoueur) {
        FirstName = fisrtName;
        ScoreJoueur = scoreJoueur;
    }

    public User(int userId, String fisrtName, int scoreJoueur) {
        UserId = userId;
        FirstName = fisrtName;
        ScoreJoueur = scoreJoueur;
    }

    public int getScoreJoueur() {
        return ScoreJoueur;
    }

    public void setScoreJoueur(int scoreJoueur) {
        ScoreJoueur = scoreJoueur;
    }


    public String getFirstName() {
        return FirstName;
    }
    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }


}
