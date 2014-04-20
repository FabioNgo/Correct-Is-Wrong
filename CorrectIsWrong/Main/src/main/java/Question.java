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
    public Question(String correct, String wrong, String content){
        this.correct = correct;
        this.wrong = wrong;
        this.content = content;
    }
}
