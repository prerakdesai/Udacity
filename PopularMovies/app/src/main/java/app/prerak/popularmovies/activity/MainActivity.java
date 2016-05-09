package app.prerak.popularmovies.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import app.prerak.popularmovies.R;
import app.prerak.popularmovies.adapter.GridViewAdapter;
import app.prerak.popularmovies.bean.MovieDetails;
import app.prerak.popularmovies.task.FetchMovieTask;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = MainActivity.this.getClass().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (isOnline()) {
            FetchMovieTask fetchMovieTask = new FetchMovieTask(this);
            fetchMovieTask.execute("popular");
        }
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
            Intent settings=new Intent(this,SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LOG_TAG,"Resumed");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String sort=prefs.getString(getString(R.string.sort_key), "sorting");
        Log.d(LOG_TAG,"Sorting:"+sort);
        if (isOnline()) {
            FetchMovieTask fetchMovieTask = new FetchMovieTask(this);
            fetchMovieTask.execute(sort);
        }
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager= (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return networkInfo!=null && networkInfo.isAvailable() && networkInfo.isConnected();
    }


    public void refreshMovies(final List<MovieDetails> movieDetails) {
        List<String> weather = new ArrayList<String>();
        for (MovieDetails movieDetails1 : movieDetails) {
            weather.add(movieDetails1.getPoster_path());
        }

        GridViewAdapter gridViewAdapter=new GridViewAdapter(this,R.layout.grid_item,weather);
        GridView gridView = (GridView) findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailIntent=new Intent(getApplicationContext(),MovieDetailActivity.class);
                detailIntent.putExtra("movie",movieDetails.get(position));
                startActivity(detailIntent);
            }
        });
        gridView.setAdapter(gridViewAdapter);

    }
}
