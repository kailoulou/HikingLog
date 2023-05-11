package com.example.demo;

import java.util.ArrayList;

public class TrailNameList {
    private static ArrayList<TrailName> trailNameList;
    /** Constructor initialises the empty trip list
     */
    public TrailNameList() {
        trailNameList = new ArrayList<>();
    }

    /** Adds a new trip to the list
     *  @param  trailIn: The trip to add
     */
    public static void addTrailName(TrailName trailIn) {
        trailNameList.add(trailIn);
    }

    /** Gets the total number of trips
     *  @return Returns the total number of trips currently in the list
     */
    public int getTotal() {
        return trailNameList.size();
    }

    public TrailName getTrailName(String trailName){
        int positionIn =  getIndex(trailName);
        if (positionIn<0 || positionIn>=getTotal()) {// check for valid position
            return null; // no object found at given position
        }
        else {
            // remove one frm logical poition to get ArrayList position
            return trailNameList.get(positionIn);
        }
    }

    /** Reads the trip at the given position in the list
     *  @param      tripIn the trip at the given logical position in the list
     *  @return     Returns the logical position of the trip in the list
     *              or null if no trip in the list
     */
    public int getIndex(String tripIn)
    {
        for(int i = 0; i < trailNameList.size(); i++){
            if (trailNameList.get(i).getLocation().equalsIgnoreCase(tripIn)){
                return i;
            }
        }
        return -1;
    }
}
