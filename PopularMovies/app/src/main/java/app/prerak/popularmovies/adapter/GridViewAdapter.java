package app.prerak.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
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

/**
 * Created by Prerak on 5/9/2016.
 */
public class GridViewAdapter extends ArrayAdapter<String> {

    private final String LOG_TAG = GridViewAdapter.this.getClass().toString();
    private Context mContext;
    private int layoutResourceId;
    private List<String> mGridData = new ArrayList<String>();


    public GridViewAdapter(Context mContext, int layoutResourceId, List<String> mGridData) {
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
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) row.findViewById(R.id.grid_item_image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        String url = createMovieUrl((String) mGridData.get(position));
        Log.d(LOG_TAG, "Loading from:" + url);
        Picasso.with(mContext).load(url).into(holder.imageView);
        Log.v(LOG_TAG, "Done from:" + url);
        return row;
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
