package m7edshin.popularmovieapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import m7edshin.popularmovieapp.CustomAdapters.MoviesRecyclerAdapter;
import m7edshin.popularmovieapp.InterfaceUtilities.ColumnsFitting;
import m7edshin.popularmovieapp.InterfaceUtilities.RecyclerViewTouchListener;
import m7edshin.popularmovieapp.Models.MovieDetails;
import m7edshin.popularmovieapp.Utilities.MovieDetailsLoader;

import static m7edshin.popularmovieapp.Utilities.Constants.LOADER_MANAGER_ID;
import static m7edshin.popularmovieapp.Utilities.Constants.MOVIE_API_URL;


public class MoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<MovieDetails>> {

    @BindView(R.id.recycle_view_movies) RecyclerView recycle_view_movies;

    private static final String MOVIE_API_KEY = BuildConfig.API_KEY;

    private MoviesRecyclerAdapter moviesRecyclerAdapter;

    private String select = "Most Popular";
    private final String MOVIE_INTENT_KEY = "movie";

    private List<MovieDetails> moviesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);

        //RecyclerView setup
        int numberOfColumns = ColumnsFitting.calculateNoOfColumns(this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns);
        recycle_view_movies.setLayoutManager(layoutManager);

        fetchMovieData();

        recycle_view_movies.addOnItemTouchListener(new RecyclerViewTouchListener
                (this,recycle_view_movies, new RecyclerViewTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        MovieDetails movieDetails = moviesList.get(position);
                        Context context = MoviesActivity.this;
                        Intent movieDetailsIntent = new Intent(context, MovieDetailsActivity.class);
                        movieDetailsIntent.putExtra(MOVIE_INTENT_KEY, movieDetails);
                        startActivity(movieDetailsIntent);
                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }
                }));
    }

    @Override
    public Loader<List<MovieDetails>> onCreateLoader(int id, Bundle args) {
        String query = createUrlRequest(select);
        return new MovieDetailsLoader(this, query);
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
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_MANAGER_ID, null, MoviesActivity.this);
        }

    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private String createUrlRequest(String sortBy) {
        Uri baseUri = Uri.parse(MOVIE_API_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(sortBy)
                .appendQueryParameter("api_key", MOVIE_API_KEY);
        return builder.build().toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sorting_menu, menu);
        MenuItem item = menu.findItem(R.id.spinner_sorting_action);
        final Spinner spinner = (Spinner) item.getActionView();
        final SpinnerAdapter spinnerAdapter =
                ArrayAdapter.createFromResource(this,
                        R.array.sort_by, android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                select = (String) spinnerAdapter.getItem(position);
                if (select.equalsIgnoreCase("Most Popular")) select = "popular";
                if (select.equalsIgnoreCase("Highest Rated")) select = "top_rated";
                if(select.equalsIgnoreCase("Favorite Movies")){
                    startActivity(new Intent(getApplicationContext(), FavoriteMoviesActivity.class));
                }
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(LOADER_MANAGER_ID, null, MoviesActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }
}
