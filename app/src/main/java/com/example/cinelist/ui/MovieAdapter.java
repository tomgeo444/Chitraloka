package com.example.cinelist.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinelist.R;
import com.example.cinelist.data.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    public interface OnMovieClickListener {
        void onMovieClick(Movie movie);
    }

    private List<Movie> movies = new ArrayList<>();
    private final OnMovieClickListener listener;

    public MovieAdapter(OnMovieClickListener listener) {
        this.listener = listener;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies != null ? movies : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie, listener);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvTitle;
        private final TextView tvGenreYear;
        private final TextView tvRating;
        private final TextView tvStatus;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvMovieTitle);
            tvGenreYear = itemView.findViewById(R.id.tvMovieGenreYear);
            tvRating = itemView.findViewById(R.id.tvMovieRating);
            tvStatus = itemView.findViewById(R.id.tvMovieStatus);
        }

        public void bind(final Movie movie, final OnMovieClickListener listener) {
            Context context = itemView.getContext();
            tvTitle.setText(movie.getTitle());
            tvGenreYear.setText(String.format(Locale.getDefault(), "%s • %d", movie.getGenre(), movie.getReleaseYear()));
            tvRating.setText(String.format(Locale.getDefault(), "%.1f", movie.getRating()));

            if (movie.isWatched()) {
                tvStatus.setText(context.getString(R.string.status_watched));
                tvStatus.setBackgroundResource(R.drawable.bg_badge_watched);
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.watched_green));
            } else {
                tvStatus.setText(context.getString(R.string.status_want_to_watch));
                tvStatus.setBackgroundResource(R.drawable.bg_badge_want_to_watch);
                tvStatus.setTextColor(ContextCompat.getColor(context, R.color.want_to_watch_amber));
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMovieClick(movie);
                }
            });
        }
    }
}
