package com.example.demo;

import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadData {
    public static ObservableList<String> suggestionsArray(ObservableList<String> suggestions, String pathToFile){
        File file = new File(pathToFile);

        String FieldDelimiter = ",";

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));

            String line;

            br.readLine(); // skip the header line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(FieldDelimiter, -1);
                suggestions.add(fields[1]);
            }
        }catch(IOException ioe) {
            System.out.println("No records found");
        }
        System.out.print(suggestions);
        return suggestions;
    }
}
