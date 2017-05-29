package edu.cnm.bootcamp.russell.myapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VER = 1;
    public static final String DB_FILENAME = "imgur_db";
    
	private static DatabaseHelper mDbHelper;
    private static SQLiteDatabase mDatabase;
    
	private DatabaseHelper(Context context) {
		super(context, DB_FILENAME, null, DB_VER);
	}

	public static synchronized DatabaseHelper getInstance(Context context) {
		if (mDbHelper == null) {
			mDbHelper = new DatabaseHelper(context);
		}
		
		return mDbHelper;
	}

    /**
     * 
     *  Return a singleton instance of the SQLiteDatabase object.
     * 
     * @param context
     * @return
     */
    public static synchronized SQLiteDatabase getDatabase(Context context) {
        if (mDatabase == null) {
            try {
                if (context != null) {
                    mDatabase = (getInstance(context)).getWritableDatabase();
                }
            } catch (SQLiteException e) {
                e.printStackTrace();
            }
        }
        
        return mDatabase;
    }
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}