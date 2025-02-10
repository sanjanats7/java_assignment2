package org.example;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.util.stream.Collectors;

class Movie {
    private int movieId;
    private String title;
    private int releaseYear;
    private String genre;
    private double rating;
    private int duration;
    private int directorId;
    private List<Integer> actorIds;

    public Movie(int movieId, String title, int releaseYear, String genre, double rating, int duration, int directorId, List<Integer> actorIds) {
        this.movieId = movieId;
        this.title = title;
        this.releaseYear = releaseYear;
        this.genre = genre;
        this.rating = rating;
        this.duration = duration;
        this.directorId = directorId;
        this.actorIds = actorIds;
    }
    public int getDirectorId() {
        return directorId;
    }



    public static HashMap<Integer, Movie> readMoviesCsv(String fileName) {
        HashMap<Integer, Movie> movieMap = new HashMap<>();

        try (InputStream inputStream = Movie.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             CSVReader csvReader = new CSVReader(reader)) {

            var rows = csvReader.readAll();

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);

                if (row.length == 8) {
                    int movieId = Integer.parseInt(row[0]);
                    String title = row[1];
                    int releaseYear = Integer.parseInt(row[2]);
                    String genre = row[3];
                    double rating = Double.parseDouble(row[4]);
                    int duration = Integer.parseInt(row[5]);
                    int directorId = Integer.parseInt(row[6]);
                    String actorIdsString = row[7].replace("\"", "");
                    List<Integer> actorIds = new ArrayList<>();
                    if (!actorIdsString.isEmpty()) {
                        String[] actorIdsArray = actorIdsString.split(",");
                        for (String actorId : actorIdsArray) {
                            actorIds.add(Integer.parseInt(actorId));
                        }
                    }

                    Movie movie = new Movie(movieId, title, releaseYear, genre, rating, duration, directorId, actorIds);
                    movieMap.put(movieId, movie);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return movieMap;
    }

    public static void getMovieDetails(HashMap<Integer, Movie> movieMap) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Movie ID (or press Enter to skip): ");
        String movieIdInput = scanner.nextLine().trim();
        Integer movieId = null;

        if (!movieIdInput.isEmpty()) {
            movieId = Integer.parseInt(movieIdInput);
        }

        System.out.print("Enter Movie Title (or press Enter to skip): ");
        String title = scanner.nextLine().trim();

        if (movieId != null) {
            Movie movie = movieMap.get(movieId);
            if (movie != null) {
                System.out.println(movie);
                return;
            } else {
                System.out.println("Movie not found for ID: " + movieId);
            }
        }

        if (!title.isEmpty()) {
            for (Movie movie : movieMap.values()) {
                if (movie.title.equalsIgnoreCase(title)) {
                    System.out.println(movie);
                    return;
                }
            }
            System.out.println("Movie not found for Title: " + title);
        } else {
            System.out.println("Invalid search parameters. Provide either a movie ID or a title.");
        }
    }

    public static void getTop10RatedMovies(HashMap<Integer, Movie> movieMap) {
        movieMap.values().stream()
                .sorted(Comparator.comparingDouble((Movie m) -> m.rating).reversed())
                .limit(10)
                .forEach(System.out::println);
    }

    public static void getMoviesByGenre(HashMap<Integer, Movie> movieMap) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine().trim();

        boolean found = false;
        for (Movie movie : movieMap.values()) {
            if (movie.genre.equalsIgnoreCase(genre)) {
                System.out.println(movie);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No movies found for Genre: " + genre);
        }
    }

    public static void getMovieByReleaseYearRange(HashMap<Integer, Movie> movieMap) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Year Range (e.g. 2000-2020): ");
        String yearRange = scanner.nextLine().trim();

        try {
            String[] parts = yearRange.split("-");
            int yearStart = Integer.parseInt(parts[0].trim());
            int yearEnd = Integer.parseInt(parts[1].trim());

            System.out.println("Movies released between " + yearStart + " and " + yearEnd + ":");
            movieMap.values().stream()
                    .filter(movie -> movie.releaseYear >= yearStart && movie.releaseYear <= yearEnd)
                    .forEach(System.out::println);

        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid year range format.");
        }
    }

    public static void getMoviesByDirector(HashMap<Integer, Movie> movieMap, HashMap<Integer, Director> directorMap) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Director Name: ");
        String directorName = scanner.nextLine().trim();

        Director director = directorMap.values().stream()
                .filter(d -> d.getName().equalsIgnoreCase(directorName))
                .findFirst()
                .orElse(null);

        if (director == null) {
            System.out.println("Director not found: " + directorName);
            return;
        }

        int directorId = director.getDirectorId();

        List<Movie> moviesByDirector = movieMap.values().stream()
                .filter(movie -> movie.directorId == directorId)
                .collect(Collectors.toList());

        if (moviesByDirector.isEmpty()) {
            System.out.println("No movies found for Director: " + directorName);
        } else {
            System.out.println("Movies directed by " + directorName + ":");
            moviesByDirector.forEach(System.out::println);
        }
    }

    public static void addMovie(HashMap<Integer, Movie> movieMap) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Movie ID: ");
        int movieId = Integer.parseInt(scanner.nextLine().trim());

        if (movieMap.containsKey(movieId)) {
            System.out.println("Movie already exists : " + movieId);
            return;
        }

        System.out.print("Enter Title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter Release Year: ");
        int releaseYear = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine().trim();

        System.out.print("Enter Rating: ");
        double rating = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Enter Duration: ");
        int duration = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter Director ID: ");
        int directorId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter Actor IDs (comma-separated): ");
        String actorIdsInput = scanner.nextLine().trim();
        List<Integer> actorIds = new ArrayList<>();

        if (!actorIdsInput.isEmpty()) {
            String[] actorIdStrings = actorIdsInput.split(",");
            for (String actorIdString : actorIdStrings) {
                actorIds.add(Integer.parseInt(actorIdString.trim()));
            }
        }

        Movie newMovie = new Movie(movieId, title, releaseYear, genre, rating, duration, directorId, actorIds);
        movieMap.put(movieId, newMovie);
        System.out.println("Movie added successfully: " + newMovie);
    }

    public static void updateMovieRating(HashMap<Integer, Movie> movieMap) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Movie ID to update rating: ");
        int movieId = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Enter new Rating: ");
        double newRating = Double.parseDouble(scanner.nextLine().trim());

        Movie movie = movieMap.get(movieId);
        if (movie != null) {
            movie.rating = newRating;
            System.out.println("Updated rating: " + movie);
        } else {
            System.out.println("Movie not found : " + movieId);
        }
    }

    public static void deleteMovie(HashMap<Integer, Movie> movieMap) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Movie ID to delete: ");
        int movieId = Integer.parseInt(scanner.nextLine().trim());

        Movie removedMovie = movieMap.remove(movieId);
        if (removedMovie != null) {
            System.out.println("Movie deleted successfully: " + removedMovie);
        } else {
            System.out.println("No movie found with ID: " + movieId);
        }
    }

    public static void displayTop15MoviesByReleaseYear(HashMap<Integer, Movie> movieMap) {
        movieMap.values().stream()
                .sorted(Comparator.comparingInt(movie -> movie.releaseYear))
                .limit(15)
                .forEach(System.out::println);
    }

    public static void getTop15MoviesByReleaseYear(HashMap<Integer, Movie> movieMap) {
        List<Movie> sortedMovies = movieMap.values()
                .stream()
                .sorted(Comparator.comparingInt(m -> m.releaseYear))
                .limit(15)
                .collect(Collectors.toList());

        System.out.println("Top 15 movies sorted by release year:");
        sortedMovies.forEach(System.out::println);
    }
}
