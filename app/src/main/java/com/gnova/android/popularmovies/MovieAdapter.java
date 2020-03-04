package com.gnova.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    //Movie Image URL
    private static final String MOVIE_IMAGE_URL = "https://image.tmdb.org/t/p/w185";

    private Context ctx;
    private JSONArray movieArray;

    private OnMovieClickListener mOnMovieClickListener;


    public interface OnMovieClickListener {
        void onMovieClick(int clickedMoviePosition);
    }


    public MovieAdapter(Context ct, JSONArray resultsArray, OnMovieClickListener listener) {
        ctx = ct;
        movieArray = resultsArray;
        mOnMovieClickListener = listener;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View movieView = LayoutInflater.from(ctx)
                .inflate(R.layout.movie_list_item, viewGroup, false);
        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String movieImagePath = null;

        try {
            movieImagePath = MOVIE_IMAGE_URL + movieArray.getJSONObject(i).getString("poster_path");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Picasso.get().load(movieImagePath).into(viewHolder.movieImage);

        //Log.i("EEEEEEĖEEEEEEEĖE", "EEEEEEĖEEEEEEEĖE " + moviePath);
    }

    @Override
    public int getItemCount() {

        return movieArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView movieImage;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movie_imageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            mOnMovieClickListener.onMovieClick(getAdapterPosition());
        }


    }


}
