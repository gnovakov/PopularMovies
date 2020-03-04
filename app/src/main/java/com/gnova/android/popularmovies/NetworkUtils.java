package com.gnova.android.popularmovies;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    //BASE API URL
    private static final String BASE_MOVIE_API_URL = "https://api.themoviedb.org/3/";


    //API QUERY
    private static final String API_QUERY = "api_key";

    //API KEY
    private static final String apiKey = BuildConfig.MOVIE_DATABSE_API_KEY;


    //DISCOVER ALL MOVIES QUERY
    private static final String DISCOVER_ALL_MOVIES_QUERY = "discover/movie?";
    //Language
    private static final String LANGUAGE = "language";
    //Sort By
    private static final String SORT_BY = "sort_by";
    //Include Adult
    private static final String INC_ADULT = "include_adult";
    //Include Video
    private static final String INC_VIDEO = "include_video";
    //page of Results
    private static final String PAGE = "page";


    //TRAILERS & REVIEWS QUERY 1
    private static final String TRAILERS_AND_REVIEWS_QUERY_1 = "movie/";

    //TRAILERS QUERY 2
    private static final String TRAILERS_QUERY_2 = "/videos?";

    //REVIEWS QUERY 2
    private static final String REVIEWS_QUERY_2 = "/reviews?";


    public static URL buildUrl(String sortQuery) {
        Uri builtUri = Uri.parse(BASE_MOVIE_API_URL + DISCOVER_ALL_MOVIES_QUERY).buildUpon()
                .appendQueryParameter(API_QUERY, apiKey)
                .appendQueryParameter(LANGUAGE, "en")
                .appendQueryParameter(SORT_BY, sortQuery)
                .appendQueryParameter(INC_ADULT, "false")
                .appendQueryParameter(INC_VIDEO, "false")
                .appendQueryParameter(PAGE, "1")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(LOG_TAG, "Built URI " + url);

        return url;
    }


    public static URL buildUrlTrailers(String sortQuery) {
        Uri builtUri = Uri.parse(BASE_MOVIE_API_URL + TRAILERS_AND_REVIEWS_QUERY_1 + sortQuery + TRAILERS_QUERY_2).buildUpon()
                .appendQueryParameter(API_QUERY, apiKey)
                .appendQueryParameter(LANGUAGE, "en")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(LOG_TAG, "Built URI Trailers " + url);

        return url;
    }

    public static URL buildUrlReviews(String sortQuery) {
        Uri builtUri = Uri.parse(BASE_MOVIE_API_URL + TRAILERS_AND_REVIEWS_QUERY_1 + sortQuery + REVIEWS_QUERY_2).buildUpon()
                .appendQueryParameter(API_QUERY, apiKey)
                .appendQueryParameter(LANGUAGE, "en")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.i(LOG_TAG, "Built URI Reviews " + url);

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

}

/*
// MOVIE INFO



    //Original Title - Title
    public String MOVIE_ORIGINAL_TITLE = "original_title";

    //Overview - Synopsis
    public String MOVIE_OVERVIEW = "overview";

    //Vote Average - User Rating
    public String MOVIE_VOTE_AVERAGE = "vote_average";

    //Release Date
    public String MOVIE_RELEASE_DATE = "release_date";

    */