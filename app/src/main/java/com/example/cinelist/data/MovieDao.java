package com.example.cinelist.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert
    long insert(Movie movie);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);

    @Query("SELECT * FROM movies ORDER BY id DESC")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM movies WHERE watched = :watched ORDER BY id DESC")
    LiveData<List<Movie>> getMoviesByWatchedStatus(boolean watched);

    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%' ORDER BY id DESC")
    LiveData<List<Movie>> searchMovies(String query);

    @Query("SELECT * FROM movies WHERE watched = :watched AND title LIKE '%' || :query || '%' ORDER BY id DESC")
    LiveData<List<Movie>> searchAndFilterMovies(boolean watched, String query);

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    LiveData<Movie> getMovieById(int id);

    @Query("SELECT * FROM movies WHERE id = :id LIMIT 1")
    Movie getMovieByIdSync(int id);

    @Query("SELECT COUNT(*) FROM movies")
    int getMovieCountSync();
}
