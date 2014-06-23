/**
 * 
 */
package ngo.game.correctiswrong;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;
import android.widget.Toast;

/**
 * @author Fabio Ngo
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper {
	static String DB_NAME = "CIW.sqlite";

	public SQLiteDatabase myDatabase;
	Context myContext;

	/**
	 * @param context
	 * @throws java.io.IOException
	 */

	public DatabaseHelper(Context context) throws IOException {

		super(context, context.getExternalFilesDir(
				context.ACCESSIBILITY_SERVICE).toString(), null, 1);

		this.myContext = context;
		myDatabase = SQLiteDatabase.openDatabase(
				context.getExternalFilesDir(context.ACCESSIBILITY_SERVICE)
						.toString() + "/" + DB_NAME, null,
				SQLiteDatabase.OPEN_READONLY);
		// TODO Auto-generated constructor stub
	}

	
	
	/**
	 * 
	 * @return the array of all characters in database
	 */
	public ArrayList<Question> getAllQuestions() {
		ArrayList<Question> questions = new ArrayList<Question>();
		String selectQuery = "SELECT  * FROM " + "QUESTIONBANK";
		Cursor c = myDatabase.rawQuery(selectQuery, null);
		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			int a = 0;
			do {
				Question question = new Question(
                        c.getString(c.getColumnIndex("ID")),
                        c.getString(c.getColumnIndex("QUESTION")),
                        c.getString(c.getColumnIndex("CORRECT")),
                        c.getString(c.getColumnIndex("WRONG")));
				// adding to tags list
				questions.add(question);
				//Log.d("character", chars.get(chars.size()-1).getText());
			} while (c.moveToNext());
		}
		return questions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onCreate(android.database.sqlite
	 * .SQLiteDatabase)
	 */
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
	 * .SQLiteDatabase, int, int)
	 */
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
