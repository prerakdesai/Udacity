package app.prerak.popularmovies.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

import app.prerak.popularmovies.R;
import app.prerak.popularmovies.bean.MovieDetails;
import app.prerak.popularmovies.fragments.MovieDetailFragment;
import app.prerak.popularmovies.providers.MoviesContract;

public class MovieDetailActivity extends AppCompatActivity implements
        MovieDetailFragment.OnFragmentInteractionListener {

    String LOG_TAG = MovieDetailActivity.this.getClass().toString();
    ImageView movieImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_detail);
        Bundle arguments = new Bundle();
        final MovieDetails movieDetails=(MovieDetails) getIntent().getSerializableExtra("movie");
        arguments
                .putSerializable("movie", movieDetails);

        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(arguments);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }

    private ContentValues getMovieContent(MovieDetails movieDetails) {
        ContentValues movieContent = new ContentValues();
        movieContent.put(MoviesContract.ID, movieDetails.getId());
        movieContent.put(MoviesContract.OVERVIEW, movieDetails.getOverview());
        movieContent.put(MoviesContract.RELEASE_DATE, movieDetails.getRelease_date());
        movieContent.put(MoviesContract.TITLE, movieDetails.getOriginal_title());
        movieContent.put(MoviesContract.VOTE, movieDetails.getVote_average());
        movieContent.put(MoviesContract.POSTER_PATH, movieDetails.getPoster_path());
        movieImage = (ImageView) findViewById(R.id.movie_imageView);
        Bitmap bitmap = ((BitmapDrawable) movieImage.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitmapdata = stream.toByteArray();

        movieContent.put(MoviesContract.POSTER, bitmapdata);
        return movieContent;
    }

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


    @Override
    public void onFragmentInteraction(Uri uri) {
        Log.d(LOG_TAG, "Inside listener");
    }
}

