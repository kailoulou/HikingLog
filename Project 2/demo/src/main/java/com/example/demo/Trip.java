package com.example.demo;
/** Class used to record the details of a logged hiking trip
*/
public class Trip {
    private String date;
    private String location;
    private String distance;
    private String temp;
    private String note;

    /** Constructor initialises the details of the trip
  	*  @param  dateIn: date the trip took place
  	*  @param  locationIn: location of the trip/name of the trail
    *  @param  distanceIn: distance (in miles) hiked
    *  @param  tempIn: temperature during the beginning of the hike or average temp
    *  @param  noteIn: a description of the trip
	*/
	public Trip(String dateIn, String locationIn, String distanceIn, String tempIn, String noteIn) {
            date = dateIn;
            location = locationIn;
            distance = distanceIn;
            temp = tempIn;
            note = noteIn;
	}

    /** Reads the date of the trip
       *  @return Returns the date of the trip
    */
    public String getDate(){
        return date;
    }

    public void setDate(String dateIn){
        date = dateIn;
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
    

    /** Reads the temperature of the trip
       *  @return Returns the temperature of the trip
    */
    public String getTemp(){
        return temp;
    }

    public void setTemp(String tempIn){
        temp = tempIn;
    }


    /** Reads the note from the trip
       *  @return Returns the note from the trip
    */
    public String getNote(){
        return note;
    }

    public void setNote(String noteIn){
        note = noteIn;
    }

    @Override
    public String toString() {
        return date + ", " + location + ", " + distance + ", " + temp + ", " + note;
    }

}