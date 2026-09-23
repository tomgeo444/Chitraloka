package com.example.cinelist.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MovieRepository {

    private final MovieDao movieDao;
    private final LiveData<List<Movie>> allMovies;

    public MovieRepository(Application application) {
        MovieDatabase database = MovieDatabase.getInstance(application);
        movieDao = database.movieDao();
        allMovies = movieDao.getAllMovies();
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    public LiveData<List<Movie>> getMoviesByWatchedStatus(boolean watched) {
        return movieDao.getMoviesByWatchedStatus(watched);
    }

    public LiveData<List<Movie>> searchMovies(String query) {
        return movieDao.searchMovies(query);
    }

    public LiveData<List<Movie>> searchAndFilterMovies(boolean watched, String query) {
        return movieDao.searchAndFilterMovies(watched, query);
    }

    public LiveData<Movie> getMovieById(int id) {
        return movieDao.getMovieById(id);
    }

    public void insert(Movie movie) {
        MovieDatabase.databaseWriteExecutor.execute(() -> movieDao.insert(movie));
    }

    public void update(Movie movie) {
        MovieDatabase.databaseWriteExecutor.execute(() -> movieDao.update(movie));
    }

    public void delete(Movie movie) {
        MovieDatabase.databaseWriteExecutor.execute(() -> movieDao.delete(movie));
    }
}
