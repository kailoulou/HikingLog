package com.example.demo;
import java.util.ArrayList;

public class TripList extends ArrayList{
    private ArrayList<Trip> tripList;

    /** Constructor initialises the empty trip list
     */
    public TripList() {
        tripList = new ArrayList<>();
    }

    /** Adds a new trip to the list
     *  @param  tripIn: The trip to add
     */
    public void addTrip(Trip tripIn) {
        tripList.add(tripIn);
    }

    /** Removes the item
     *  @param tripIn The trip to remove
     */
    public void removeTrip(Trip tripIn) {
        Trip find = search(tripIn.getLocation()); // call search method
        tripList.remove(find); // remove trip

    }

    /** Gets the total number of trips
     *  @return Returns the total number of trips currently in the list
     */
    public int getTotal() {
        return tripList.size();
    }

    /** clears the list
     */
    public void clearList() {
        tripList.clear();
    }

    /** Searches for the trip in the list
     *  @param nameIn The trip to search for
     *  @return Returns the trip or null if it is not in the list
     */
    public Trip search(String nameIn) {
        for(Trip currentTrip: tripList) {
            // find the trip with the given location
            // case-insensitive, spelling sensitive
            if(currentTrip.getLocation().equalsIgnoreCase(nameIn)) {
                return currentTrip;
            }
        }
        return null; // no trip found in the list
    }

    /** Reads the trip at the given position in the list
     *  @param      positionIn The position of the trip in the list
     *  @return     Returns the trip at the given logical position in the list
     *              or null if no trip at that logical position
     */
    public Trip getTrip(int positionIn)
    {
        if (positionIn<0 || positionIn>=getTotal()) {// check for valid position
            return null; // no object found at given position
        }
        else {
            // remove one frm logical poition to get ArrayList position
            return tripList.get(positionIn);
        }
    }

    /** Reads the trip at the given position in the list
     *  @param      tripIn the trip at the given logical position in the list
     *  @return     Returns the logical position of the trip in the list
     *              or null if no trip in the list
     */
    public int getIndex(String tripIn)
    {
        for(int i = 0; i < tripList.size(); i++){
            if (tripList.get(i).getLocation().equalsIgnoreCase(tripIn)){
                return i;
            }
        }
        return -1;
    }

    /** Gets the trip list
     *  @return Returns array list of trips
     */
    public ArrayList<Trip> getList() {
        return tripList;
    }

    /** Calculates the total distance of the trips
     *  @return  Returns the total distance
     */
    public double calculateTotalDistance() {
        double totalDistance = 0; // initialize totalDistance
        // loop through all payments
        for (Trip i: tripList) {
            // add current payment to running total
            totalDistance = totalDistance + Double.parseDouble(i.getDistance());
        }
        return totalDistance;
    }

    @Override
    public String toString() {
        return tripList.toString();
    }
}
