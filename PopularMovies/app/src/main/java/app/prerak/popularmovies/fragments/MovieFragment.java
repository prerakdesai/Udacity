package app.prerak.popularmovies.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.prerak.popularmovies.R;
import app.prerak.popularmovies.adapter.GridViewAdapter;
import app.prerak.popularmovies.bean.MovieDetails;
import app.prerak.popularmovies.providers.MoviesContract;
import app.prerak.popularmovies.task.FetchMovieTask;

//import app.prerak.popularmovies.adapter.MyMovieRecyclerViewAdapter;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MovieFragment extends Fragment {
    private final String LOG_TAG = MovieFragment.this.getClass().toString();

    GridViewAdapter gridViewAdapter;
    String sort = "popular";
    View rootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MovieFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MovieFragment newInstance(int columnCount) {
        MovieFragment fragment = new MovieFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        loadMovies();
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void loadMovies() {
        sort = getSortOption();

        if (!sort.equals("favourites")) {
            if (isOnline()) {
                FetchMovieTask fetchMovieTask = new FetchMovieTask(this);
                fetchMovieTask.execute(sort);
            } else {
                if (gridViewAdapter != null) {
                    gridViewAdapter.clear();
                }
                Toast.makeText(getContext(), "You are not online. Change to Favourites to view your favourite movies offline", Toast.LENGTH_SHORT)
                        .show();
            }
        } else {
            loadFavourites();
        }
    }

    private String getSortOption() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        return prefs.getString(getString(R.string.sort_key), "popular");
    }


    private void loadFavourites() {
        if (gridViewAdapter != null) {
            gridViewAdapter.clear();
        }

        List<MovieDetails> movieDetails = new ArrayList<>();
        Cursor cursor = getContext().getContentResolver()
                .query(MoviesContract.CONTENT_URI, new String[]{MoviesContract.POSTER,
                        MoviesContract.TITLE, MoviesContract.ID, MoviesContract.VOTE,
                        MoviesContract.OVERVIEW, MoviesContract.RELEASE_DATE, MoviesContract.POSTER_PATH}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                byte[] bitmapString = cursor
                        .getBlob(cursor.getColumnIndex(MoviesContract.POSTER));

                MovieDetails movieDetail = new MovieDetails();
                movieDetail.setId(cursor
                        .getString(cursor.getColumnIndex(MoviesContract.ID)));
                movieDetail.setOriginal_title(cursor
                        .getString(cursor.getColumnIndex(MoviesContract.TITLE)));
                movieDetail.setOverview(cursor
                        .getString(cursor.getColumnIndex(MoviesContract.OVERVIEW)));
                movieDetail.setPoster_path(cursor
                        .getString(cursor.getColumnIndex(MoviesContract.POSTER_PATH)));

                movieDetail.setPosterImage(bitmapString);
                movieDetail.setRelease_date(cursor
                        .getString(cursor.getColumnIndex(MoviesContract.RELEASE_DATE)));
                movieDetail.setVote_average(cursor
                        .getString(cursor.getColumnIndex(MoviesContract.VOTE)));
                movieDetails.add(movieDetail);
                Log.d(LOG_TAG, "Stored : " + cursor
                        .getString(cursor.getColumnIndex(MoviesContract.TITLE)));
            } while (cursor.moveToNext());
            refreshMovies(movieDetails);
        }
        cursor.close();
    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }

    public void refreshMovies(final List<MovieDetails> movieDetails) {

        gridViewAdapter = new GridViewAdapter(getContext(), R.layout.grid_item, movieDetails);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((Callback) getActivity())
                        .onItemSelected(movieDetails.get(position));
                /*Intent detailIntent = new Intent(getContext(), MovieDetailActivity.class);
                detailIntent.putExtra("movie", movieDetails.get(position));
                startActivity(detailIntent);*/
            }
        });
        gridView.setAdapter(gridViewAdapter);

    }

    public interface Callback {
        /**
         * DetailFragmentCallback for when an item has been selected.
         */
        public void onItemSelected(MovieDetails movieDetails);
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
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
    }
}
