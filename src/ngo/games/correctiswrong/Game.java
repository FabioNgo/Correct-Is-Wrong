package ngo.games.correctiswrong;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class Game extends Activity {
    ArrayList<Question> questions;
    int score;
    int highScore;
    int current;
    boolean left;
    boolean inGame;
    Button buttonLeft, buttonRight;
    ImageView startGame;
    long time; //in milisecond
    long TimeLimit; // in milisecond
    Runnable runnable;
    Handler handler;
    TextView timerText;
    TextView question;
    TextView gameOver;
    int screenHeight;
    int screenWidth;
    Button share;
    DatabaseHelper db;
    CountDownTimer timer;

    public void initialize () {

        TimeLimit = 1000;
        time = TimeLimit;
        inGame = false;
        left = false;
        highScore = 0;
        score = 0;
        current = 0;


    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        initialize();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
        /**
         * Initialize database
         */
        try {
            db = new DatabaseHelper(getApplicationContext());
        } catch (IOException e) {
        }
        /**
         * Initialize Timer
         */
        timer = new CountDownTimer(TimeLimit, 50) {
            @Override
            public void onTick (long millisUntilFinished) {
                timerText.setText("" + millisUntilFinished / 1000.0);
            }

            @Override
            public void onFinish () {
                timerText.setText("0");
                endGame();
            }
        };
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
        timerText = (TextView) findViewById(R.id.time);
        Typeface timetf = Typeface.createFromAsset(getAssets(),
                "hlcomibm.ttf");

        timerText.setTypeface(timetf);
        timerText.setHeight(screenHeight / 4);


        /**
         * Import Highscore from file
         */
        File file = new File(getExternalFilesDir(ACCESSIBILITY_SERVICE), "score.ciw");
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
        questions = new ArrayList<Question>();
        questions = db.getAllQuestions();

        /**
         * GameOver
         *
         */
        gameOver = (TextView) findViewById(R.id.gameOver);
        gameOver.setHeight(3 * screenHeight / 4);
        gameOver.setVisibility(View.INVISIBLE);
        Typeface gameOvertf = Typeface.createFromAsset(getAssets(),
                "seguibl.ttf");

        gameOver.setTypeface(gameOvertf);
        /**
         * Question Text
         */
        question = (TextView) findViewById(R.id.QuestionContent);
        question.setHeight(screenHeight / 4);
        question.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenHeight / 20);
        /**
         * Score Text
         */
        TextView scoreText = (TextView) findViewById(R.id.score);
        scoreText.setHeight(screenHeight / 8);
        scoreText.setWidth(screenWidth / 3);
        Typeface scoretf = Typeface.createFromAsset(getAssets(),
                "ostrich-regular.ttf");
        scoreText.setTypeface(scoretf);

        TextView newScore = (TextView) findViewById(R.id.newScore);
        newScore.setHeight(screenHeight / 8);
        newScore.setWidth(screenWidth / 3);
        Typeface scoreTexttf = Typeface.createFromAsset(getAssets(),
                "ostrich-regular.ttf");
        newScore.setTypeface(scoreTexttf);
        /**
         * Best Score Text
         */
        TextView bestScoreText = (TextView) findViewById(R.id.best);
        bestScoreText.setHeight(screenHeight / 8);
        bestScoreText.setWidth(screenWidth / 3);
        bestScoreText.setTypeface(scoretf);
        bestScoreText.setText(String.valueOf(highScore));
        TextView bestScore = (TextView) findViewById(R.id.bestScore);
        bestScore.setHeight(screenHeight / 8);
        bestScore.setWidth(screenWidth / 3);
        bestScore.setTypeface(scoreTexttf);
        /**
         * Start Button
         */
        startGame = (ImageView) findViewById(R.id.start);
        Drawable drawable = startGame.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        int size = (int) (screenWidth * (0.617 - 0.417));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);

        startGame.setImageBitmap(scaledBitmap);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(size, size);
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        startGame.setLayoutParams(params);
        startGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (!inGame) startGame();
            }
        });


        /**
         * Share button
         */
        share = (Button) findViewById(R.id.share);
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
        buttonLeft = (Button) findViewById(R.id.answerLeft);
        buttonLeft.setWidth(screenWidth / 2);
        buttonLeft.setHeight(screenHeight / 4);
        buttonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (inGame) {
                    if (!left) {
                        nextQuestion();

                    } else {
                        endGame();
                    }
                }


            }
        });
        //TextView ansL = (TextView)findViewById(R.id.)

        buttonLeft.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenHeight / 20);
        /**
         * Button Answer Right
         */
        buttonRight = (Button) findViewById(R.id.answerRight);
        buttonRight.setWidth(screenWidth / 2);
        buttonRight.setHeight(screenHeight / 4);
        buttonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (inGame) {
                    if (left) {
                        nextQuestion();
                    } else {
                        endGame();
                    }
                }


            }
        });
        buttonRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, screenHeight / 20);
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
        File imageFile = new File(getExternalFilesDir(ACCESSIBILITY_SERVICE), "share.png");


        fout = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, fout);
        fout.flush();
        fout.close();


        /**
         * Share
         */
        File filePath = new File(getExternalFilesDir(ACCESSIBILITY_SERVICE), "share.png");
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
        timer.start();
        timerText.setText(String.valueOf(time));
        score = 0;
        TextView scoreText = (TextView) findViewById(R.id.score);
        scoreText.setText(String.valueOf(score));
        current = 0;
        Collections.shuffle(questions);
        Question cur = questions.get(current);
        TextView question = (TextView) findViewById(R.id.QuestionContent);
        question.setText(cur.question);
        Random random = new Random();
        left = random.nextBoolean();
        if (left) {
            buttonLeft.setText(cur.correct);
            buttonRight.setText(cur.wrong);
        } else {
            buttonLeft.setText(cur.wrong);
            buttonRight.setText(cur.correct);
        }
        current++;
    }

    private void endGame () {
        inGame = false;
        startGame.setVisibility(View.VISIBLE);
        //timer.onFinish();
        timer.cancel();
        gameOver.setVisibility(View.VISIBLE);
        share.setVisibility(View.VISIBLE);
        if (highScore < score) {
            highScore = score;
            //Games.Leaderboards.submitScore(mClient, getResources().getString(R.string.leaderboardid), highScore);
            TextView highScoreText = (TextView) findViewById(R.id.best);
            highScoreText.setText(String.valueOf(highScore));
            File file = new File(getExternalFilesDir(ACCESSIBILITY_SERVICE), "score.ciw");
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


        timer.start();
        TextView scoreText = (TextView) findViewById(R.id.score);
        scoreText.setText(String.valueOf(score));

        Question cur;
        try {
            cur = questions.get(current);
        } catch (Exception e) {
            current = 0;
            cur = questions.get(current);
        }
        TextView question = (TextView) findViewById(R.id.QuestionContent);
        question.setText(cur.question);
        Random random = new Random();
        left = random.nextBoolean();
        if (left) {
            buttonLeft.setText(cur.correct);
            buttonRight.setText(cur.wrong);
        } else {
            buttonLeft.setText(cur.wrong);
            buttonRight.setText(cur.correct);
        }
        current++;
    }

    


}
