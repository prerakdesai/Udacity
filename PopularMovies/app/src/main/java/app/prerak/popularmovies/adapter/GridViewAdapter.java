package app.prerak.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.prerak.popularmovies.R;
import app.prerak.popularmovies.bean.MovieDetails;
import app.prerak.popularmovies.providers.MoviesContract;

/**
 * Created by Prerak on 5/9/2016.
 */
public class GridViewAdapter extends ArrayAdapter<MovieDetails> {

    private final String LOG_TAG = GridViewAdapter.this.getClass().toString();
    private Context mContext;
    private int layoutResourceId;
    private List<MovieDetails> mGridData = new ArrayList<MovieDetails>();


    public GridViewAdapter(Context mContext, int layoutResourceId, List<MovieDetails> mGridData) {
        super(mContext, layoutResourceId, mGridData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.mGridData = mGridData;
    }


    public void setGridData(ArrayList mGridData) {
        this.mGridData = mGridData;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        if(isOnline() && ((MovieDetails) mGridData.get(position)).getPoster_path()!=null) {
            String url = createMovieUrl(((MovieDetails) mGridData.get(position)).getPoster_path());
            Log.d(LOG_TAG, "Loading from:" + url);
            Picasso.with(mContext).load(url).into(holder.imageView);
            Log.v(LOG_TAG, "Done from:" + url);
        }else{
            byte[] bitmapString=((MovieDetails) mGridData.get(position)).getPosterImage();
            Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapString, 0, bitmapString.length);

            holder.imageView.setImageBitmap(bitmap);
        }
        return row;
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
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

    static class ViewHolder {
        ImageView imageView;
    }

}
