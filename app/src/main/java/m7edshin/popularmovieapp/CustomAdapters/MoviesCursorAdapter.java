package m7edshin.popularmovieapp.CustomAdapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import m7edshin.popularmovieapp.R;

import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_POSTER_PATH;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_RATING;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_RELEASE_DATE;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_SYNOPSIS;
import static m7edshin.popularmovieapp.MoviesDatabase.DbContract.DatabaseEntry.COLUMN_TITLE;
import static m7edshin.popularmovieapp.Utilities.Constants.POSTER_PATH;

/**
 * Created by Mohamed Shahin on 03/03/2018.
 * Hold/Display data from the database
 */

public class MoviesCursorAdapter extends RecyclerView.Adapter<MoviesCursorAdapter.FavMoviesViewHolder>{

    private Cursor cursor;

    public MoviesCursorAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public MoviesCursorAdapter.FavMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_movie_recycler_item,
                parent, false);
        return new FavMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesCursorAdapter.FavMoviesViewHolder holder, int position) {
        if(cursor.moveToPosition(position)){
            int posterPathColumnIndex = cursor.getColumnIndex(COLUMN_POSTER_PATH);
            int titleColumnIndex = cursor.getColumnIndex(COLUMN_TITLE);
            int synopsisColumnIndex = cursor.getColumnIndex(COLUMN_SYNOPSIS);
            int ratingColumnIndex = cursor.getColumnIndex(COLUMN_RATING);
            int releaseDateColumnIndex = cursor.getColumnIndex(COLUMN_RELEASE_DATE);

            String posterPath = cursor.getString(posterPathColumnIndex);
            String title = cursor.getString(titleColumnIndex);
            String synopsis = cursor.getString(synopsisColumnIndex);
            String rating = cursor.getString(ratingColumnIndex);
            String releaseDate = cursor.getString(releaseDateColumnIndex);

            holder.bind(posterPath,title,synopsis,rating,releaseDate);
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public class FavMoviesViewHolder extends RecyclerView.ViewHolder{
        ImageView iv_fav_poster;
        TextView tv_fav_title;
        TextView tv_synopsis;
        TextView tv_fav_rating;
        TextView tv_fav_release_date;

        public FavMoviesViewHolder(View itemView) {
            super(itemView);
            iv_fav_poster = itemView.findViewById(R.id.iv_fav_poster);
            tv_fav_title = itemView.findViewById(R.id.tv_fav_title);
            tv_synopsis = itemView.findViewById(R.id.tv_synopsis);
            tv_fav_rating = itemView.findViewById(R.id.tv_fav_rating);
            tv_fav_release_date = itemView.findViewById(R.id.tv_fav_release_date);
        }

        public void bind(String posterPath, String title, String synopsis, String rating, String releaseDate){
            String path = POSTER_PATH + posterPath;
            Picasso.with(itemView.getContext()).load(path).into(iv_fav_poster);
            tv_fav_title.setText(title);
            tv_synopsis.setText(synopsis);
            tv_fav_rating.setText(rating);
            tv_fav_release_date.setText(releaseDate);
        }
    }

}
