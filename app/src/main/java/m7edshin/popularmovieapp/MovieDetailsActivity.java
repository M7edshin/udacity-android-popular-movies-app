package m7edshin.popularmovieapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_overview) TextView tv_overview;
    @BindView(R.id.tv_release_date) TextView tv_release_date;
    @BindView(R.id.tv_vote) TextView tv_vote;
    @BindView(R.id.iv_poster) ImageView iv_poster;

    private String movieTitle;
    private String movieReleaseDate;
    private String movieVote;
    private String moviePoster;
    private String movieOverview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        final String MOVIE_TITLE = "movieTitle";
        final String MOVIE_RELEASE_DATE = "movieReleaseDate";
        final String MOVIE_POSTER = "moviePoster";
        final String MOVIE_VOTE = "movieVote";
        final String MOVIE_OVERVIEW= "movieOverview";

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity.hasExtra(MOVIE_TITLE)) {
            movieTitle = intentThatStartedThisActivity.getStringExtra(MOVIE_TITLE);
            movieReleaseDate = intentThatStartedThisActivity.getStringExtra(MOVIE_RELEASE_DATE);
            movieVote = intentThatStartedThisActivity.getStringExtra(MOVIE_VOTE);
            moviePoster = intentThatStartedThisActivity.getStringExtra(MOVIE_POSTER);
            movieOverview = intentThatStartedThisActivity.getStringExtra(MOVIE_OVERVIEW);
            populateMovieDetails();
        }

    }

    private void populateMovieDetails() {
        final String poster_path = "http://image.tmdb.org/t/p/w185";
        String createPosterPath = poster_path + moviePoster;

        tv_overview.setText(movieOverview);
        tv_release_date.setText(getString(R.string.released) + movieReleaseDate);
        tv_vote.setText(getString(R.string.rating) + movieVote);

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
}
