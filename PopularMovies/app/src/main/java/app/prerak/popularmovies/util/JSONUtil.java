package app.prerak.popularmovies.util;

import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Prerak on 6/19/2016.
 */
public class JSONUtil {

    private static String LOG_TAG = JSONUtil.class.toString();

    public static Object getObject(String jsonString) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JSONObject json = new JSONObject(jsonString);
            String movies = json.getString("results");
            return mapper.readValue(movies, new TypeReference() {
            });
        } catch (JsonGenerationException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } catch (IOException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
        }
        return null;
    }

}
