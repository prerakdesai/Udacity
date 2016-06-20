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

import app.prerak.popularmovies.activity.MovieDetailActivity;
import app.prerak.popularmovies.bean.ReviewDetails;
import app.prerak.popularmovies.constants.Constants;
import app.prerak.popularmovies.fragments.MovieDetailFragment;
import app.prerak.popularmovies.util.HttpUtil;

/**
 * Created by Prerak on 5/9/2016.
 */
public class FetchReviewTask extends AsyncTask<String, Void, List<ReviewDetails>> {
    String LOG_TAG = FetchReviewTask.this.getClass().toString();
    MovieDetailFragment activity;

    public FetchReviewTask(MovieDetailFragment activity) {
        this.activity = activity;
    }
    @Override
    protected List<ReviewDetails> doInBackground(String... params) {
        String reviewsUrl = createMovieDetailsUrl(params[0],params[1]);
        String jsonString = HttpUtil.getJSONResult(reviewsUrl);
        List<ReviewDetails> reviews=getReviewDetails(jsonString);
        return reviews;
    }

    private List<ReviewDetails> getReviewDetails(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        List<ReviewDetails> movieList=new ArrayList<>();

        try {
            JSONObject json=new JSONObject(jsonString);
            String reviews=json.getString("results");
            movieList = mapper.readValue(reviews, new TypeReference<List<ReviewDetails>>() { });
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
    protected void onPostExecute(List<ReviewDetails> reviewDetails) {
        super.onPostExecute(reviewDetails);
        activity.updateReview(reviewDetails);
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

