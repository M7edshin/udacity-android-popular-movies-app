package m7edshin.popularmovieapp.CustomAdapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import m7edshin.popularmovieapp.R;
import mehdi.sakout.fancybuttons.FancyButton;

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
        holder.btn_trailer.setIconResource(R.drawable.ic_trailer);
        holder.btn_trailer.setText("Trailer " + String.valueOf(position+1));
    }

    @Override
    public int getItemCount() {
        return videosKeyList.size();
    }

    public class TrailersHolder extends RecyclerView.ViewHolder{

        private FancyButton btn_trailer ;

        public TrailersHolder(View itemView) {
            super(itemView);
            btn_trailer = itemView.findViewById(R.id.btn_trailer);
        }
    }
}
