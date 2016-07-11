package app.prerak.popularmovies.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.prerak.popularmovies.activity.MainActivity;
import app.prerak.popularmovies.bean.MovieDetails;
import app.prerak.popularmovies.constants.Constants;
import app.prerak.popularmovies.fragments.MovieFragment;
import app.prerak.popularmovies.util.HttpUtil;

/**
 * Created by Prerak on 5/9/2016.
 */
public class FetchMovieTask extends AsyncTask<String, Void, List<MovieDetails>> {
    String LOG_TAG = FetchMovieTask.this.getClass().toString();
    MovieFragment mainActivity;

    public FetchMovieTask(MovieFragment mainActivity) {
        this.mainActivity = mainActivity;
    }
    @Override
    protected List<MovieDetails> doInBackground(String... params) {
        String movieUrl = createMovieUrl(params[0]);
        String jsonString = HttpUtil.getJSONResult(movieUrl);
        List<MovieDetails> movies=getMovieTitles(jsonString);
        return movies;
    }

    private List<MovieDetails> getMovieTitles(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        List<MovieDetails> movieList=new ArrayList<>();

        try {
            JSONObject json=new JSONObject(jsonString);
            String movies=json.getString("results");
            movieList = mapper.readValue(movies, new TypeReference<List<MovieDetails>>() { });
        } catch (JsonGenerationException e) {
            Log.e(LOG_TAG,e.getMessage(),e);
        } catch (IOException e) {
            Log.e(LOG_TAG,e.getMessage(),e);
        } catch (JSONException e) {
            Log.e(LOG_TAG,e.getMessage(),e);
        }
        return movieList;
    }

    @Override
    protected void onPostExecute(List<MovieDetails> movieDetailses) {
        super.onPostExecute(movieDetailses);
        mainActivity.refreshMovies(movieDetailses);
    }

    private String createMovieUrl(String sort) {
        Uri.Builder movieUrl = new Uri.Builder();
        movieUrl.scheme("https");
        movieUrl.authority("api.themoviedb.org");
        movieUrl.appendPath("3");
        movieUrl.appendPath("movie");
        movieUrl.appendPath(sort);
        movieUrl.appendQueryParameter("api_key", Constants.KEY);

        return movieUrl.build().toString();
    }
}

