package com.gnova.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gnova.android.popularmovies.database.FavouritesDatabase;
import com.gnova.android.popularmovies.database.MovieIdEntry;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MovieInfoActivity extends AppCompatActivity implements TrailerAdapter.OnTrailerClickListener {

    //Movie Image URL
    private static final String MOVIE_IMAGE_URL = "https://image.tmdb.org/t/p/w185";

    //Youtube Base URL
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mMovieSynopsis;
    private TextView mMovieUSerRating;
    private TextView mMovieReleaseDate;
    private String mMovieId;

    private Button mFavouriteButton;

    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewRecyclerView;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;

    private JSONArray mTrailers;
    private JSONArray mReviews;

    // Member variable for the Database
    private FavouritesDatabase mDb;



    private static final String TAG = "MovieInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        mDb = FavouritesDatabase.getInstance(getApplicationContext());

        mMoviePoster = findViewById(R.id.movieImage);
        mMovieTitle = findViewById(R.id.movieTitle);
        mMovieSynopsis = findViewById(R.id.synopsis);
        mMovieUSerRating = findViewById(R.id.rating);
        mMovieReleaseDate = findViewById(R.id.releaseDate);

        mTrailerRecyclerView = findViewById(R.id.rv_trailers);
        mTrailerRecyclerView.setLayoutManager(new LinearLayoutManager(MovieInfoActivity.this));
        mTrailerRecyclerView.setHasFixedSize(true);

        mReviewRecyclerView = findViewById(R.id.rv_reviews);
        mReviewRecyclerView.setLayoutManager(new LinearLayoutManager(MovieInfoActivity.this));
        mReviewRecyclerView.setHasFixedSize(true);




        Intent movieIntent = getIntent();

        if (movieIntent.hasExtra("moviePoster")){

            String moviePoster = MOVIE_IMAGE_URL + movieIntent.getStringExtra("moviePoster");
            String movieTitle = movieIntent.getStringExtra("movieTitle");
            String movieSynopsis = movieIntent.getStringExtra("movieSynopsis") + "...";
            String movieUserRating = movieIntent.getStringExtra("movieUserRating");
            String movieReleaseDate = movieIntent.getStringExtra("movieReleaseDate");
            String movieId = movieIntent.getStringExtra("movieId");

            Log.d(TAG, "onCreate: " + moviePoster.toString());

            Picasso.get().load(moviePoster).into(mMoviePoster);

            mMovieTitle.setText(movieTitle);
            mMovieSynopsis.setText(movieSynopsis);
            mMovieUSerRating.setText(movieUserRating);
            mMovieReleaseDate.setText(movieReleaseDate);

            mMovieSynopsis.setMovementMethod(new ScrollingMovementMethod());

            mMovieId = movieId;

            mFavouriteButton = findViewById(R.id.favouritesButton);
            mFavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onSaveFavouriteButtonClicked();
                }
            });


        }

        makeSortQuery(mMovieId);

    }

    //onSaveFavouriteButtonClicked is called when the "save" button is clicked.
    public void onSaveFavouriteButtonClicked() {
        String id = mMovieId;

        //Log.d("WWWWWWWWW", "onMovieClick: clicked " + id + " " + mMovieId);

        MovieIdEntry movieIdEntry = new MovieIdEntry(id);

        mDb.movieIdDao().insertMovid(movieIdEntry);
        //finish();

        Log.d("WWWWWWWWW", "onMovieClick: clicked " + movieIdEntry.getMovId());

    }


    @Override
    public void onTrailerClick(int clickTrailerPosition) {

        String trailerPath = null;


        try {
            trailerPath = YOUTUBE_BASE_URL + mTrailers.getJSONObject(clickTrailerPosition).getString("key");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerPath)); startActivity(intent);


        //Log.d("WWWWWWWWW", "onMovieClick: clicked " + trailerPath);

    }

    // Query Method
    void makeSortQuery(String query) {
        String sortQuery = query;
        URL trailerSortUrl = NetworkUtils.buildUrlTrailers(sortQuery);
        URL reviewSortUrl = NetworkUtils.buildUrlReviews(sortQuery);

        //Connect to the API using the URL
        new MovieInfoActivity.GetTrailersTask().execute(trailerSortUrl);
        new MovieInfoActivity.GetReviewsTask().execute(reviewSortUrl);
    }


    // AsyncTask Trailers
    public class GetTrailersTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL myUrl = urls[0];
            String sortResults = null;
            try {
                sortResults = NetworkUtils.getResponseFromHttpUrl(myUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("WWWWWWWWW", "onMovieClick: clicked " + sortResults);
            // Data Returned from API as a String
            return sortResults;
        }

        @Override
        protected void onPostExecute(String sortResults) {
            try {
                JSONObject jsonObject = new JSONObject(sortResults);
                JSONArray resultsArray = jsonObject.getJSONArray("results");

                mTrailers = resultsArray;


                mTrailerAdapter = new TrailerAdapter(MovieInfoActivity.this, mTrailers, MovieInfoActivity.this);


                mTrailerRecyclerView.setAdapter(mTrailerAdapter);

                //Log.i("WWWWWWWWWWWWWWWWWWWWWW", "WWWWWWWWWWWWWWWWWWWWWW " + mTrailers);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    // AsyncTask Reviews
    public class GetReviewsTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL myUrl = urls[0];
            String sortResults = null;
            try {
                sortResults = NetworkUtils.getResponseFromHttpUrl(myUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("WWWWWWWWW", "onMovieClick: clicked " + sortResults);
            // Data Returned from API as a String
            return sortResults;
        }

        @Override
        protected void onPostExecute(String sortResults) {
            try {
                JSONObject jsonObject = new JSONObject(sortResults);
                JSONArray resultsArray = jsonObject.getJSONArray("results");

                mReviews = resultsArray;


                mReviewAdapter = new ReviewAdapter(MovieInfoActivity.this, mReviews);


                mReviewRecyclerView.setAdapter(mReviewAdapter);

                //Log.i("WWWWWWWWWWWWWWWWWWWWWW", "WWWWWWWWWWWWWWWWWWWWWW " + mReviews);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


}
