package com.example.cinelist.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.cinelist.data.Movie;
import com.example.cinelist.data.MovieRepository;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    public enum FilterMode {
        ALL,
        WANT_TO_WATCH,
        WATCHED
    }

    private final MovieRepository repository;
    private final MutableLiveData<FilterMode> currentFilter = new MutableLiveData<>(FilterMode.ALL);
    private final MutableLiveData<String> currentSearchQuery = new MutableLiveData<>("");
    private final MediatorLiveData<List<Movie>> filteredMovies = new MediatorLiveData<>();
    private LiveData<List<Movie>> currentSourceLiveData;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        repository = new MovieRepository(application);

        // Re-evaluate movie list whenever filter or search query changes
        filteredMovies.addSource(currentFilter, filter -> updateMovieSource());
        filteredMovies.addSource(currentSearchQuery, query -> updateMovieSource());

        updateMovieSource();
    }

    private void updateMovieSource() {
        if (currentSourceLiveData != null) {
            filteredMovies.removeSource(currentSourceLiveData);
        }

        FilterMode filter = currentFilter.getValue() != null ? currentFilter.getValue() : FilterMode.ALL;
        String query = currentSearchQuery.getValue() != null ? currentSearchQuery.getValue().trim() : "";

        if (filter == FilterMode.ALL) {
            if (query.isEmpty()) {
                currentSourceLiveData = repository.getAllMovies();
            } else {
                currentSourceLiveData = repository.searchMovies(query);
            }
        } else if (filter == FilterMode.WANT_TO_WATCH) {
            if (query.isEmpty()) {
                currentSourceLiveData = repository.getMoviesByWatchedStatus(false);
            } else {
                currentSourceLiveData = repository.searchAndFilterMovies(false, query);
            }
        } else { // WATCHED
            if (query.isEmpty()) {
                currentSourceLiveData = repository.getMoviesByWatchedStatus(true);
            } else {
                currentSourceLiveData = repository.searchAndFilterMovies(true, query);
            }
        }

        filteredMovies.addSource(currentSourceLiveData, filteredMovies::setValue);
    }

    public LiveData<List<Movie>> getFilteredMovies() {
        return filteredMovies;
    }

    public LiveData<Movie> getMovieById(int id) {
        return repository.getMovieById(id);
    }

    public void setFilter(FilterMode filter) {
        if (currentFilter.getValue() != filter) {
            currentFilter.setValue(filter);
        }
    }

    public void setSearchQuery(String query) {
        currentSearchQuery.setValue(query);
    }

    public void insert(Movie movie) {
        repository.insert(movie);
    }

    public void update(Movie movie) {
        repository.update(movie);
    }

    public void delete(Movie movie) {
        repository.delete(movie);
    }

    public void toggleWatchedStatus(Movie movie) {
        movie.setWatched(!movie.isWatched());
        repository.update(movie);
    }
}
