package com.example.tin.popularmovies.retrofit;


import com.example.tin.popularmovies.retrofit.cast.Cast;
import com.example.tin.popularmovies.retrofit.movie.Movie;
import com.example.tin.popularmovies.retrofit.movie_detail.MovieDetail;
import com.example.tin.popularmovies.retrofit.review.Review;
import com.example.tin.popularmovies.retrofit.trailer.Trailer;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiMethods {

    /**
     * The Top Rated Feed
     * "https://api.themoviedb.org/3/movie/top_rated?api_key={{API_KEY}}&language=en-UK&page=1"
     * The Popular Feed
     * "https://api.themoviedb.org/3/movie/popular?api_key={{API_KEY}}&language=en-UK&page=1"
     * The Trailers Feed
     * "https://api.themoviedb.org/3/movie/{{MOVIE_ID}}/videos?api_key={{API_KEY}}&language=en-UK"
     * The Cast Members Feed (Credits)
     * "https://api.themoviedb.org/3/movie/339403/credits?api_key={{API_KEY}}&language=en-UK"
     * The Reviews Feed
     * "https://api.themoviedb.org/3/movie/339403/reviews?api_key={{API_KEY}}&language=en-US"
     * The GetDetail Feed
     * "https://api.themoviedb.org/3/movie/339403?api_key=41fe79dd1f576ae823dfb4939a5eaff6&language=en-UK"
     */

    @GET("3/movie/top_rated?api_key={API_KEY}&language=en-UK&page=1")
    Observable<ArrayList<Movie>> getTopRatedFilms(@Path("API_KEY") String apiKey);

    @GET("3/movie/popular?api_key={API_KEY}&language=en-UK&page=1")
    Observable<ArrayList<Movie>> getPopularFilms(@Path("API_KEY") String apiKey);

    @GET("3/movie/{MOVIE_ID}/videos?api_key={API_KEY}&language=en-UK")
    Observable<ArrayList<Trailer>> getTrailers(@Path("API_KEY") String apiKey, @Path("MOVIE_ID") String movieId);

    @GET("3/movie/{MOVIE_ID}/credits?api_key={API_KEY}&language=en-UK")
    Observable<ArrayList<Cast>> getCast(@Path("API_KEY") String apiKey, @Path("MOVIE_ID") String movieId);

    @GET("3/movie/{MOVIE_ID}/reviews?api_key={API_KEY}&language=en-US")
    Observable<ArrayList<Review>> getReviews(@Path("API_KEY") String apiKey, @Path("MOVIE_ID") String movieId);

    @GET("3/movie/{MOVIE_ID}?api_key={API_KEY}&language=en-UK")
    Observable<ArrayList<MovieDetail>> getFilmDetails(@Path("API_KEY") String apiKey, @Path("MOVIE_ID") String movieId);
}
