package ngo.game.correctiswrong;

import android.util.Log;

/**
 * Created by Fabio Ngo on 4/21/2014.
 */
@SuppressWarnings ("DefaultFileTemplate")
public class Question {
    String id;
    String correct;
    String wrong;
    String question;

    public Question(String id, String question, String correct, String wrong){
        this.id = id;
        this.question = question;
        this.correct = correct;
        this.wrong = wrong;
    }
    public String toString(){
        return question+"_" + correct + "_" + wrong;
    }
}
