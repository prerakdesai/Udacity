package app.prerak.popularmovies.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.picasso.Picasso;

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
import app.prerak.popularmovies.activity.MovieDetail;
import app.prerak.popularmovies.bean.MovieDetails;

/**
 * Created by Prerak on 5/9/2016.
 */
public class FetchMovieTask extends AsyncTask<String, Void, List<MovieDetails>> {
    String LOG_TAG = FetchMovieTask.this.getClass().toString();
    MainActivity mainActivity;

    public FetchMovieTask(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }
    @Override
    protected List<MovieDetails> doInBackground(String... params) {
        String movieUrl = createMovieUrl(params[0]);
        String jsonString = getMovieDetails(movieUrl);
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

    private String getMovieDetails(String movieUrl) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        try {
            URL url = new URL(movieUrl);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                return null;
            }
            String json = buffer.toString();
            Log.d(LOG_TAG, json);
            return json;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing stream", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
        return null;
    }

    private String createMovieUrl(String sort) {
        Uri.Builder movieUrl = new Uri.Builder();
        movieUrl.scheme("https");
        movieUrl.authority("api.themoviedb.org");
        movieUrl.appendPath("3");
        movieUrl.appendPath("movie");
        movieUrl.appendPath(sort);
        movieUrl.appendQueryParameter("api_key", "<your key>");

        return movieUrl.build().toString();
    }
}

