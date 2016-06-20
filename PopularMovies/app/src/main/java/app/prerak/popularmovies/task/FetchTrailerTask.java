package app.prerak.popularmovies.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.prerak.popularmovies.activity.MainActivity;
import app.prerak.popularmovies.activity.MovieDetailActivity;
import app.prerak.popularmovies.bean.MovieDetails;
import app.prerak.popularmovies.bean.TrailerDetails;
import app.prerak.popularmovies.constants.Constants;
import app.prerak.popularmovies.fragments.MovieDetailFragment;
import app.prerak.popularmovies.util.HttpUtil;

/**
 * Created by Prerak on 5/9/2016.
 */
public class FetchTrailerTask extends AsyncTask<String, Void, List<TrailerDetails>> {
    String LOG_TAG = FetchTrailerTask.this.getClass().toString();
    MovieDetailFragment movieDetailActivity;

    public FetchTrailerTask(MovieDetailFragment activity) {
        this.movieDetailActivity = activity;
    }
    @Override
    protected List<TrailerDetails> doInBackground(String... params) {
        String trailerUrl = createMovieDetailsUrl(params[0],params[1]);
        String jsonString = HttpUtil.getJSONResult(trailerUrl);
        List<TrailerDetails> trailers=getTrailerDetails(jsonString);
        return trailers;
    }

    private List<TrailerDetails> getTrailerDetails(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        List<TrailerDetails> movieList=new ArrayList<>();

        try {
            JSONObject json=new JSONObject(jsonString);
            String movies=json.getString("results");
            movieList = mapper.readValue(movies, new TypeReference<List<TrailerDetails>>() { });
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
    protected void onPostExecute(List<TrailerDetails> trailerDetails) {
        super.onPostExecute(trailerDetails);
        movieDetailActivity.updateTrailers(trailerDetails);
    }

    private String createMovieDetailsUrl(String id, String type) {
        Uri.Builder movieUrl = new Uri.Builder();
        movieUrl.scheme("https");
        movieUrl.authority("api.themoviedb.org");
        movieUrl.appendPath("3");
        movieUrl.appendPath("movie");
        movieUrl.appendPath(id);
        movieUrl.appendPath(type);
        movieUrl.appendQueryParameter("api_key", Constants.KEY);

        return movieUrl.build().toString();
    }
}

