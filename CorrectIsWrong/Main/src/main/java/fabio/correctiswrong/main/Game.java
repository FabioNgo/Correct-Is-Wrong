package fabio.correctiswrong.main;

import com.google.android.gms.appstate.AppStateManager;
import com.google.android.gms.common.api.*;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Random;
import java.util.Vector;


public class Game extends Activity {
    Vector<Question> questions;
    int score;
    int highScore;
    int current;
    boolean left;
    boolean inGame;
    Button buttonLeft, buttonRight;
    ImageView startGame;
    int time;
    int TimeLimit;
    Runnable runnable;
    Handler handler;
    TextView timerText;
    TextView question;
    TextView gameOver;
    int screenHeight;
    int screenWidth;
    Button share;
    boolean mExplicitSignOut = false;
    boolean mInSignInFlow = false; // set to true when you're in the middle of the
    // sign in flow, to know you should not attempt
    // to connect on onStart()

    public void initialize () {

        TimeLimit = 2;
        time = TimeLimit;
        inGame = false;
        left = false;
        highScore = 0;
        score = 0;
        current = 0;



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initialize();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        /**
         * GOOGLE API
         */

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
        Typeface timetf = Typeface.createFromAsset(getAssets(),
                "hlcomibm.ttf");

        timerText.setTypeface(timetf);
        timerText.setHeight(screenHeight/4);
        handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                    doStuff();
                if(time >=0) {
                    handler.postDelayed(this, 1000);
                }
                if(time < 0){
                    handler.removeCallbacks(runnable);
                    endGame();
                }


            }
            private void doStuff () {

                timerText.setText(String.valueOf(time));
                time--;
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
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                questions.add(new Question(temp));
            }
            bufferedReader.close();
        } catch (IOException e) {
        }
        /**
         * GameOver
         *
         */
        gameOver = (TextView)findViewById(R.id.gameOver);
        gameOver.setHeight(3*screenHeight/4);
        gameOver.setVisibility(View.INVISIBLE);
        Typeface gameOvertf = Typeface.createFromAsset(getAssets(),
                "seguibl.ttf");

        gameOver.setTypeface(gameOvertf);
        /**
         * Question Text
         */
        question = (TextView)findViewById(R.id.QuestionContent);
        question.setHeight(screenHeight/4);
        question.setTextSize(TypedValue.COMPLEX_UNIT_PX,screenHeight/16);
        Typeface questiontf = Typeface.createFromAsset(getAssets(),
                "Ubuntu-B.ttf");

        question.setTypeface(questiontf);
        /**
         * Score Text
         */
        TextView scoreText = (TextView)findViewById(R.id.score);
        scoreText.setHeight(screenHeight/8);
        scoreText.setWidth(screenWidth/3);
        Typeface scoretf = Typeface.createFromAsset(getAssets(),
                "ostrich-regular.ttf");
        scoreText.setTypeface(scoretf);

        TextView newScore = (TextView)findViewById(R.id.newScore);
        newScore.setHeight(screenHeight/8);
        newScore.setWidth(screenWidth/3);
        Typeface scoreTexttf = Typeface.createFromAsset(getAssets(),
                "ostrich-regular.ttf");
        newScore.setTypeface(scoreTexttf);
        /**
         * Best Score Text
         */
        TextView bestScoreText = (TextView)findViewById(R.id.best);
        bestScoreText.setHeight(screenHeight/8);
        bestScoreText.setWidth(screenWidth/3);
        bestScoreText.setTypeface(scoretf);
        bestScoreText.setText(String.valueOf(highScore));
        TextView bestScore = (TextView)findViewById(R.id.bestScore);
        bestScore.setHeight(screenHeight/8);
        bestScore.setWidth(screenWidth/3);
        bestScore.setTypeface(scoreTexttf);
        /**
         * Start Button
         */
        startGame = (ImageView)findViewById(R.id.start);
        Drawable drawable = startGame.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        int size = (int)(screenWidth*(0.617-0.417));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,size,size,true);

        startGame.setImageBitmap(scaledBitmap);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size,size);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        startGame.setLayoutParams(params);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(!inGame) startGame();
            }
        });


        /**
         * Share button
         */
        share = (Button)findViewById(R.id.share);
        share.setVisibility(View.INVISIBLE);
        Typeface sharetf = Typeface.createFromAsset(getAssets(),
                "Ubuntu-B.ttf");
        share.setTypeface(sharetf);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                try {
                    Share();
                } catch (IOException e) {
                }
            }
        });
        /**
         * Button Answer Left
         */
        Typeface answertf = Typeface.createFromAsset(getAssets(),
                "Ubuntu-C.ttf");

        scoreText.setTypeface(scoreTexttf);
        buttonLeft = (Button)findViewById(R.id.answerLeft);
        buttonLeft.setWidth(screenWidth/2);
        buttonLeft.setHeight(screenHeight/4);
        buttonLeft.setTypeface(answertf);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(inGame){
                    if(!left){
                        nextQuestion();

                    }else{
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
        buttonRight.setTypeface(answertf);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if(inGame) {
                    if(left){
                        nextQuestion();
                    }else{
                        endGame();
                    }
                }


            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (!mInSignInFlow && !mExplicitSignOut) {
            // auto sign in
        }
    }
    private void Share () throws IOException {
        /**
         * Take screenshot
         */
        Bitmap bitmap;
        View v = getWindow().getDecorView();
        v.setDrawingCacheEnabled(true);
        bitmap = Bitmap.createBitmap(v.getDrawingCache());
        v.setDrawingCacheEnabled(false);

        OutputStream fout;
        File imageFile = new File(getExternalFilesDir(ACCESSIBILITY_SERVICE),"share.png");


            fout = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
            fout.flush();
            fout.close();


        /**
         * Share
         */
        File filePath = new File (getExternalFilesDir(ACCESSIBILITY_SERVICE),"share.png");
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "My score");
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filePath));  //optional//use this when you want to send an image
        shareIntent.setType("image/jpeg");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "send"));
    }



    private void startGame () {
        startGame.setVisibility(View.INVISIBLE);
        inGame = true;
        gameOver.setVisibility(View.INVISIBLE);
        share.setVisibility(View.INVISIBLE);
        time = TimeLimit;
        timerText.setText(String.valueOf(time));
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
        startGame.setVisibility(View.VISIBLE);
        handler.removeCallbacks(runnable);
        gameOver.setVisibility(View.VISIBLE);
        share.setVisibility(View.VISIBLE);
        if(highScore<score){
            highScore = score;
            //Games.Leaderboards.submitScore(mClient, getResources().getString(R.string.leaderboardid), highScore);
            TextView highScoreText = (TextView)findViewById(R.id.best);
            highScoreText.setText(String.valueOf(highScore));
            File file = new File(getExternalFilesDir(ACCESSIBILITY_SERVICE),"score.ciw");
            FileOutputStream fileOutputStream;
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(score);
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }

        }
    }

    private void nextQuestion () {
        score++;


        handler.removeCallbacks(runnable);
        time = TimeLimit;
        timerText.setText(String.valueOf(time));
        runnable.run();
        TextView scoreText = (TextView)findViewById(R.id.score);
        scoreText.setText(String.valueOf(score));

        Question cur;
        try {
             cur = questions.get(current);
        }catch (Exception e){
            current = 0;
            cur = questions.get(current);
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
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

}
