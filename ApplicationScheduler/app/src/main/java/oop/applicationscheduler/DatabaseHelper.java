package oop.applicationscheduler;

/**
 * Created by Angelo on 11/24/2015.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Declare Variables
    private static final String DB_NAME = "MyNotes";
    public static final String TABLE_NAME = "tablenotes";
    public static final String TITLE = "title";
    public static final String NOTE = "note";

    public DatabaseHelper(Context context, String name, CursorFactory factory,
                          int version) {
        super(context, DB_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a database table
        String createQuery = "CREATE TABLE " + TABLE_NAME
                + " (_id integer primary key autoincrement," + TITLE + ", "
                + NOTE + ");";
        db.execSQL(createQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Database will be wipe on version change
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}