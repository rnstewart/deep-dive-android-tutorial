package edu.cnm.bootcamp.russell.myapplication.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import edu.cnm.bootcamp.russell.myapplication.datatables.TableImages;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DB_VER = 2;
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

	public static void createColumnIfNotExists(SQLiteDatabase db, String table_name, String column_name, String type) {
		if (!DatabaseHelper.checkForTableColumn(db, table_name, column_name)) {
			db.execSQL("ALTER TABLE " + table_name + " ADD COLUMN " + column_name + " " + type);
		}
	}

	public static boolean checkForTableColumn(SQLiteDatabase db, String table_name, String column_name) {
		boolean result = false;

		if (db != null) {
			try {
				Cursor c = db.query(table_name,
						new String[]{column_name},
						null,
						null,
						null,
						null,
						null);
				if (c != null) {
					result = true;
					c.close();
				}
			} catch (SQLiteException e) {
			}
		}

		return result;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	@Override
	public void onOpen(SQLiteDatabase db) {
        db.execSQL(TableImages.CREATE);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion == 2 && oldVersion == 1) {
			createColumnIfNotExists(db, TableImages.NAME, TableImages.COL_SECTION, "text");
		}
	}
}