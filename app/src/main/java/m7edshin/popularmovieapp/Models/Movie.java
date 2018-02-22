package m7edshin.popularmovieapp.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohamed Shahin on 17/02/2018.
 * Movie Class Data
 */

public class Movie implements Parcelable {

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(poster);
        dest.writeString(vote);
        dest.writeString(overview);
    }

    private Movie(Parcel in){
        this.title = in.readString();
        this.releaseDate = in.readString();
        this.poster = in.readString();
        this.vote = in.readString();
        this.overview = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
