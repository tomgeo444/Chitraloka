# CineList – Movie Watchlist Manager

**CineList** is an Android application designed for movie enthusiasts to manage their personal movie watchlist offline. Built specifically as a clean, beginner-friendly college project demonstration following Android standard best practices.

---

## Technology Stack

- **Language:** 100% Java (No Kotlin)
- **UI Framework:** Android XML Layouts & Material Design Components (No Jetpack Compose)
- **Architecture:** Model-View-ViewModel (MVVM)
- **Local Persistence:** Room Database & SQLite
- **Asynchronous Operations:** Java `ExecutorService` & AndroidX `LiveData`
- **Minimum SDK:** API 24 (Android 7.0)
- **Target / Compile SDK:** API 34 (Android 14)

---

## Key Features

1. **Movie Catalog & Watchlist:**
   - View all saved movies with title, genre, release year, star rating, and status badge.
   - Pre-populated with sample movies (`Inception`, `Interstellar`, `The Dark Knight`, `Spider-Man: Into the Spider-Verse`) upon first launch.

2. **Real-time Search:**
   - Search movies instantly by title with dynamic list filtering as you type.
   - One-tap clear search button.

3. **Category Filtering:**
   - Material choice chips to quickly filter by:
     - **All**
     - **Want to Watch**
     - **Watched**

4. **Add Movie:**
   - Add new movies with validation (title, genre, release year between 1888–2100, rating between 0.0–10.0, watched status, notes).
   - Clear error indicators on invalid inputs.

5. **Movie Details:**
   - View comprehensive movie information, star rating, status, and personal review/notes.
   - One-tap toggle between **Mark as Watched** and **Mark as Want to Watch**.
   - Edit movie details.
   - Delete movie with an `AlertDialog` confirmation prompt.

6. **Edit Movie:**
   - Edit any existing movie's title, genre, year, rating, status, or notes with immediate database synchronization.

---

## Architecture (MVVM)

```text
       UI Layer (Activities & Adapter)
           MainActivity, AddMovieActivity,
           MovieDetailsActivity, EditMovieActivity, MovieAdapter
                         │
                         ▼
                  ViewModel Layer
                  MovieViewModel (LiveData, MediatorLiveData)
                         │
                         ▼
                 Repository Layer
                  MovieRepository (ExecutorService)
                         │
                         ▼
                     DAO Layer
                  MovieDao (Room SQL Queries)
                         │
                         ▼
                Room Database Layer
                  MovieDatabase (SQLite)
```

---

## Project Structure

```text
sentinel/androidpro/
├── app/
│   ├── build.gradle
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/example/cinelist/
│       │   │   ├── data/
│       │   │   │   ├── Movie.java             # Room Entity
│       │   │   │   ├── MovieDao.java          # Room DAO
│       │   │   │   ├── MovieDatabase.java     # Room Database & Sample Data
│       │   │   │   └── MovieRepository.java   # Repository with ExecutorService
│       │   │   ├── ui/
│       │   │   │   ├── MainActivity.java          # Home Screen
│       │   │   │   ├── AddMovieActivity.java      # Add Screen
│       │   │   │   ├── MovieDetailsActivity.java  # Details Screen
│       │   │   │   ├── EditMovieActivity.java     # Edit Screen
│       │   │   │   └── MovieAdapter.java          # RecyclerView Adapter
│       │   │   └── viewmodel/
│       │   │       └── MovieViewModel.java    # MVVM ViewModel
│       │   └── res/
│       │       ├── drawable/                  # Vector icons & badges
│       │       ├── layout/
│       │       │   ├── activity_main.xml
│       │       │   ├── activity_add_movie.xml
│       │       │   ├── activity_movie_details.xml
│       │       │   ├── activity_edit_movie.xml
│       │       │   └── movie_item.xml
│       │       └── values/
│       │           ├── colors.xml
│       │           ├── strings.xml
│       │           └── themes.xml
│       └── test/
│           └── java/com/example/cinelist/
│               └── MovieUnitTest.java         # Automated Unit Tests
├── build.gradle
├── gradle.properties
├── settings.gradle
└── gradlew
```

---

## How to Build and Run

### Command Line
```bash
./gradlew assembleDebug
```
The output APK is generated at:
```text
app/build/outputs/apk/debug/app-debug.apk
```

### Run Unit Tests
```bash
./gradlew test
```

### Android Studio
1. Open Android Studio.
2. Select **File > Open** and choose the `androidpro` directory.
3. Sync Gradle and press **Run** (Shift + F10) on an emulator or connected device.
