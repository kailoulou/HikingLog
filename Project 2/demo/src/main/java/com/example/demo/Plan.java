package com.example.demo;
/** Class used to record the details of a planned hiking trip
*/
public class Plan {
    private String location;
    private String distance;
    private String difficulty;

    /** Constructor initialises the details of the trip
  	*  @param  locationIn: location of the trip/name of the trail
    *  @param  distanceIn: distance (in miles) hiked
    *  @param difficultyIn: difficulty of the hike
	*/
	public Plan(String locationIn, String distanceIn, String difficultyIn) {
            location = locationIn;
            distance = distanceIn;
            difficulty = difficultyIn;
	}


/** Reads the location of the trip
       *  @return Returns the location of the trip
    */
    public String getLocation(){
        return location;
    }

    public void setLocation(String locationIn){
        location = locationIn;
    }


    /** Reads the distance of the trip
       *  @return Returns the distance of the trip
    */
    public String getDistance(){
        return distance;
    }

    public void setDistance(String distanceIn){
        distance = distanceIn;
    }

    /** Reads the distance of the trip
       *  @return Returns the distance of the trip
    */
    public String getDifficulty(){
        return difficulty;
    }

    public void setDifficulty(String difficultyIn){
        difficulty = difficultyIn;
    }
}