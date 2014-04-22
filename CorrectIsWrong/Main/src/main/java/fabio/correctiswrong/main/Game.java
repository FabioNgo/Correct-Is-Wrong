package fabio.correctiswrong.main;

import android.app.Activity;
import android.graphics.Color;
import android.os.Handler;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
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
    Button buttonLeft, buttonRight;
    ImageButton startGame;
    int time;
    int TimeLimit;
    Runnable runnable;
    Handler handler;
    boolean win;
    TextView timerText;
    TextView question;
    TextView gameOver;
    int screenHeight;
    int screenWidth;

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
        setContentView(R.layout.game);
        /**
         *Get Screen Attributes
         *
         *
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        /**
         * Timer
         */
        timerText = (TextView)findViewById(R.id.time);
        timerText.setHeight(screenHeight/4);
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

            }
        };
        /**
         * Import Highscore from file
         */
        File file = new File(getExternalFilesDir(ACCESSIBILITY_SERVICE),"score.ciw");
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                int temp;
                do {
                    temp = fileInputStream.read();
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
        /**
         *
         *
         * Import questionairs
         */
        questions = new Vector<Question>();
        InputStream inputStream;
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
        /**
         * GameOver
         *
         */
        gameOver = (TextView)findViewById(R.id.gameOver);
        gameOver.setHeight(3*screenHeight/4);
        gameOver.setVisibility(View.INVISIBLE);
        /**
         * Question Text
         */
        question = (TextView)findViewById(R.id.QuestionContent);
        question.setHeight(screenHeight/4);
        /**
         * Score Text
         */
        TextView scoreText = (TextView)findViewById(R.id.score);
        scoreText.setHeight(screenHeight/8);
        scoreText.setWidth(screenWidth/3);
        TextView newScore = (TextView)findViewById(R.id.newScore);
        newScore.setHeight(screenHeight/8);
        newScore.setWidth(screenWidth/3);
        /**
         * Score Text
         */
        TextView bestScoreText = (TextView)findViewById(R.id.best);
        bestScoreText.setHeight(screenHeight/8);
        bestScoreText.setWidth(screenWidth/3);
        bestScoreText.setText(String.valueOf(highScore));
        TextView bestScore = (TextView)findViewById(R.id.bestScore);
        bestScore.setHeight(screenHeight/8);
        bestScore.setWidth(screenWidth/3);
        /**
         * Start Button
         */
        startGame = (ImageButton)findViewById(R.id.start);
        /**
         * Button Answer Left
         */
        buttonLeft = (Button)findViewById(R.id.answerLeft);
        buttonLeft.setWidth(screenWidth/2);
        buttonLeft.setHeight(screenHeight/4);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(inGame){
                    if(!left){
                        nextQuestion();

                    }else{
                        win = false;
                        endGame();
                    }
                }


            }
        });
        /**
         * Button Answer Right
         */
        buttonRight = (Button)findViewById(R.id.answerRight);
        buttonRight.setWidth(screenWidth/2);
        buttonRight.setHeight(screenHeight/4);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(inGame) {
                    if(left){
                        nextQuestion();
                    }else{
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
        timerText.setText(String.valueOf(time));
    }

    private void startGame () {
        inGame = true;
        gameOver.setVisibility(View.INVISIBLE);
        win = false;
        time = TimeLimit;
        score = 0;
        TextView scoreText = (TextView)findViewById(R.id.score);
        scoreText.setText(String.valueOf(score));
        current = 0;
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
        gameOver.setVisibility(View.VISIBLE);
        if(win){

        }else{

        }
        if(highScore<score){
            highScore = score;
            TextView highScoreText = (TextView)findViewById(R.id.best);
            highScoreText.setText(String.valueOf(highScore));
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
        scoreText.setText(String.valueOf(score));

        Question cur;
        try {
             cur = questions.get(current);
        }catch (Exception e){
            win = true;
            endGame();
            return;
        }
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
