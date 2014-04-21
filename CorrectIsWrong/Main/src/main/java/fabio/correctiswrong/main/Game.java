package fabio.correctiswrong.main;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
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
import java.sql.Time;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;


public class Game extends Activity {
    Vector<Question> questions;
    int score;
    int highScore;
    int current;
    boolean left;
    boolean inGame;
    Button buttonLeft, buttonRight,startGame;
    int time;
    int TimeLimit;
    Runnable runnable;
    Handler handler;
    boolean win;

    public Game () {
        TimeLimit = 5;
        time = TimeLimit;
        inGame = false;
        left = false;
        highScore = 0;
        score = 0;
        current = 0;
        win = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //read highScore
         handler = new Handler();
        runnable = new Runnable() {

            public void run() {


                    doStuff();
                if(time >0) {
                    handler.postDelayed(this, 1000);
                }
                if(time <= 0){
                    handler.removeCallbacks(runnable);
                    endGame();
                }

  /*
   * Now register it for running next time
   */


            }


        };
        File file = new File(getExternalFilesDir(ACCESSIBILITY_SERVICE),"score.ciw");
        //System.out.println(file.getAbsolutePath());
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                int temp;
                do {
                    temp = fileInputStream.read();
                    //System.out.println(temp);
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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
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
        startGame = (Button)findViewById(R.id.start);
        buttonLeft = (Button)findViewById(R.id.answer1);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(inGame){
                    if(!left){
                        buttonLeft.setBackgroundColor(Color.GREEN);
                        nextQuestion();

                    }else{
                        buttonLeft.setBackgroundColor(Color.RED);
                        win = false;
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
                    if(left){
                        buttonRight.setBackgroundColor(Color.GREEN);
                        nextQuestion();
                    }else{
                        buttonRight.setBackgroundColor(Color.RED);
                        win =false;
                        endGame();
                    }
                }


            }
        });
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(!inGame) {
                    startGame();
                }
            }
        });
    }

    private void doStuff () {
        time--;
        startGame.setText(String.valueOf(time)+"s");



    }

    private void startGame () {
        inGame = true;
        win = false;
        time = TimeLimit;
        score = 0;
        TextView scoreText = (TextView)findViewById(R.id.score);
        scoreText.setText("Score: "+ String.valueOf(score));
        TextView highScoreText = (TextView)findViewById(R.id.highscore);
        highScoreText.setText("High Score: "+highScore);
        current = 0;
        buttonLeft.setBackgroundColor(Color.BLACK);
        buttonRight.setBackgroundColor(Color.BLACK);
        runnable.run();
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
        handler.removeCallbacks(runnable);
        if(win){
            Button b = (Button)findViewById(R.id.start);
            b.setText("You Win. Tap to start a new game");
        }else{
            Button b = (Button)findViewById(R.id.start);
            b.setText("You loose. Tap to retry");
        }
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
        time = TimeLimit;
        TextView scoreText = (TextView)findViewById(R.id.score);
        scoreText.setText("Score: "+ String.valueOf(score));

        Question cur;
        try {
             cur = questions.get(current);
        }catch (Exception e){
            win = true;
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
