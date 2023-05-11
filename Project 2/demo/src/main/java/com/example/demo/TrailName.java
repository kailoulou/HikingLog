package com.example.demo;

public class TrailName {
    private String location;
    private String xCord;
    private String yCord;
    public TrailName(String locIn, String xIn, String yIn){
        location = locIn;
        xCord = xIn;
        yCord = yIn;
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
    public void setxCord(String xCordIn){
        xCord = xCordIn;
    }

    public String getxCord(){
        return xCord;
    }

    public void setyCord(String yCordIn){
        yCord=yCordIn;
    }

    public String getyCord(){
        return yCord;
    }
}
