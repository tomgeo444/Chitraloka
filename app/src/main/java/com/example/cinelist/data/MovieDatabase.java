package com.example.cinelist.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieDatabase extends RoomDatabase {

    private static volatile MovieDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public abstract MovieDao movieDao();

    public static MovieDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    MovieDatabase.class, "cinelist_database")
                            .addCallback(roomCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // Populate database with initial sample data in the background
            databaseWriteExecutor.execute(() -> {
                MovieDao dao = INSTANCE.movieDao();
                dao.insert(new Movie(
                        "Inception",
                        "Sci-Fi",
                        2010,
                        8.8,
                        true,
                        "Mind-bending dream heist thriller directed by Christopher Nolan."
                ));
                dao.insert(new Movie(
                        "Interstellar",
                        "Sci-Fi",
                        2014,
                        8.7,
                        true,
                        "Space exploration journey to find a new home for mankind."
                ));
                dao.insert(new Movie(
                        "The Dark Knight",
                        "Action",
                        2008,
                        9.0,
                        true,
                        "Batman battles the Joker for the soul of Gotham City."
                ));
                dao.insert(new Movie(
                        "Spider-Man: Into the Spider-Verse",
                        "Animation",
                        2018,
                        8.4,
                        false,
                        "Teenager Miles Morales becomes the new Spider-Man and joins other spider heroes."
                ));
            });
        }
    };
}
