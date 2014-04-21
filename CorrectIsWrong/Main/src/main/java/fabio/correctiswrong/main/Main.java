package fabio.correctiswrong.main;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class Main extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //File dir = getFilesDir();
        //Toast.makeText(this,getCacheDir().getAbsolutePath(),10000000).show();


        //copyAssets();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ImageButton newGame = (ImageButton)findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                startGame();

            }
        });

    }
    private void copyAssets() {

        String Path = getFilesDir().getPath()+File.pathSeparator+"Correct.Is.Wrong";
        File dir = new File(Path);
        dir.mkdir();
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
                //File outF = new File()
                File outFile = new File(dir, filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
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
    void startGame(){
        Intent intent = new Intent(this,Game.class);
        startActivity(intent);
    }



}
