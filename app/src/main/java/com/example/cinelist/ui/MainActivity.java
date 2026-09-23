package com.example.cinelist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinelist.R;
import com.example.cinelist.data.Movie;
import com.example.cinelist.viewmodel.MovieViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class MainActivity extends AppCompatActivity implements MovieAdapter.OnMovieClickListener {

    public static final String EXTRA_MOVIE_ID = "com.example.cinelist.EXTRA_MOVIE_ID";

    private MovieViewModel movieViewModel;
    private MovieAdapter movieAdapter;

    private EditText etSearch;
    private ImageView btnClearSearch;
    private ChipGroup chipGroupFilter;
    private RecyclerView rvMovies;
    private LinearLayout layoutEmptyState;
    private ExtendedFloatingActionButton fabAddMovie;
    private MaterialButton btnAddMovieEmpty;
    private MaterialButton btnTopAddMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupViewModel();
        setupSearch();
        setupFilterChips();
        setupAddActions();
    }

    private void initViews() {
        etSearch = findViewById(R.id.etSearch);
        btnClearSearch = findViewById(R.id.btnClearSearch);
        chipGroupFilter = findViewById(R.id.chipGroupFilter);
        rvMovies = findViewById(R.id.rvMovies);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        fabAddMovie = findViewById(R.id.fabAddMovie);
        btnAddMovieEmpty = findViewById(R.id.btnAddMovieEmpty);
        btnTopAddMovie = findViewById(R.id.btnTopAddMovie);
    }

    private void setupRecyclerView() {
        movieAdapter = new MovieAdapter(this);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(movieAdapter);
    }

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getFilteredMovies().observe(this, movies -> {
            movieAdapter.setMovies(movies);
            if (movies == null || movies.isEmpty()) {
                layoutEmptyState.setVisibility(View.VISIBLE);
                rvMovies.setVisibility(View.GONE);
            } else {
                layoutEmptyState.setVisibility(View.GONE);
                rvMovies.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No-op
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s != null ? s.toString().trim() : "";
                movieViewModel.setSearchQuery(query);
                btnClearSearch.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No-op
            }
        });

        btnClearSearch.setOnClickListener(v -> etSearch.setText(""));
    }

    private void setupFilterChips() {
        chipGroupFilter.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.isEmpty()) {
                return;
            }
            int checkedId = checkedIds.get(0);
            if (checkedId == R.id.chipAll) {
                movieViewModel.setFilter(MovieViewModel.FilterMode.ALL);
            } else if (checkedId == R.id.chipWantToWatch) {
                movieViewModel.setFilter(MovieViewModel.FilterMode.WANT_TO_WATCH);
            } else if (checkedId == R.id.chipWatched) {
                movieViewModel.setFilter(MovieViewModel.FilterMode.WATCHED);
            }
        });
    }

    private void setupAddActions() {
        View.OnClickListener openAddMovieListener = v -> {
            Intent intent = new Intent(MainActivity.this, AddMovieActivity.class);
            startActivity(intent);
        };

        fabAddMovie.setOnClickListener(openAddMovieListener);
        btnTopAddMovie.setOnClickListener(openAddMovieListener);
        btnAddMovieEmpty.setOnClickListener(openAddMovieListener);
    }

    @Override
    public void onMovieClick(Movie movie) {
        Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
        intent.putExtra(EXTRA_MOVIE_ID, movie.getId());
        startActivity(intent);
    }
}
