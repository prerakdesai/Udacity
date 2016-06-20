package app.prerak.popularmovies.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import app.prerak.popularmovies.R;
import app.prerak.popularmovies.bean.ReviewDetails;

/**
 * Created by Prerak on 5/9/2016.
 */
public class ReviewsListAdapter extends ArrayAdapter<ReviewDetails> {

    private final String LOG_TAG = ReviewsListAdapter.this.getClass().toString();
    private Context mContext;
    private int layoutResourceId;
    private List<ReviewDetails> mGridData = new ArrayList<ReviewDetails>();


    public ReviewsListAdapter(Context mContext, int layoutResourceId, List<ReviewDetails> mGridData) {
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
        ReviewDetails reviewDetail = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_review, parent, false);
        }

        TextView versionNameView = (TextView) convertView.findViewById(R.id.textView_author);
        versionNameView.setText(reviewDetail.getAuthor());

      TextView versionNumberView = (TextView) convertView.findViewById(R.id.textView_review);
        versionNumberView.setText(reviewDetail.getContent());
        return convertView;
    }
}
