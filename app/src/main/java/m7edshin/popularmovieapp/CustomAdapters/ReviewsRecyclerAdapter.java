package m7edshin.popularmovieapp.CustomAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import m7edshin.popularmovieapp.Models.MovieReview;
import m7edshin.popularmovieapp.R;

/**
 * Created by Mohamed Shahin on 02/03/2018.
 * Data holder for Movie Reviews
 */

public class ReviewsRecyclerAdapter extends RecyclerView.Adapter<ReviewsRecyclerAdapter.ReviewsHolder>{

    private List<MovieReview> reviewsList;

    public ReviewsRecyclerAdapter(List<MovieReview> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @Override
    public ReviewsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_recycler_item, parent,false);
        return new ReviewsHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewsHolder holder, int position) {
        MovieReview movieReview = reviewsList.get(position);
        holder.tv_author.setText(movieReview.getAuthor());
        holder.tv_review.setText(movieReview.getReview());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public class ReviewsHolder extends RecyclerView.ViewHolder{

        private TextView tv_author;
        private TextView tv_review;

        public ReviewsHolder(View itemView) {
            super(itemView);
            tv_author = itemView.findViewById(R.id.tv_author);
            tv_review = itemView.findViewById(R.id.tv_review);
        }
    }
}
