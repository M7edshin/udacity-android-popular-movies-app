package m7edshin.popularmovieapp.Models;

/**
 * Created by Mohamed Shahin on 02/03/2018.
 * Movie Review Class for each movie
 */

public class MovieReview{

    private String author;
    private String review;

    public MovieReview(String author, String review) {
        this.author = author;
        this.review = review;
    }

    public String getAuthor() {
        return author;
    }

    public String getReview() {
        return review;
    }

}
