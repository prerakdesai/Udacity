package app.prerak.popularmovies.providers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Prerak on 6/19/2016.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = MoviesDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 12;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MoviesContract.TABLE_MOVIES + "(" + MoviesContract.ID +
                " PRIMARY KEY, " +
                MoviesContract.TITLE + " TEXT NOT NULL, " +
                MoviesContract.OVERVIEW + " TEXT NOT NULL, " +
                MoviesContract.RELEASE_DATE + " TEXT NOT NULL, " +
                MoviesContract.POSTER_PATH + " TEXT NOT NULL, " +
                MoviesContract.POSTER + " INTEGER NOT NULL, " +
                MoviesContract.VOTE + " TEXT NOT NULL); ";
        db.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        db.execSQL("DROP TABLE IF EXISTS " + MoviesContract.TABLE_MOVIES);
        // re-create database
        onCreate(db);

    }
}
