package ngo.game.correctiswrong;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Main extends Activity {
    int screenHeight;
    int screenWidth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        copyAssets();
        /**
         * Get screen att
         */
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;

        screenWidth = displayMetrics.widthPixels;
        /**
         * Instruction
         *
         */
        TextView instruction = (TextView)findViewById(R.id.instruction);
        RelativeLayout.LayoutParams insParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT

        );
        insParams.setMargins(
                0,screenHeight/8,0,0
        );
        instruction.setLayoutParams(insParams);
        /**
         * Leaderboard button
         */
        Button leaderboard = (Button)findViewById(R.id.leaderboard);

        leaderboard.setHeight((int) (displayMetrics.heightPixels * (0.72 - 0.64)));
        RelativeLayout.LayoutParams leaderParams = new RelativeLayout.LayoutParams(
                (int) (screenWidth* (0.8 - 0.155)),
                (int) (screenHeight * (0.72 - 0.64))
        );

        leaderParams.setMargins(
                (int) (screenWidth * 0.155),
                (int) (screenHeight * 0.64), 0, 0);
        leaderboard.setLayoutParams(leaderParams);
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
        Drawable drawable = newGame.getDrawable();
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        int size = (int)(displayMetrics.heightPixels*(0.912-0.799));
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap,size,size,true);

        newGame.setImageBitmap(scaledBitmap);
        RelativeLayout.LayoutParams startParams = new RelativeLayout.LayoutParams(size,size);
        startParams.setMargins(
                (int) (screenWidth * 0.426),
                (int) (screenHeight * 0.799), 0, 0);
        newGame.setLayoutParams(startParams);
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
    private void copyAssets() {
        AssetManager assetManager = getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        for(String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = assetManager.open(filename);
                File outFile = new File(getExternalFilesDir(getApplicationContext().ACCESSIBILITY_SERVICE).toString()
                        , filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch(IOException e) {
                //Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }


}
