package m7edshin.popularmovieapp.Models;

import java.util.List;

/**
 * Created by Mohamed Shahin on 26/02/2018.
 * MovieDetails Class for each movie's extra details (Trailer & Reviews)
 */

public class MovieExtraDetails {

    private List<MovieReview> reviewsList;
    private List<String> videosList;

    public MovieExtraDetails(List<MovieReview> reviewsList, List<String> videosList) {
        this.reviewsList = reviewsList;
        this.videosList = videosList;
    }

    public List<MovieReview> getReviewsList() {
        return reviewsList;
    }

    public List<String> getVideosList() {
        return videosList;
    }

}
