package fabio.correctiswrong.main;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;


public class Game extends ActionBarActivity {
    Vector<Question> questions;
    int score = 0;
    int highScore = 0;
    int current = 0;
    boolean left = false;
    boolean inGame = false;
    Button buttonLeft, buttonRight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //read highScore

        File file = new File(getExternalFilesDir(ACCESSIBILITY_SERVICE),"score.ciw");
        System.out.println(file.getAbsolutePath());
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                int temp;
                do {
                    temp = fileInputStream.read();
                    System.out.println(temp);
                    if (temp != -1) {
                        highScore = highScore * 255 + temp;
                    }

                } while (temp != -1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            try {
                file.createNewFile();
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(0);
                highScore = 0;
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
        //Import questionairs
        questions = new Vector<Question>();
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("QuestionBank.ciw");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            if (bufferedReader!=null) {
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    questions.add(new Question(temp));
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            Log.e("tag", "Fail Open QuestionBank");
        }
        setContentView(R.layout.game);
        TextView scoreText = (TextView)findViewById(R.id.score);
        scoreText.setText("Score: "+score);
        TextView highScoreText = (TextView)findViewById(R.id.highscore);
        highScoreText.setText("High Score: "+highScore);
        final Button startGame = (Button)findViewById(R.id.start);
        buttonLeft = (Button)findViewById(R.id.answer1);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(inGame){
                    if(left){
                        buttonLeft.setBackgroundColor(Color.GREEN);
                        nextQuestion();

                    }else{
                        buttonLeft.setBackgroundColor(Color.RED);
                        endGame();
                    }
                }


            }
        });
        buttonRight = (Button)findViewById(R.id.answer2);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(inGame) {
                    if(!left){
                        buttonRight.setBackgroundColor(Color.GREEN);
                        nextQuestion();
                    }else{
                        buttonRight.setBackgroundColor(Color.RED);
                        endGame();
                    }
                }


            }
        });
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startGame();

            }
        });
    }

    private void startGame () {
        inGame = true;
        current = 0;
        buttonLeft.setBackgroundColor(Color.BLACK);
        buttonRight.setBackgroundColor(Color.BLACK);
        Collections.shuffle(questions);
        Question cur = questions.get(current);
        TextView question = (TextView)findViewById(R.id.QuestionContent);
        question.setText(cur.content);
        Random random = new Random();
        left = random.nextBoolean();
        if(left){
            buttonLeft.setText(cur.correct);
            buttonRight.setText(cur.wrong);
        }else{
            buttonLeft.setText(cur.wrong);
            buttonRight.setText(cur.correct);
        }
        current++;
    }

    private void endGame () {
        inGame = false;


        if(highScore<score){
            highScore = score;
            File file = new File(getExternalFilesDir(ACCESSIBILITY_SERVICE),"score.ciw");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(score);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void nextQuestion () {
        score++;
        TextView scoreText = (TextView)findViewById(R.id.score);
        scoreText.setText("Score: "+ String.valueOf(score));

        Question cur;
        try {
             cur = questions.get(current);
        }catch (Exception e){
            endGame();
            return;
        }
        buttonLeft.setBackgroundColor(Color.BLACK);
        buttonRight.setBackgroundColor(Color.BLACK);
        TextView question = (TextView)findViewById(R.id.QuestionContent);
        question.setText(cur.content);
        Random random = new Random();
        left = random.nextBoolean();
        if(left){
            buttonLeft.setText(cur.correct);
            buttonRight.setText(cur.wrong);
        }else{
            buttonLeft.setText(cur.wrong);
            buttonRight.setText(cur.correct);
        }
        current++;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
