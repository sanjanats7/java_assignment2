package org.example;

import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        HashMap<Integer, Movie> movieMap = Movie.readMoviesCsv("movies_large.csv");
        HashMap<Integer, Actor> actorMap = Actor.readActorsCsv("actors_large.csv");
        HashMap<Integer, Director> directorMap = Director.readDirectorsCsv("directors_large.csv");

        while (true) {
            System.out.println("1. Get Movie Information");
            System.out.println("2. Get Top 10 Rated Movies");
            System.out.println("3. Get Movies by Genre");
            System.out.println("4. Get Movies by Director");
            System.out.println("5. Get Movies by Release Year");
            System.out.println("6. Get Movies by release year range");
            System.out.println("7.Add a New Movie");
            System.out.println("8. Update Movie Rating");
            System.out.println("9.Sort and return 15 movies by the release year ");
            System.out.println("10 Delete a Movie");
            System.out.println("11 Get top 15 directors.");
            System.out.println("12 Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> Movie.getMovieDetails(movieMap);
                case 2 -> Movie.getTop10RatedMovies(movieMap);
                case 3 -> Movie.getMoviesByGenre(movieMap);
                case 4 -> Movie.getMoviesByDirector(movieMap, directorMap);
                case 5 -> Movie.getTop15MoviesByReleaseYear(movieMap);
                case 6 -> Movie.getMovieByReleaseYearRange(movieMap);
                case 7 -> Movie.addMovie(movieMap);
                case 8 -> Movie.updateMovieRating(movieMap);
                case 9 -> Movie.displayTop15MoviesByReleaseYear(movieMap);
                case 10 -> Movie.deleteMovie(movieMap);
                case 11 -> Director.getTop5Directors(directorMap,movieMap);
                default->
                    System.out.println("Invalid choice. Please select again.");
            }
        }

    }

    private static void exitProgram() {
        System.out.println("Exiting the program...");
        System.exit(0);
    }
}
