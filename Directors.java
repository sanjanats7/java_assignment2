package org.example;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

class Director {
    private int directorId;
    private String name;
    private String dateOfBirth;
    private String nationality;

    public Director(int directorId, String name, String dateOfBirth, String nationality) {
        this.directorId = directorId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
    }

    public int getDirectorId() {
        return directorId;
    }

    public String getName() {
        return name;
    }

    public static HashMap<Integer, Director> readDirectorsCsv(String fileName) {
        HashMap<Integer, Director> directorMap = new HashMap<>();

        try (InputStream inputStream = Director.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             CSVReader csvReader = new CSVReader(reader)) {

            var rows = csvReader.readAll();

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);

                if (row.length == 4) {
                    int directorId = Integer.parseInt(row[0]);
                    String name = row[1];
                    String dateOfBirth = row[2];
                    String nationality = row[3];

                    Director director = new Director(directorId, name, dateOfBirth, nationality);
                    directorMap.put(directorId, director);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return directorMap;
    }

    public static void getTop5Directors(HashMap<Integer, Director> directorMap, HashMap<Integer, Movie> movieMap) {
        Map<Integer, Long> directorMovieCount = movieMap.values()
                .stream()
                .collect(Collectors.groupingBy(m -> m.getDirectorId(), Collectors.counting()));

        directorMovieCount.entrySet()
                .stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed())
                .limit(5)
                .forEach(entry -> {
                    Director director = directorMap.get(entry.getKey());
                    if (director != null) {
                        System.out.println("Director: " + director.getName() + " (ID: " + entry.getKey() + ") - Movie Count: " + entry.getValue());
                    }
                });
    }
}
