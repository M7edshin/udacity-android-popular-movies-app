package m7edshin.popularmovieapp;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import m7edshin.popularmovieapp.CustomAdapters.ReviewsRecyclerAdapter;
import m7edshin.popularmovieapp.CustomAdapters.TrailersRecyclerAdapter;
import m7edshin.popularmovieapp.InterfaceUtilities.RecyclerViewTouchListener;
import m7edshin.popularmovieapp.Models.MovieDetails;
import m7edshin.popularmovieapp.Models.MovieExtraDetails;
import m7edshin.popularmovieapp.Models.MovieReview;
import m7edshin.popularmovieapp.MoviesDatabase.DbSQLiteOpenHelper;
import m7edshin.popularmovieapp.Utilities.MovieExtraDetailsLoader;

import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_POSTER_PATH;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_RATING;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_RELEASE_DATE;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_SYNOPSIS;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_TITLE;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.CONTENT_URI;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.TABLE_NAME;
import static m7edshin.popularmovieapp.Utilities.Constants.LOADER_MANAGER_ID;
import static m7edshin.popularmovieapp.Utilities.Constants.MOVIE_API_URL;
import static m7edshin.popularmovieapp.Utilities.Constants.POSTER_PATH;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieExtraDetails> {

    @BindView(R.id.tv_synopsis) TextView tv_synopsis;
    @BindView(R.id.tv_release_date) TextView tv_release_date;
    @BindView(R.id.tv_vote) TextView tv_vote;
    @BindView(R.id.iv_poster) ImageView iv_poster;
    @BindView(R.id.rv_trailers) RecyclerView rv_trailers;
    @BindView(R.id.rv_reviews) RecyclerView rv_reviews;
    @BindView(R.id.iv_favorite) ImageView iv_favorite;
    @BindView(R.id.iv_share) ImageView iv_share;

    private String movieID;
    private String movieTitle;
    private String movieReleaseDate;
    private String movieRating;
    private String moviePoster;
    private String movieSynopsis;

    private static final String MOVIE_API_KEY = BuildConfig.API_KEY;

    private List<String> videoKeyList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        RecyclerView.LayoutManager trailersLayoutManager = new LinearLayoutManager(this);
        rv_trailers.setLayoutManager(trailersLayoutManager);

        RecyclerView.LayoutManager reviewsLayoutManager = new LinearLayoutManager(this);
        rv_reviews.setLayoutManager(reviewsLayoutManager);

        //Getting the data passed from the MoviesActivity
        Intent intentThatStartedThisActivity = getIntent();
        String MOVIE_INTENT_KEY = "movie";
        if (intentThatStartedThisActivity.hasExtra(MOVIE_INTENT_KEY)) {
            MovieDetails movieDetails = intentThatStartedThisActivity.getParcelableExtra(MOVIE_INTENT_KEY);
            movieID = movieDetails.getId();
            movieTitle = movieDetails.getTitle();
            movieReleaseDate = movieDetails.getReleaseDate();
            movieRating = movieDetails.getVote();
            moviePoster = movieDetails.getPoster();
            movieSynopsis = movieDetails.getSynopsis();
            populateMovieDetails();
            populateExtraMovieDetails();
        }

        iv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMovieDetails();
            }
        });

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = videoKeyList.get(0);
                if(!key.isEmpty()){
                    String shareBody = "http://www.youtube.com/watch?v=" + videoKeyList.get(0);
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Youtube");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    startActivity(Intent.createChooser(sharingIntent, "Enjoy"));
                }else{
                    Toast.makeText(getApplicationContext(), "No trailer available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void populateMovieDetails() {
        String createPosterPath = POSTER_PATH + moviePoster;

        tv_synopsis.setText(movieSynopsis);
        tv_release_date.setText(String.format("%s%s", getString(R.string.released), movieReleaseDate));
        tv_vote.setText(String.format("%s%s", getString(R.string.rating), movieRating));

        Picasso.with(this).load(createPosterPath).into(iv_poster);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(movieTitle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home) NavUtils.navigateUpFromSameTask(this);
        return super.onOptionsItemSelected(item);
    }

    private String createAPIUrl(String movieID){

        Uri baseUri = Uri.parse(MOVIE_API_URL);
        Uri.Builder builder = baseUri.buildUpon();
        builder.appendPath(movieID)
                .appendQueryParameter("api_key", MOVIE_API_KEY)
                .appendQueryParameter("append_to_response", "videos,reviews");
        return builder.build().toString();
    }

    @Override
    public Loader<MovieExtraDetails> onCreateLoader(int id, Bundle args) {
        String query = createAPIUrl(movieID);
        return new MovieExtraDetailsLoader(this, query);
    }

    @Override
    public void onLoadFinished(Loader<MovieExtraDetails> loader, MovieExtraDetails data) {
        List<MovieReview> reviewsList = new ArrayList<>(data.getReviewsList());
        ReviewsRecyclerAdapter reviewsRecyclerAdapter = new ReviewsRecyclerAdapter(reviewsList);
        rv_reviews.setAdapter(reviewsRecyclerAdapter);

        videoKeyList = new ArrayList<>(data.getVideosList());
        TrailersRecyclerAdapter trailersRecyclerAdapter = new TrailersRecyclerAdapter(videoKeyList);
        rv_trailers.setAdapter(trailersRecyclerAdapter);
        rv_trailers.addOnItemTouchListener(new RecyclerViewTouchListener(this, rv_trailers,
                new RecyclerViewTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                String videoKey = videoKeyList.get(position);
                playTrailer(MovieDetailsActivity.this, videoKey);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

    }

    private void populateExtraMovieDetails(){
        LoaderManager loaderManager = this.getLoaderManager();
        loaderManager.initLoader(LOADER_MANAGER_ID, null, MovieDetailsActivity.this);
    }

    @Override
    public void onLoaderReset(Loader<MovieExtraDetails> loader) {
        //Nothing
    }

    private void playTrailer(Context context, String key){
        Intent openAppIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
        Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + key));
        try {
            context.startActivity(openAppIntent);
        } catch (Exception ex) {
            context.startActivity(openBrowserIntent);
        }
    }

    private void saveMovieDetails(){

        DbSQLiteOpenHelper dbSQLiteOpenHelper = new DbSQLiteOpenHelper(this);
        SQLiteDatabase database = dbSQLiteOpenHelper.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + "Title = '" + movieTitle + "'";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.getCount() > 0){
            cursor.close();
            Toast.makeText(getApplicationContext(), "Movie is already in your Fav", Toast.LENGTH_SHORT).show();
        }else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_TITLE, movieTitle);
            values.put(COLUMN_SYNOPSIS, movieSynopsis);
            values.put(COLUMN_RATING, movieRating);
            values.put(COLUMN_RELEASE_DATE, movieReleaseDate);
            values.put(COLUMN_POSTER_PATH, moviePoster);

            Uri newUri = getContentResolver().insert(CONTENT_URI, values);

            if(newUri == null){
                Toast.makeText(this, "Error occurred to save this Movie", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Movie has been saved", Toast.LENGTH_SHORT).show();
            }

            cursor.close();
        }

    }

}
