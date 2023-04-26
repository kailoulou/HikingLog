package com.example.demo;
import java.io.*;
import java.io.File;
import java.io.FileWriter;
import com.opencsv.CSVWriter;
import javafx.collections.ObservableList;
import java.io.BufferedReader;


public class SaveAndLoad {
    public static void saveToFile(TripList listIn, File file) {
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);
            //adding header to csv
            String[] header = {"Date", "Location", "Distance", "Temperature", "Notes"};
            writer.writeNext(header);

            // add data to csv
            for(int i = 0; i < listIn.getTotal(); i++){
                Trip trip = listIn.getTrip(i);
                String[] data = {trip.getDate(), trip.getLocation(), trip.getDistance(), trip.getTemp(), trip.getNote()};
                writer.writeNext(data);
            }
            // closing writer connection
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void openFile(ObservableList<Trip> logData , TripList listIn,  File file){
        String FieldDelimiter = ",";

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));

            String line;

            br.readLine(); // skip the header line
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(FieldDelimiter, -1);

                for(int i = 0; i < fields.length; i++){
                    if (fields[i].length() > 0) {
                        fields[i] = fields[i].substring(1, fields[i].length() - 1);
                    }
                }
                Trip trip = new Trip(fields[0], fields[1], fields[2],
                        fields[3], fields[4]);
                listIn.addTrip(trip);
                logData.add(trip);

            }
        }catch(IOException ioe) {
                System.out.println("No records found");
            }


    }
}

