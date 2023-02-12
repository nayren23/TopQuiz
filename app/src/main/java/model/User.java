package model;

public class User {

    //m signifie member
    private String setFisrtName;

    private int ScoreJoueur =0;

    public int getScoreJoueur() {
        return ScoreJoueur;
    }

    public void setScoreJoueur(int scoreJoueur) {
        ScoreJoueur = scoreJoueur;
    }

    public String getFisrtName() {
        return setFisrtName;
    }

    public void setFisrtName(String fisrtName) {
        setFisrtName = fisrtName;
    }
}
