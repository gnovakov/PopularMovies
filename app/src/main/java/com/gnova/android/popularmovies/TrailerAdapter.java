package com.gnova.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context ctx;
    private JSONArray trailerArray;

    private OnTrailerClickListener mOnTrailerClickListener;

    public interface OnTrailerClickListener {
        void onTrailerClick(int clickTrailerPosition);
    }

    public TrailerAdapter(Context ct, JSONArray resultsArray, OnTrailerClickListener listener) {
        ctx = ct;
        trailerArray = resultsArray;
        mOnTrailerClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View trailerView = LayoutInflater.from(ctx)
                .inflate(R.layout.trailer_list_item, viewGroup, false);
        return new ViewHolder(trailerView);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.trailerNumber.setText("Trailer " + (viewHolder.getAdapterPosition() + 1));

    }

    @Override
    public int getItemCount() {

        return trailerArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView trailerNumber;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            trailerNumber = itemView.findViewById(R.id.text_view_trailer);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mOnTrailerClickListener.onTrailerClick(getAdapterPosition());
        }
    }

}
