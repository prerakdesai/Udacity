package app.prerak.popularmovies.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.prerak.popularmovies.R;
import app.prerak.popularmovies.bean.MovieDetails;

public class MovieDetail extends AppCompatActivity {

    String LOG_TAG=MovieDetail.this.getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        MovieDetails movieDetails=(MovieDetails)getIntent().getSerializableExtra("movie");
        ((TextView)findViewById(R.id.title_textView)).setText(movieDetails.getOriginal_title());

        String url=createMovieUrl(movieDetails.getPoster_path());
        Log.d(LOG_TAG,"Loading from:"+url);
        Picasso.with(this).load(url).into((ImageView) findViewById(R.id.movie_imageView));
        Log.v(LOG_TAG,"Done from:"+url);

        ((TextView)findViewById(R.id.overview_textView)).setText(movieDetails.getOverview());
        ((TextView)findViewById(R.id.rating_textView)).setText("("+movieDetails.getVote_average()+")");
        ((TextView)findViewById(R.id.release_textView)).setText("Released on : "+movieDetails.getRelease_date());


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private String createMovieUrl(String image) {
        Uri.Builder movieUrl = new Uri.Builder();
        movieUrl.scheme("http");
        movieUrl.authority("image.tmdb.org");
        movieUrl.appendPath("t");
        movieUrl.appendPath("p");
        movieUrl.appendPath("w185");
        movieUrl.appendPath(image.substring(1));
        return movieUrl.build().toString();
    }




}
