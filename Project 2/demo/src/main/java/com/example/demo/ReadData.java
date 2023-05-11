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
        TrailNameList list = new TrailNameList();

        String FieldDelimiter = ",";
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(file));
            String line;

            br.readLine(); // skip the header line
            while ((line = br.readLine()) != null) {
                String x = "";
                String y = "";
                String[] fields = line.split(FieldDelimiter, -1);
                suggestions.add(fields[1]);

                String[] splitStrlat = fields[6].split(" ");
                x = splitStrlat[1];

                String[] splitStrlng = fields[7].split(" ");
                y = splitStrlng[2].substring(0, splitStrlng[2].length() - 2);

                list.addTrailName(new TrailName(fields[1], x, y));
            }
        }catch(IOException ioe) {
            System.out.println("No records found");
        }
        System.out.print(suggestions);
        return suggestions;
    }
}
