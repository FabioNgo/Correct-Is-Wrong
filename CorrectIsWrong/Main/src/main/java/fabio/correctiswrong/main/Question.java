package fabio.correctiswrong.main;

import android.util.Log;

/**
 * Created by Fabio Ngo on 4/21/2014.
 */
@SuppressWarnings ("DefaultFileTemplate")
public class Question {
    String correct;
    String wrong;
    String content;

    public Question(String input){
        String[] temp = input.split(",");
        if(temp.length == 3){
            content = temp[0];
            correct = temp[1];
            wrong = temp[2];
        }else{
        }
    }
    public String toString(){
        return content+"_" + correct + "_" + wrong;
    }
}
