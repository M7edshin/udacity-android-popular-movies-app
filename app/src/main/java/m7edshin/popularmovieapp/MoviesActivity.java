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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import m7edshin.popularmovieapp.Models.Movie;
import m7edshin.popularmovieapp.Utilities.DataCustomArrayAdapter;
import m7edshin.popularmovieapp.Utilities.DataLoader;


public class MoviesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Movie>> {

    @BindView(R.id.grid_view_movies) GridView grid_view_movies;

    private static final String LOG_TAG = MoviesActivity.class.getName();
    private static final int LOADER_ID = 1;
    private static final String MOVIE_API_REQUEST_URL = "https://api.themoviedb.org/3/movie/";
    private static final String MOVIE_API_KEY = "-----------> ADD YOUR API KEY HERE <-----------";

    private DataCustomArrayAdapter dataCustomArrayAdapter;

    private String select = "Most Popular";

    private final String MOVIE_TITLE = "movieTitle";
    private final String MOVIE_RELEASE_DATE = "movieReleaseDate";
    private final String MOVIE_POSTER = "moviePoster";
    private final String MOVIE_VOTE = "movieVote";
    private final String MOVIE_OVERVIEW= "movieOverview";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        ButterKnife.bind(this);

        dataCustomArrayAdapter = new DataCustomArrayAdapter(this, new ArrayList<Movie>());
        grid_view_movies.setAdapter(dataCustomArrayAdapter);

        fetchMovieData();

        grid_view_movies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = dataCustomArrayAdapter.getItem(position);
                Context context = MoviesActivity.this;
                Intent movieDetailsIntent = new Intent(context, MovieDetailsActivity.class);
                movieDetailsIntent.putExtra(MOVIE_TITLE, movie.getTitle());
                movieDetailsIntent.putExtra(MOVIE_RELEASE_DATE, movie.getReleaseDate());
                movieDetailsIntent.putExtra(MOVIE_POSTER, movie.getPoster());
                movieDetailsIntent.putExtra(MOVIE_VOTE, movie.getVote());
                movieDetailsIntent.putExtra(MOVIE_OVERVIEW, movie.getOverview());
                startActivity(movieDetailsIntent);
            }
        });
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.i(LOG_TAG, "Performing: Method onCreateLoader is called");
        String query = createUrlRequest(select);
        return new DataLoader(this, query);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.i(LOG_TAG, "Performing: Method onLoadFinished is called");
        dataCustomArrayAdapter.clear();
        if (data != null && !data.isEmpty()) {
            dataCustomArrayAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        Log.i(LOG_TAG, "Performing: Method onLoaderReset is called");
        dataCustomArrayAdapter.clear();
    }

    private void fetchMovieData() {

        boolean isConnected = checkInternetConnection();

        if (isConnected) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(LOADER_ID, null, MoviesActivity.this);
        }

    }

    private boolean checkInternetConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    private String createUrlRequest(String sortBy) {
        Uri baseUri = Uri.parse(MOVIE_API_REQUEST_URL);
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
                LoaderManager loaderManager = getLoaderManager();
                loaderManager.restartLoader(LOADER_ID, null, MoviesActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }
}
