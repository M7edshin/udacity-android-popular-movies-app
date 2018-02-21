package m7edshin.popularmovieapp.Utilities;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

import m7edshin.popularmovieapp.Models.Movie;

/**
 * Created by Mohamed Shahin on 18/02/2018.
 * Load data in the background
 */

public class DataLoader extends AsyncTaskLoader<List<Movie>> {

    private static final String LOG_TAG = DataLoader.class.getName();
    private final String url;


    public DataLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "Performing: Method onStartLoading() is called");
        forceLoad();
    }

    @Override
    public List<Movie> loadInBackground() {
        Log.i(LOG_TAG, "Performing: Method loadInBackground is called");
        if (url == null) return null;
        return NetworkingRequest.fetchMovieData(url);
    }
}
