package com.gnova.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    //URL Variables
        //Popular Movies Option
        private static final String POPULAR_MOVIES = "popularity.desc";
        //Top Rated Movies Option
        private static final String TOP_RATED_MOVIES = "vote_count.desc";


    private RecyclerView mRecyclerView;

    private MovieAdapter mMovieAdapter;

    private JSONArray mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.rv_movies);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);



        // Runs the Query to Display Results
        makeSortQuery(POPULAR_MOVIES);


    }

    @Override
    public void onMovieClick(int clickedMoviePosition) {

        try {
            Log.d("WWWWWWWWW", "onMovieClick: clicked " + mMovie.getJSONObject(clickedMoviePosition).getString("original_title"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MovieInfoActivity.class);
        try {
            intent.putExtra("moviePoster", mMovie.getJSONObject(clickedMoviePosition).getString("poster_path"));
            intent.putExtra("movieTitle", mMovie.getJSONObject(clickedMoviePosition).getString("original_title"));
            intent.putExtra("movieSynopsis", mMovie.getJSONObject(clickedMoviePosition).getString("overview"));
            intent.putExtra("movieUserRating", mMovie.getJSONObject(clickedMoviePosition).getString("vote_average"));
            intent.putExtra("movieReleaseDate", mMovie.getJSONObject(clickedMoviePosition).getString("release_date"));
            intent.putExtra("movieId", mMovie.getJSONObject(clickedMoviePosition).getString("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        startActivity(intent);

    }


    // Query Method
    void makeSortQuery(String query) {
        String sortQuery = query;
        URL sortUrl = NetworkUtils.buildUrl(sortQuery);

        //Connect to the API using the URL
        new GetMoviesTask().execute(sortUrl);
    }



    // AsyncTask
    public class GetMoviesTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL myUrl = urls[0];
            String sortResults = null;
            try {
                sortResults = NetworkUtils.getResponseFromHttpUrl(myUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Data Returned from API as a String
            return sortResults;
        }

        @Override
        protected void onPostExecute(String sortResults) {
            try {
                JSONObject jsonObject = new JSONObject(sortResults);
                JSONArray resultsArray = jsonObject.getJSONArray("results");

                /*for ( int i = 0; i < resultsArray.length(); i++) {
                    JSONObject currentMovie = resultsArray.getJSONObject(i);
                }*/

                mMovie = resultsArray;

                mMovieAdapter = new MovieAdapter(MainActivity.this, mMovie, MainActivity.this);


                mRecyclerView.setAdapter(mMovieAdapter);

                //Log.i("WWWWWWWWWWWWWWWWWWWWWW", "WWWWWWWWWWWWWWWWWWWWWW " + mMovie);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    // Creates the Sort Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_main.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    // Sorting Method activated by Sort Menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemThatWasSelected = item.getItemId();

        switch (menuItemThatWasSelected) {
            case R.id.sort_by_popular:
                makeSortQuery(POPULAR_MOVIES);
                break;
            case R.id.sort_by__top_rated:
                makeSortQuery(TOP_RATED_MOVIES);
                break;
            default:
                makeSortQuery(POPULAR_MOVIES);
        }

        return super.onOptionsItemSelected(item);
    }
}
