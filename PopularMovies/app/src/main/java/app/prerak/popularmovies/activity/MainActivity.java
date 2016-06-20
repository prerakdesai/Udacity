package app.prerak.popularmovies.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import app.prerak.popularmovies.R;
import app.prerak.popularmovies.bean.MovieDetails;
import app.prerak.popularmovies.fragments.MovieDetailFragment;
import app.prerak.popularmovies.fragments.MovieFragment;
import app.prerak.popularmovies.providers.MoviesContract;

public class MainActivity extends AppCompatActivity implements
        MovieFragment.Callback,MovieDetailFragment.OnFragmentInteractionListener, MovieDetailFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private final String LOG_TAG = MainActivity.this.getClass().toString();
    boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;


            Bundle args = new Bundle();
            args.putString("sort", addSortOption());
            Log.d(LOG_TAG,"Setting argument");
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }

    }

    private String addSortOption() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getString(getString(R.string.sort_key), "popular");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG, "Resumed");

        MovieFragment fragment = (MovieFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movies);
        if ( null != fragment ) {
            fragment.loadMovies();
        }


    }

    @Override
    public void onItemSelected(MovieDetails movieDetails) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putSerializable("movie", movieDetails);

            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra("movie", movieDetails);

            startActivity(intent);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(LOG_TAG,"In Fragtment method");
    }


    private ContentValues getMovieContent(MovieDetails movieDetails) {
        ContentValues movieContent = new ContentValues();
        movieContent.put(MoviesContract.ID, movieDetails.getId());
        movieContent.put(MoviesContract.OVERVIEW, movieDetails.getOverview());
        movieContent.put(MoviesContract.RELEASE_DATE, movieDetails.getRelease_date());
        movieContent.put(MoviesContract.TITLE, movieDetails.getOriginal_title());
        movieContent.put(MoviesContract.VOTE, movieDetails.getVote_average());
        movieContent.put(MoviesContract.POSTER_PATH, movieDetails.getPoster_path());
        ImageView movieImage = (ImageView) findViewById(R.id.movie_imageView);
        Bitmap bitmap = ((BitmapDrawable) movieImage.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        movieContent.put(MoviesContract.POSTER, bitmapdata);
        return movieContent;
    }

    @Override
    public void markAsFavourite(MovieDetails movieDetails) {

        ContentValues movieContent = getMovieContent(movieDetails);

        Log.d(LOG_TAG, "Saving to database");
        if (!isMovieAlreadyStored(movieContent)) {
            Uri uri = getContentResolver()
                    .insert(MoviesContract.CONTENT_URI, movieContent);
            Button favourite = (Button) findViewById(R.id.button_favourite);
            favourite.setBackgroundColor(Color.BLUE);
        } else {
            getContentResolver()
                    .delete(MoviesContract.CONTENT_URI, MoviesContract.ID + "=?",
                            new String[]{movieContent.getAsString(MoviesContract.ID)});
            Button favourite = (Button) findViewById(R.id.button_favourite);
            favourite.setBackgroundColor(Color.GRAY);
        }
        Cursor cursor = getContentResolver()
                .query(MoviesContract.CONTENT_URI, new String[]{MoviesContract.TITLE}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Log.d(LOG_TAG, "Stored : " + cursor
                        .getString(cursor.getColumnIndex(MoviesContract.TITLE)));
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    private boolean isMovieAlreadyStored(ContentValues movieContent) {
        Cursor cursor = getContentResolver()
                .query(MoviesContract.CONTENT_URI, new String[]{MoviesContract.TITLE},
                        MoviesContract.ID + "=?", new String[]{movieContent.getAsString(MoviesContract.ID)}, null);
        return cursor.getCount() == 1;
    }

}
