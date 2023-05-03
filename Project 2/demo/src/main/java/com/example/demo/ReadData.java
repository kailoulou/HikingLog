package com.example.demo;

import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ReadData {
    public static void suggestionsArray(ArrayList<String> suggestions, String pathToFile){
        File file = new File(pathToFile);

        String FieldDelimiter = ",";

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));

            String line;

            br.readLine(); // skip the header line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(FieldDelimiter, -1);

                /*for(int i = 0; i < fields.length; i++){
                    if (fields[i].length() > 0) {
                        fields[i] = fields[i].substring(1, fields[i].length() - 1);
                    }
                }*/
                suggestions.add(fields[1]);
            }
        }catch(IOException ioe) {
            System.out.println("No records found");
        }


    }
}
