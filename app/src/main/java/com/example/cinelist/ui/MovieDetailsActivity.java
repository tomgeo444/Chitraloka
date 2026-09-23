package com.example.cinelist.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.example.cinelist.R;
import com.example.cinelist.data.Movie;
import com.example.cinelist.viewmodel.MovieViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class MovieDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_MOVIE_ID = "com.example.cinelist.EXTRA_EDIT_MOVIE_ID";

    private MovieViewModel movieViewModel;
    private Movie currentMovie;
    private int movieId = -1;

    private TextView tvDetailTitle;
    private TextView tvDetailGenreYear;
    private TextView tvDetailRating;
    private TextView tvDetailStatus;
    private TextView tvDetailNotes;
    private MaterialButton btnToggleWatched;
    private MaterialButton btnEditMovie;
    private MaterialButton btnDeleteMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        movieId = getIntent().getIntExtra(MainActivity.EXTRA_MOVIE_ID, -1);
        if (movieId == -1) {
            Toast.makeText(this, "Movie not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupViewModel();
        setupActions();
    }

    private void initViews() {
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailGenreYear = findViewById(R.id.tvDetailGenreYear);
        tvDetailRating = findViewById(R.id.tvDetailRating);
        tvDetailStatus = findViewById(R.id.tvDetailStatus);
        tvDetailNotes = findViewById(R.id.tvDetailNotes);

        btnToggleWatched = findViewById(R.id.btnToggleWatched);
        btnEditMovie = findViewById(R.id.btnEditMovie);
        btnDeleteMovie = findViewById(R.id.btnDeleteMovie);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbarDetails);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getMovieById(movieId).observe(this, movie -> {
            if (movie != null) {
                currentMovie = movie;
                populateMovieDetails(movie);
            }
        });
    }

    private void populateMovieDetails(Movie movie) {
        tvDetailTitle.setText(movie.getTitle());
        tvDetailGenreYear.setText(String.format(Locale.getDefault(), "%s • %d", movie.getGenre(), movie.getReleaseYear()));
        tvDetailRating.setText(String.format(Locale.getDefault(), "%.1f / 10", movie.getRating()));

        if (movie.getNotes() != null && !movie.getNotes().trim().isEmpty()) {
            tvDetailNotes.setText(movie.getNotes().trim());
        } else {
            tvDetailNotes.setText("No additional notes provided for this movie.");
        }

        if (movie.isWatched()) {
            tvDetailStatus.setText(getString(R.string.status_watched));
            tvDetailStatus.setBackgroundResource(R.drawable.bg_badge_watched);
            tvDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.watched_green));

            btnToggleWatched.setText(R.string.btn_mark_unwatched);
            btnToggleWatched.setIconResource(R.drawable.ic_check);
        } else {
            tvDetailStatus.setText(getString(R.string.status_want_to_watch));
            tvDetailStatus.setBackgroundResource(R.drawable.bg_badge_want_to_watch);
            tvDetailStatus.setTextColor(ContextCompat.getColor(this, R.color.want_to_watch_amber));

            btnToggleWatched.setText(R.string.btn_mark_watched);
            btnToggleWatched.setIconResource(R.drawable.ic_check);
        }
    }

    private void setupActions() {
        // Toggle watched status
        btnToggleWatched.setOnClickListener(v -> {
            if (currentMovie != null) {
                movieViewModel.toggleWatchedStatus(currentMovie);
                Toast.makeText(MovieDetailsActivity.this, R.string.toast_status_updated, Toast.LENGTH_SHORT).show();
            }
        });

        // Edit movie
        btnEditMovie.setOnClickListener(v -> {
            if (currentMovie != null) {
                Intent intent = new Intent(MovieDetailsActivity.this, EditMovieActivity.class);
                intent.putExtra(EXTRA_EDIT_MOVIE_ID, currentMovie.getId());
                startActivity(intent);
            }
        });

        // Delete movie with confirmation
        btnDeleteMovie.setOnClickListener(v -> showDeleteConfirmationDialog());
    }

    private void showDeleteConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_delete_title)
                .setMessage(R.string.dialog_delete_message)
                .setPositiveButton(R.string.action_delete, (dialog, which) -> {
                    if (currentMovie != null) {
                        movieViewModel.delete(currentMovie);
                        Toast.makeText(MovieDetailsActivity.this, R.string.toast_movie_deleted, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton(R.string.action_cancel, null)
                .show();
    }
}
