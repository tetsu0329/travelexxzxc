package com.travelex.asus.realtimeproject;

/**
 * Created by Asus on 7/30/2017.
 */

public class RequestTour {
    public String requestID;
    public String numjoiner;
    public String tourID;
    public String userID;

    public String getRequestID() {
        return requestID;
    }

    public String getNumjoiner (){
        return numjoiner;
    }

    public String getTourID() {
        return tourID;
    }

    public String getUserID() {
        return userID;
    }

    public RequestTour (String requestID, String numjoiner, String tourID, String userID){
        this.requestID = requestID;
        this.numjoiner = numjoiner;
        this.tourID = tourID;
        this.userID = userID;
    }
    public RequestTour (){

    }
}
