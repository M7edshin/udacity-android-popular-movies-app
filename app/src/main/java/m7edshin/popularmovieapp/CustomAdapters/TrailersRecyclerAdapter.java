package m7edshin.popularmovieapp.CustomAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import m7edshin.popularmovieapp.R;

/**
 * Created by Mohamed Shahin on 02/03/2018.
 * Data holder for Trailer details
 */

public class TrailersRecyclerAdapter extends RecyclerView.Adapter<TrailersRecyclerAdapter.TrailersHolder> {

    private List<String> videosKeyList;

    public TrailersRecyclerAdapter(List<String> videosKeyList) {
        this.videosKeyList = videosKeyList;
    }

    @Override
    public TrailersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_recycler_item, parent,false);
        return new TrailersHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailersHolder holder, int position) {
        holder.iv_play.setImageResource(R.drawable.ic_launcher_background);
        holder.tv_name.setText(String.format("Trailer %s", String.valueOf(position + 1)));
    }

    @Override
    public int getItemCount() {
        return videosKeyList.size();
    }

    public class TrailersHolder extends RecyclerView.ViewHolder{

        private ImageView iv_play;
        private TextView tv_name;

        public TrailersHolder(View itemView) {
            super(itemView);
            iv_play = itemView.findViewById(R.id.iv_play);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
