package com.example.cinelist;

import static org.junit.Assert.*;

import com.example.cinelist.data.Movie;

import org.junit.Test;

public class MovieUnitTest {

    @Test
    public void testMovieCreation() {
        Movie movie = new Movie(
                "Inception",
                "Sci-Fi",
                2010,
                8.8,
                true,
                "Mind-bending dream heist thriller."
        );

        assertEquals("Inception", movie.getTitle());
        assertEquals("Sci-Fi", movie.getGenre());
        assertEquals(2010, movie.getReleaseYear());
        assertEquals(8.8, movie.getRating(), 0.001);
        assertTrue(movie.isWatched());
        assertEquals("Mind-bending dream heist thriller.", movie.getNotes());
    }

    @Test
    public void testMovieSettersAndToggleWatched() {
        Movie movie = new Movie(
                "Interstellar",
                "Sci-Fi",
                2014,
                8.7,
                false,
                "Space exploration."
        );

        assertFalse(movie.isWatched());

        // Toggle watched
        movie.setWatched(true);
        assertTrue(movie.isWatched());

        // Update details
        movie.setTitle("Interstellar (Updated)");
        movie.setGenre("Sci-Fi / Adventure");
        movie.setReleaseYear(2015);
        movie.setRating(9.0);
        movie.setNotes("Updated notes.");

        assertEquals("Interstellar (Updated)", movie.getTitle());
        assertEquals("Sci-Fi / Adventure", movie.getGenre());
        assertEquals(2015, movie.getReleaseYear());
        assertEquals(9.0, movie.getRating(), 0.001);
        assertEquals("Updated notes.", movie.getNotes());
    }

    @Test
    public void testMovieIdAssignment() {
        Movie movie = new Movie("The Dark Knight", "Action", 2008, 9.0, true, "Gotham hero");
        assertEquals(0, movie.getId());

        movie.setId(42);
        assertEquals(42, movie.getId());
    }
}
