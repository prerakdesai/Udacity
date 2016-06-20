package app.prerak.popularmovies.fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import app.prerak.popularmovies.R;
import app.prerak.popularmovies.adapter.ReviewsListAdapter;
import app.prerak.popularmovies.adapter.TextGridViewAdapter;
import app.prerak.popularmovies.bean.MovieDetails;
import app.prerak.popularmovies.bean.ReviewDetails;
import app.prerak.popularmovies.bean.TrailerDetails;
import app.prerak.popularmovies.providers.MoviesContract;
import app.prerak.popularmovies.task.FetchReviewTask;
import app.prerak.popularmovies.task.FetchTrailerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieDetailFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String LOG_TAG = MovieDetailFragment.class.getClass().toString();
    ImageView movieImage;
    GridView gridView;
    View rootView;

    MovieDetails movieDetails;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailFragment newInstance(String param1, String param2) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private ContentValues getMovieContent(MovieDetails movieDetails) {
        ContentValues movieContent = new ContentValues();
        if (movieDetails != null) {
            movieContent.put(MoviesContract.ID, movieDetails.getId());
            movieContent.put(MoviesContract.OVERVIEW, movieDetails.getOverview());
            movieContent.put(MoviesContract.RELEASE_DATE, movieDetails.getRelease_date());
            movieContent.put(MoviesContract.TITLE, movieDetails.getOriginal_title());
            movieContent.put(MoviesContract.VOTE, movieDetails.getVote_average());
            movieContent.put(MoviesContract.POSTER_PATH, movieDetails.getPoster_path());
            movieImage = (ImageView) rootView.findViewById(R.id.movie_imageView);
            if (movieImage.getDrawable() != null) {
                Bitmap bitmap = ((BitmapDrawable) movieImage.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitmapdata = stream.toByteArray();
                movieContent.put(MoviesContract.POSTER, bitmapdata);
            }
        }
        return movieContent;
    }

    private boolean isMovieAlreadyStored(ContentValues movieContent) {
        Cursor cursor = getContext().getContentResolver()
                .query(MoviesContract.CONTENT_URI, new String[]{MoviesContract.TITLE},
                        MoviesContract.ID + "=?", new String[]{movieContent.getAsString(MoviesContract.ID)}, null);
        return cursor.getCount() == 1;
    }

    public void markAsFavourite(MovieDetails movieDetails) {

        ContentValues movieContent = getMovieContent(movieDetails);

        Log.d(LOG_TAG, "Saving to database");
        if (!isMovieAlreadyStored(movieContent)) {
            Uri uri = getContext().getContentResolver()
                    .insert(MoviesContract.CONTENT_URI, movieContent);
            Button favourite = (Button) rootView.findViewById(R.id.button_favourite);
            favourite.setBackgroundColor(Color.BLUE);
        } else {
            getContext().getContentResolver()
                    .delete(MoviesContract.CONTENT_URI, MoviesContract.ID + "=?",
                            new String[]{movieContent.getAsString(MoviesContract.ID)});
            Button favourite = (Button) rootView.findViewById(R.id.button_favourite);
            favourite.setBackgroundColor(Color.GRAY);
        }
        Cursor cursor = getContext().getContentResolver()
                .query(MoviesContract.CONTENT_URI, new String[]{MoviesContract.TITLE}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Log.d(LOG_TAG, "Stored : " + cursor
                        .getString(cursor.getColumnIndex(MoviesContract.TITLE)));
            } while (cursor.moveToNext());
        }

        cursor.close();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);


        Bundle arguments = getArguments();
        movieDetails = (MovieDetails) arguments.getSerializable("movie");
        Button favourite = (Button) rootView.findViewById(R.id.button_favourite);
        TextView trailer_title = ((TextView) rootView.findViewById(R.id.trailers_title_textView));
        if (movieDetails != null) {
            if (isMovieAlreadyStored(getMovieContent(movieDetails))) {
                movieDetails.setFavourite(true);
                favourite.setBackgroundColor(Color.BLUE);
            } else {
                movieDetails.setFavourite(false);
                favourite.setBackgroundColor(Color.GRAY);

            }
        }


        if (movieDetails != null) {

            favourite.setVisibility(View.VISIBLE);
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    markAsFavourite(movieDetails);

                }
            });

            // MovieDetails movieDetails = (MovieDetails) getIntent().getSerializableExtra("movie");

            trailer_title.setVisibility(View.VISIBLE);

            TextView title = ((TextView) rootView.findViewById(R.id.title_textView));
            title.setText(movieDetails.getOriginal_title());

            TextView rating = ((TextView) rootView.findViewById(R.id.rating_textView));
            rating.setText("(" + movieDetails.getVote_average() + ")");


            TextView release = ((TextView) rootView.findViewById(R.id.release_textView));
            release.setText("Released on : " + movieDetails.getRelease_date());

            TextView overview = ((TextView) rootView.findViewById(R.id.overview_textView));
            overview.setText(movieDetails.getOverview());

            movieImage = (ImageView) rootView.findViewById(R.id.movie_imageView);

            if (isOnline()) {
                String url = createMovieUrl(movieDetails.getPoster_path());
                Log.d(LOG_TAG, "Loading from:" + url);

                Picasso.with(getContext()).load(url).into(movieImage);
                Log.v(LOG_TAG, "Done from:" + url);

                getTrailers(movieDetails.getId());
                getReviews(movieDetails.getId());
            } else {
                byte[] bitmapString = movieDetails.getPosterImage();
                Bitmap bitmap = BitmapFactory
                        .decodeByteArray(bitmapString, 0, bitmapString.length);
                movieImage.setImageBitmap(bitmap);

            }
        } else {

            favourite.setVisibility(View.GONE);
            trailer_title.setVisibility(View.GONE);
        }


        // Inflate the layout for this fragment
        return rootView;
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public void updateTrailers(final List<TrailerDetails> trailerDetails) {
        final List<String> trailers = new ArrayList<String>();
        for (TrailerDetails trailerDetail : trailerDetails) {
            trailers.add(trailerDetail.getName());
        }


        TextGridViewAdapter textGridViewAdapter = new TextGridViewAdapter(getContext(),
                R.layout.text_grid_item, trailers);
        gridView = (GridView) rootView.findViewById(R.id.trailerGridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse("http://www.youtube.com/watch?v="
                                + trailerDetails.get(position).getKey())));
                Log.i(LOG_TAG, "Video Playing....");
            }
        });
        gridView.setAdapter(textGridViewAdapter);

    }


    private void getTrailers(String id) {
        FetchTrailerTask fetchTrailerTask = new FetchTrailerTask(this);
        fetchTrailerTask.execute(id, "videos");
    }

    private void getReviews(String id) {
        FetchReviewTask fetchReviewTask = new FetchReviewTask(this);
        fetchReviewTask.execute(id, "reviews");
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

    public void updateReview(final List<ReviewDetails> reviewDetails) {
        ListView listViewReviews = (ListView) rootView.findViewById(R.id.listView_reviews);

        ReviewsListAdapter reviewsListAdapter = new ReviewsListAdapter(getContext(),
                R.layout.list_item_review, reviewDetails);

        listViewReviews.setAdapter(reviewsListAdapter);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void markAsFavourite(MovieDetails movieDetails);
    }


}
