package fabio.correctiswrong.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        ImageButton newGame = (ImageButton)findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startGame();

            }
        });
        ImageView leaderboard = (ImageView)findViewById(R.id.leaderboard);
        leaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                goLeaderboard();
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
        Toast.makeText(getApplicationContext(),"Not Available",Toast.LENGTH_LONG).show();
    }

    void startGame(){
        Intent intent = new Intent(this,Game.class);
        startActivity(intent);
    }



}
