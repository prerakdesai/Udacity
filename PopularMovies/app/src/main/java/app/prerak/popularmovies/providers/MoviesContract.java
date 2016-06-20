package app.prerak.popularmovies.providers;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Prerak on 6/19/2016.
 */
public class MoviesContract implements BaseColumns {
    public static final String CONTENT_AUTHORITY = "app.prerak.popularmovies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String TABLE_MOVIES = "movies";
    public static final String ID = "_id";
    public static final String TITLE = "original_title";
    public static final String OVERVIEW = "overview";
    public static final String VOTE = "vote_average";
    public static final String RELEASE_DATE = "release_date";
    public static final String POSTER = "poster";
    public static final String POSTER_PATH = "poster_path";


    // create content uri
    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
            .appendPath(TABLE_MOVIES).build();
    // create cursor of base type directory for multiple entries
    public static final String CONTENT_DIR_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;
    // create cursor of base type item for single entry
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_MOVIES;

    // for building URIs on insertion
    public static Uri buildMovieUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}


