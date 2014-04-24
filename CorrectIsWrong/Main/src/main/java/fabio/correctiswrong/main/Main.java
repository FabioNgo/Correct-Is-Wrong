package fabio.correctiswrong.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        /**
         * Leaderboard button
         */
        Button leaderboard = (Button)findViewById(R.id.leaderboard);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        leaderboard.setHeight((int) (displayMetrics.heightPixels * (0.72 - 0.64)));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                (int) (displayMetrics.widthPixels * (0.8 - 0.155)),
                (int) (displayMetrics.heightPixels * (0.72 - 0.64))
        );

        params.setMargins(
                (int)(displayMetrics.widthPixels*0.155),
                (int)(displayMetrics.heightPixels*0.64),0,0);
        leaderboard.setLayoutParams(params);
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                goLeaderboard();
            }
        });
        /**
         * New Game
         */
        ImageButton newGame = (ImageButton)findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startGame();

            }
        });
        TextView tv = (TextView)findViewById(R.id.textView2);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String url = "https://docs.google.com/forms/d/18XuEbSUdNGRKeXvP3I4gIsz5A1H8B2-u5eDs2UUeXNg/viewform?usp=send_form";
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

    }

    private void goLeaderboard () {
        Toast.makeText(getApplicationContext(),"Not Available",Toast.LENGTH_SHORT).show();
    }

    void startGame(){
        Intent intent = new Intent(this,Game.class);
        startActivity(intent);
    }



}
