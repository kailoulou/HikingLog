package com.example.demo;
import java.io.*;
import java.io.File;
import java.io.FileWriter;
import com.opencsv.CSVWriter;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.util.Arrays;


public class SaveAndLoad {
    private final static String[] lineBreakPlan = {"*", "*", "*", "*", "*", "*", "*"};
    private final static String[] lineBreakToDo = {"|", "|", "|", "|", "|", "|", "|"};
    public static void saveToFile(TripList listIn, PlanList planIn, ObservableList<String> todoIn, File file) {
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file);
            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(outputfile);
            //adding header to csv
            String[] header = {"Date", "Location", "Distance", "Temperature", "Notes", "Difficulty", "To Do"};
            writer.writeNext(header);

            // add hiking log data to csv
            for(int i = 0; i < listIn.getTotal(); i++){
                Trip trip = listIn.getTrip(i);
                String[] data = {trip.getDate(), trip.getLocation(), trip.getDistance(), trip.getTemp(), trip.getNote(), null, null};
                writer.writeNext(data);
            }
            writer.writeNext(lineBreakPlan);

            // add plan data to csv
            for(int i = 0; i < planIn.getTotal(); i++){
                Plan plan = planIn.getPlan(i);
                String[] data = {null, plan.getLocation(), plan.getDistance(), null, null, plan.getDifficulty(), null};
                writer.writeNext(data);
            }

            writer.writeNext(lineBreakToDo);
            // add to do data to csv
            for(int i = 0; i < todoIn.size(); i++){
                String toDo = todoIn.get(i);
                String[] data = {null, null, null, null, null, null, toDo};
                writer.writeNext(data);
            }


            // closing writer connection
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void openFile(ObservableList<Trip> logData, ObservableList<Plan> planData, TripList listIn, PlanList planIn, ObservableList<String> todo, File file){
        String FieldDelimiter = ",";

        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(file));

            String line;

            br.readLine(); // skip the header line
            boolean break1 = false;
            boolean break2 = false;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(FieldDelimiter, -1);

                for(int i = 0; i < fields.length; i++){
                    if (fields[i].length() > 0) {
                        fields[i] = fields[i].substring(1, fields[i].length() - 1);
                    }
                }
                if(Arrays.equals(fields, lineBreakPlan)){
                    break1 = true;
                }
                if(Arrays.equals(fields, lineBreakToDo)){
                    break2 = true;
                }

                if (!break1 && !break2){
                    Trip trip = new Trip(fields[0], fields[1], fields[2],
                            fields[3], fields[4]);
                    listIn.addTrip(trip);
                    logData.add(trip);
                } else if (!break2) {
                    if (!Arrays.equals(fields, lineBreakPlan)){
                        Plan plan = new Plan(fields[1], fields[2], fields[5]);
                        planIn.addPlan(plan);
                        planData.add(plan);
                    }
                }
                else{
                    if(!Arrays.equals(fields, lineBreakToDo)) {
                        todo.add(fields[6]);
                    }
                }
            }
        }catch(IOException ioe) {
                System.out.println("No records found");
            }


    }
}

