package model;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Dans l'idéal, à chaque nouvelle partie, il faudrait que les questions soient différentes,
 * et affichées dans un ordre aléatoire.
 */
public class QuestionBank {

    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

    /**
     * Mélangez la liste de questions avant de la stocker.
     * @param questionList
     */
    public QuestionBank(List<Question> questionList) {
        Collections.shuffle(questionList);
        System.out.println("Affichage de la liste des questions" + questionList);
        this.mQuestionList = questionList;
    }

    public void questionBankShuflle(List<Question> questionList) {
        Collections.shuffle(questionList);
        System.out.println("Affichage de la liste des questions" + questionList);
        this.mQuestionList = questionList;
    }
    /**
     *  // Boucle sur les questions et renvoie une nouvelle question à chaque appel.
     * @return
     */
    public Question getNextQuestion() {

        Question prochaineQuesion;

        if(this.mNextQuestionIndex<this.mQuestionList.size()){
            prochaineQuesion = this.mQuestionList.get(mNextQuestionIndex);
            this.mNextQuestionIndex +=1;
        }
        else {
            this.mNextQuestionIndex =0;
            prochaineQuesion = this.mQuestionList.get(mNextQuestionIndex);
        }
        return prochaineQuesion;

    }

    public List<Question> getQuestionList() {
            return mQuestionList;
    }

    public void setQuestionList(List<Question> questionList) {
        mQuestionList = questionList;
    }

    public int getNextQuestionIndex() {
        return mNextQuestionIndex;
    }

    public void setNextQuestionIndex(int nextQuestionIndex) {
        mNextQuestionIndex = nextQuestionIndex;
    }

    @Override
    public String toString() {
        return "QuestionBank{" +
                "mQuestionList=" + mQuestionList +
                ", mNextQuestionIndex=" + mNextQuestionIndex +
                '}';
    }
}