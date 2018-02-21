package m7edshin.popularmovieapp.Utilities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import m7edshin.popularmovieapp.Models.Movie;
import m7edshin.popularmovieapp.R;

/**
 * Created by Mohamed Shahin on 18/02/2018.
 * Data Holder
 */

public class DataCustomArrayAdapter extends ArrayAdapter<Movie> {

    public DataCustomArrayAdapter(Context context, ArrayList<Movie> movieArrayList) {
        super(context, 0, movieArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Movie movie = getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.movie_list_item, parent, false);
            viewHolder.iv_movie_image = convertView.findViewById(R.id.iv_movie_image);

            convertView.setTag(viewHolder);
        }

        viewHolder = (ViewHolder) convertView.getTag();

        if (movie != null) {
            final String poster_path = "http://image.tmdb.org/t/p/w185";
            String createPath = poster_path + movie.getPoster();
            Picasso.with(getContext()).load(createPath).into(viewHolder.iv_movie_image);
        }

        return convertView;
    }

    private static class ViewHolder {
        ImageView iv_movie_image;
    }
}
