package model;

import java.util.List;

public class Question {

    private final String mQuestion; //texte de la question
    private final List<String> mChoiceList; // liste des réponses proposées
    private final int mAnswerIndex; //l'index de la réponse dans la liste précédente

    private final int indiceTemporaire;

    public Question(String question, List<String> choiceList, int answerIndex, int indiceTemporaire) {
        mQuestion = question;
        mChoiceList = choiceList;
        mAnswerIndex = answerIndex;

        this.indiceTemporaire = indiceTemporaire;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public List<String> getChoiceList() {
        return mChoiceList;
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }

    public int getIndiceTemporaire() {
        return indiceTemporaire;
    }
}
