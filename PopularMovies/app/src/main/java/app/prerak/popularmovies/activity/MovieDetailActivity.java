package app.prerak.popularmovies.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import app.prerak.popularmovies.R;
import app.prerak.popularmovies.bean.MovieDetails;

public class MovieDetailActivity extends AppCompatActivity {

    String LOG_TAG=MovieDetailActivity.this.getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MovieDetails movieDetails=(MovieDetails)getIntent().getSerializableExtra("movie");
        TextView title=((TextView)findViewById(R.id.title_textView));
        title.setText(movieDetails.getOriginal_title());

        String url=createMovieUrl(movieDetails.getPoster_path());
        Log.d(LOG_TAG,"Loading from:"+url);
        ImageView movieImage=(ImageView) findViewById(R.id.movie_imageView);
        Picasso.with(this).load(url).into(movieImage);
        Log.v(LOG_TAG,"Done from:"+url);

        TextView overview=((TextView)findViewById(R.id.overview_textView));
        overview.setText(movieDetails.getOverview());

        TextView rating=((TextView)findViewById(R.id.rating_textView));
        rating.setText("("+movieDetails.getVote_average()+")");


        TextView release=((TextView)findViewById(R.id.release_textView));
        release.setText("Released on : "+movieDetails.getRelease_date());


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
