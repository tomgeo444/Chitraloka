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

public class AddMovieActivity extends AppCompatActivity {

    private TextInputLayout tilTitle;
    private TextInputLayout tilGenre;
    private TextInputLayout tilReleaseYear;
    private TextInputLayout tilRating;
    private TextInputLayout tilNotes;

    private TextInputEditText etTitle;
    private TextInputEditText etGenre;
    private TextInputEditText etReleaseYear;
    private TextInputEditText etRating;
    private TextInputEditText etNotes;
    private CheckBox cbWatched;
    private MaterialButton btnSaveMovie;

    private MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_movie);

        initViews();
        setupToolbar();
        setupViewModel();
        setupSaveButton();
    }

    private void initViews() {
        tilTitle = findViewById(R.id.tilTitle);
        tilGenre = findViewById(R.id.tilGenre);
        tilReleaseYear = findViewById(R.id.tilReleaseYear);
        tilRating = findViewById(R.id.tilRating);
        tilNotes = findViewById(R.id.tilNotes);

        etTitle = findViewById(R.id.etTitle);
        etGenre = findViewById(R.id.etGenre);
        etReleaseYear = findViewById(R.id.etReleaseYear);
        etRating = findViewById(R.id.etRating);
        etNotes = findViewById(R.id.etNotes);
        cbWatched = findViewById(R.id.cbWatched);
        btnSaveMovie = findViewById(R.id.btnSaveMovie);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.toolbarAddMovie);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
    }

    private void setupSaveButton() {
        btnSaveMovie.setOnClickListener(v -> saveMovie());
    }

    private void saveMovie() {
        // Clear previous errors
        tilTitle.setError(null);
        tilGenre.setError(null);
        tilReleaseYear.setError(null);
        tilRating.setError(null);

        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String genre = etGenre.getText() != null ? etGenre.getText().toString().trim() : "";
        String yearStr = etReleaseYear.getText() != null ? etReleaseYear.getText().toString().trim() : "";
        String ratingStr = etRating.getText() != null ? etRating.getText().toString().trim() : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";
        boolean isWatched = cbWatched.isChecked();

        boolean hasError = false;

        // Validate Title
        if (title.isEmpty()) {
            tilTitle.setError(getString(R.string.err_title_required));
            hasError = true;
        }

        // Validate Genre
        if (genre.isEmpty()) {
            tilGenre.setError(getString(R.string.err_genre_required));
            hasError = true;
        }

        // Validate Year
        int releaseYear = 0;
        if (yearStr.isEmpty()) {
            tilReleaseYear.setError(getString(R.string.err_year_required));
            hasError = true;
        } else {
            try {
                releaseYear = Integer.parseInt(yearStr);
                if (releaseYear < 1888 || releaseYear > 2100) {
                    tilReleaseYear.setError(getString(R.string.err_year_invalid));
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                tilReleaseYear.setError(getString(R.string.err_year_invalid));
                hasError = true;
            }
        }

        // Validate Rating
        double rating = 0.0;
        if (ratingStr.isEmpty()) {
            tilRating.setError(getString(R.string.err_rating_required));
            hasError = true;
        } else {
            try {
                rating = Double.parseDouble(ratingStr);
                if (rating < 0.0 || rating > 10.0) {
                    tilRating.setError(getString(R.string.err_rating_invalid));
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                tilRating.setError(getString(R.string.err_rating_invalid));
                hasError = true;
            }
        }

        if (hasError) {
            return;
        }

        Movie movie = new Movie(title, genre, releaseYear, rating, isWatched, notes);
        movieViewModel.insert(movie);

        Toast.makeText(this, R.string.toast_movie_added, Toast.LENGTH_SHORT).show();
        finish();
    }
}
