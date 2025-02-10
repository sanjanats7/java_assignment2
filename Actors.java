package org.example;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

class Actor {
    private int actorId;
    private String name;
    private String dateOfBirth;
    private String nationality;

    public Actor(int actorId, String name, String dateOfBirth, String nationality) {
        this.actorId = actorId;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
    }



    public static HashMap<Integer, Actor> readActorsCsv(String fileName) {
        HashMap<Integer, Actor> actorMap = new HashMap<>();

        try (InputStream inputStream = Actor.class.getClassLoader().getResourceAsStream(fileName);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
             CSVReader csvReader = new CSVReader(reader)) {

            var rows = csvReader.readAll();

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);

                if (row.length == 4) {
                    int actorId = Integer.parseInt(row[0]);
                    String name = row[1];
                    String dateOfBirth = row[2];
                    String nationality = row[3];

                    Actor actor = new Actor(actorId, name, dateOfBirth, nationality);
                    actorMap.put(actorId, actor);
                }
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }

        return actorMap;
    }


}
