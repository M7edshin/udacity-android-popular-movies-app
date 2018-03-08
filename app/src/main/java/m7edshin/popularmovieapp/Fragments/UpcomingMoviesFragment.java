package m7edshin.popularmovieapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import m7edshin.popularmovieapp.BuildConfig;
import m7edshin.popularmovieapp.CustomAdapters.MoviesRecyclerAdapter;
import m7edshin.popularmovieapp.InterfaceUtilities.ColumnsFitting;
import m7edshin.popularmovieapp.InterfaceUtilities.RecyclerViewTouchListener;
import m7edshin.popularmovieapp.Models.MovieDetails;
import m7edshin.popularmovieapp.MovieDetailsActivity;
import m7edshin.popularmovieapp.R;
import m7edshin.popularmovieapp.Utilities.MovieDetailsLoader;

import static m7edshin.popularmovieapp.Utilities.Constants.LOADER_MANAGER_ID;
import static m7edshin.popularmovieapp.Utilities.Constants.MOVIE_API_URL;
import static m7edshin.popularmovieapp.Utilities.Constants.QUERY_STRING_REGION;

/**
 * Created by Mohamed Shahin on 08/03/2018.
 * Upcoming Movies Fragment implementation
 */

public class UpcomingMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<MovieDetails>>{

    @BindView(R.id.recycle_view_movies)
    RecyclerView recycle_view_movies;

    private static final String MOVIE_API_KEY = BuildConfig.API_KEY;

    private MoviesRecyclerAdapter moviesRecyclerAdapter;


    private final String MOVIE_INTENT_KEY = "movie";

    private List<MovieDetails> moviesList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movies, container,false);
        ButterKnife.bind(this, rootView);

        //RecyclerView setup
        int numberOfColumns = ColumnsFitting.calculateNoOfColumns(getActivity());
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), numberOfColumns);
        recycle_view_movies.setLayoutManager(layoutManager);

        fetchMovieData();

        recycle_view_movies.addOnItemTouchListener(new RecyclerViewTouchListener
                (getActivity(),recycle_view_movies, new RecyclerViewTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        MovieDetails movieDetails = moviesList.get(position);
                        Context context = getActivity();
                        Intent movieDetailsIntent = new Intent(context, MovieDetailsActivity.class);
                        movieDetailsIntent.putExtra(MOVIE_INTENT_KEY, movieDetails);
                        startActivity(movieDetailsIntent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));

        return rootView;
    }


    @Override
    public Loader<List<MovieDetails>> onCreateLoader(int id, Bundle args) {
        String select = "upcoming";
        String query = createUrlRequest(select);
        return new MovieDetailsLoader(getActivity(), query);
    }

    @Override
    public void onLoadFinished(Loader<List<MovieDetails>> loader, List<MovieDetails> data) {
        if (data != null && !data.isEmpty()) {
            moviesList = data;
            moviesRecyclerAdapter = new MoviesRecyclerAdapter(moviesList);
            recycle_view_movies.setAdapter(moviesRecyclerAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<MovieDetails>> loader) {
        moviesRecyclerAdapter = new MoviesRecyclerAdapter(new ArrayList<MovieDetails>());
    }

    private void fetchMovieData() {

        boolean isConnected = checkInternetConnection();

        if (isConnected) {
            android.support.v4.app.LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_MANAGER_ID, null, UpcomingMoviesFragment.this);
        }

    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private String createUrlRequest(String sortBy) {
        Uri baseUri = Uri.parse(MOVIE_API_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(sortBy)
                .appendQueryParameter("api_key", MOVIE_API_KEY)
                .appendQueryParameter("region", QUERY_STRING_REGION);
        return builder.build().toString();
    }
}
