package m7edshin.popularmovieapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import m7edshin.popularmovieapp.Utilities.MovieExtraDetailsLoader;

public class MovieDetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieExtraDetails> {

    @BindView(R.id.tv_overview) TextView tv_overview;
    @BindView(R.id.tv_release_date) TextView tv_release_date;
    @BindView(R.id.tv_vote) TextView tv_vote;
    @BindView(R.id.iv_poster) ImageView iv_poster;
    @BindView(R.id.rv_trailers) RecyclerView rv_trailers;
    @BindView(R.id.rv_reviews) RecyclerView rv_reviews;

    private String movieID;
    private String movieTitle;
    private String movieReleaseDate;
    private String movieVote;
    private String moviePoster;
    private String movieOverview;

    private static final int LOADER_ID = 1;
    private static final String MOVIE_API_APPENDED_URL = "https://api.themoviedb.org/3/movie/";
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
            movieVote = movieDetails.getVote();
            moviePoster = movieDetails.getPoster();
            movieOverview = movieDetails.getOverview();
            populateMovieDetails();
            populateExtraMovieDetails();
        }

    }

    private void populateMovieDetails() {
        final String poster_path = "http://image.tmdb.org/t/p/w185";
        String createPosterPath = poster_path + moviePoster;

        tv_overview.setText(movieOverview);
        tv_release_date.setText(String.format("%s%s", getString(R.string.released), movieReleaseDate));
        tv_vote.setText(String.format("%s%s", getString(R.string.rating), movieVote));

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

        Uri baseUri = Uri.parse(MOVIE_API_APPENDED_URL);
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
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(LOADER_ID, null, MovieDetailsActivity.this);
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

}
