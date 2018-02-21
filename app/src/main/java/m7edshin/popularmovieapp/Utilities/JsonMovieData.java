package m7edshin.popularmovieapp.Utilities;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import m7edshin.popularmovieapp.Models.Movie;

/**
 * Created by Mohamed Shahin on 17/02/2018.
 * Json data extracting
 */

class JsonMovieData {

    private static final String LOG_TAG = JsonMovieData.class.getName();

    private static final String NO_DATA = "N/A";
    private static final String KEY_RESULTS = "results";
    private static final String KEY_TITLE = "title";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_POSTER_PATH = "poster_path";
    private static final String KEY_VOTE = "vote_average";
    private static final String KEY_OVERVIEW = "overview";

    static List<Movie> extractJsonMovieData(String json) {

        if (TextUtils.isEmpty(json)) return null;

        List<Movie> movieList = new ArrayList<>();

        String title = NO_DATA;
        String releaseDate = NO_DATA;
        String poster = NO_DATA;
        String vote = NO_DATA;
        String overview = NO_DATA;

        try {
            JSONObject base = new JSONObject(json);

            JSONArray resultsArray = base.getJSONArray(KEY_RESULTS);

            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject object = resultsArray.getJSONObject(i);

                if (object.has(KEY_TITLE)) {
                    title = object.optString(KEY_TITLE);
                }
                if (object.has(KEY_RELEASE_DATE)) {
                    releaseDate = object.optString(KEY_RELEASE_DATE);
                }
                if (object.has(KEY_POSTER_PATH)) {
                    poster = object.optString(KEY_POSTER_PATH);
                }
                if (object.has(KEY_VOTE)) {
                    vote = object.optString(KEY_VOTE);
                }
                if (object.has(KEY_OVERVIEW)) {
                    overview = object.optString(KEY_OVERVIEW);
                }

                Movie movie = new Movie(title, releaseDate, poster, vote, overview);

                movieList.add(movie);
            }

        } catch (JSONException e) {
            Log.v(LOG_TAG, "Error: Problem occurred during extracting Json Movie Data");
        }

        return movieList;
    }


}
