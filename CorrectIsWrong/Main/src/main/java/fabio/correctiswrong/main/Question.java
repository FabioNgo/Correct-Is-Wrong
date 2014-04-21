package fabio.correctiswrong.main;

import android.util.Log;

/**
 * Created by Fabio Ngo on 4/21/2014.
 */
public class Question {
    String correct;
    String wrong;
    String content;
    public Question(){
        correct = "";
        wrong = "";
        content = "";
    }
    public Question(String content, String correct, String wrong){
        this.correct = correct;
        this.wrong = wrong;
        this.content = content;
    }
    public Question(String input){
        String[] temp = input.split(",");
        //System.out.println(temp.length);
        if(temp.length == 3){
            content = temp[0];
            correct = temp[1];
            wrong = temp[2];
        }else{
            Log.e("tag", "Cannot create Question");
        }
    }
    public String toString(){
        return content+"_" + correct + "_" + wrong;
    }
}
