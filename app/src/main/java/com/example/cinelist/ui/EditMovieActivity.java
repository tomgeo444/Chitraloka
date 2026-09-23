package com.example.cinelist.ui;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.cinelist.R;
import com.example.cinelist.data.Movie;
import com.example.cinelist.viewmodel.MovieViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Locale;

public class EditMovieActivity extends AppCompatActivity {

    private TextInputLayout tilEditTitle;
    private TextInputLayout tilEditGenre;
    private TextInputLayout tilEditReleaseYear;
    private TextInputLayout tilEditRating;
    private TextInputLayout tilEditNotes;

    private TextInputEditText etEditTitle;
    private TextInputEditText etEditGenre;
    private TextInputEditText etEditReleaseYear;
    private TextInputEditText etEditRating;
    private TextInputEditText etEditNotes;
    private CheckBox cbEditWatched;
    private MaterialButton btnSaveChanges;

    private MovieViewModel movieViewModel;
    private Movie currentMovie;
    private int movieId = -1;
    private boolean isInitialPopulateDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movie);

        movieId = getIntent().getIntExtra(MovieDetailsActivity.EXTRA_EDIT_MOVIE_ID, -1);
        if (movieId == -1) {
            Toast.makeText(this, "Movie not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupToolbar();
        setupViewModel();
        setupSaveButton();
    }

    private void initViews() {
        tilEditTitle = findViewById(R.id.tilEditTitle);
        tilEditGenre = findViewById(R.id.tilEditGenre);
        tilEditReleaseYear = findViewById(R.id.tilEditReleaseYear);
        tilEditRating = findViewById(R.id.tilEditRating);
        tilEditNotes = findViewById(R.id.tilEditNotes);

        etEditTitle = findViewById(R.id.etEditTitle);
        etEditGenre = findViewById(R.id.etEditGenre);
        etEditReleaseYear = findViewById(R.id.etEditReleaseYear);
        etEditRating = findViewById(R.id.etEditRating);
        etEditNotes = findViewById(R.id.etEditNotes);
        cbEditWatched = findViewById(R.id.cbEditWatched);
        btnSaveChanges = findViewById(R.id.btnSaveChanges);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbarEditMovie);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.getMovieById(movieId).observe(this, movie -> {
            if (movie != null) {
                currentMovie = movie;
                if (!isInitialPopulateDone) {
                    populateFields(movie);
                    isInitialPopulateDone = true;
                }
            }
        });
    }

    private void populateFields(Movie movie) {
        etEditTitle.setText(movie.getTitle());
        etEditGenre.setText(movie.getGenre());
        etEditReleaseYear.setText(String.valueOf(movie.getReleaseYear()));
        etEditRating.setText(String.format(Locale.US, "%.1f", movie.getRating()));
        cbEditWatched.setChecked(movie.isWatched());
        etEditNotes.setText(movie.getNotes());
    }

    private void setupSaveButton() {
        btnSaveChanges.setOnClickListener(v -> saveChanges());
    }

    private void saveChanges() {
        if (currentMovie == null) {
            return;
        }

        tilEditTitle.setError(null);
        tilEditGenre.setError(null);
        tilEditReleaseYear.setError(null);
        tilEditRating.setError(null);

        String title = etEditTitle.getText() != null ? etEditTitle.getText().toString().trim() : "";
        String genre = etEditGenre.getText() != null ? etEditGenre.getText().toString().trim() : "";
        String yearStr = etEditReleaseYear.getText() != null ? etEditReleaseYear.getText().toString().trim() : "";
        String ratingStr = etEditRating.getText() != null ? etEditRating.getText().toString().trim() : "";
        String notes = etEditNotes.getText() != null ? etEditNotes.getText().toString().trim() : "";
        boolean isWatched = cbEditWatched.isChecked();

        boolean hasError = false;

        // Validate Title
        if (title.isEmpty()) {
            tilEditTitle.setError(getString(R.string.err_title_required));
            hasError = true;
        }

        // Validate Genre
        if (genre.isEmpty()) {
            tilEditGenre.setError(getString(R.string.err_genre_required));
            hasError = true;
        }

        // Validate Year
        int releaseYear = 0;
        if (yearStr.isEmpty()) {
            tilEditReleaseYear.setError(getString(R.string.err_year_required));
            hasError = true;
        } else {
            try {
                releaseYear = Integer.parseInt(yearStr);
                if (releaseYear < 1888 || releaseYear > 2100) {
                    tilEditReleaseYear.setError(getString(R.string.err_year_invalid));
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                tilEditReleaseYear.setError(getString(R.string.err_year_invalid));
                hasError = true;
            }
        }

        // Validate Rating
        double rating = 0.0;
        if (ratingStr.isEmpty()) {
            tilEditRating.setError(getString(R.string.err_rating_required));
            hasError = true;
        } else {
            try {
                rating = Double.parseDouble(ratingStr);
                if (rating < 0.0 || rating > 10.0) {
                    tilEditRating.setError(getString(R.string.err_rating_invalid));
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                tilEditRating.setError(getString(R.string.err_rating_invalid));
                hasError = true;
            }
        }

        if (hasError) {
            return;
        }

        currentMovie.setTitle(title);
        currentMovie.setGenre(genre);
        currentMovie.setReleaseYear(releaseYear);
        currentMovie.setRating(rating);
        currentMovie.setWatched(isWatched);
        currentMovie.setNotes(notes);

        movieViewModel.update(currentMovie);

        Toast.makeText(this, R.string.toast_movie_updated, Toast.LENGTH_SHORT).show();
        finish();
    }
}
