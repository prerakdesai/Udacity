package app.prerak.popularmovies.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Prerak on 6/18/2016.
 */
public class HttpUtil {

    private static final String LOG_TAG = HttpUtil.class.toString();


    public static String getJSONResult(String requestURL) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;


        try {
            URL url = new URL(requestURL);

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
        return "";
    }

}
