package m7edshin.popularmovieapp.Models;

/**
 * Created by Mohamed Shahin on 17/02/2018.
 * Movie Class Data
 */

public class Movie {

    private String title;
    private String releaseDate;
    private String poster;
    private String vote;
    private String overview;


    public Movie(String title, String releaseDate, String poster, String vote, String overview) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.poster = poster;
        this.vote = vote;
        this.overview = overview;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPoster() {
        return poster;
    }

    public String getVote() {
        return vote;
    }

    public String getOverview() {
        return overview;
    }
}
