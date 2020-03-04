package com.gnova.android.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context ctx;
    private JSONArray reviewArray;

    public ReviewAdapter(Context ct, JSONArray resultsArray) {
        ctx = ct;
        reviewArray = resultsArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View reviewView = LayoutInflater.from(ctx)
                .inflate(R.layout.review_list_items, viewGroup, false);
        return new ViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        try {
            viewHolder.reviewAuthor.setText(reviewArray.getJSONObject(i).getString("author"));
            viewHolder.reviewReview.setText(reviewArray.getJSONObject(i).getString("content"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {

        return reviewArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView reviewAuthor;
        TextView reviewReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reviewAuthor = itemView.findViewById(R.id.text_view_author);
            reviewReview = itemView.findViewById(R.id.text_view_review);

        }

    }
}
