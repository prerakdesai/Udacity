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
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.prerak.popularmovies.R;

/**
 * Created by Prerak on 5/9/2016.
 */
public class TextGridViewAdapter extends ArrayAdapter<String> {

    private final String LOG_TAG = TextGridViewAdapter.this.getClass().toString();
    private Context mContext;
    private int layoutResourceId;
    private List<String> mGridData = new ArrayList<String>();


    public TextGridViewAdapter(Context mContext, int layoutResourceId, List<String> mGridData) {
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
            holder.textView = (TextView) row.findViewById(R.id.text_grid_item);
            holder.textView.setText(mGridData.get(position));
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        return row;
    }

    static class ViewHolder {
        TextView textView;
    }

}
