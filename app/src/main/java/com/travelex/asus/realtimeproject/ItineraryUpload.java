package com.travelex.asus.realtimeproject;

/**
 * Created by Asus on 8/14/2017.
 */

public class ItineraryUpload {
    public String itinerary;
    public boolean selected;

    public String getItinerary() {
        return itinerary;
    }

    public ItineraryUpload(String itinerary, boolean selected){
        this.itinerary = itinerary;
        this.selected = selected;
    }
    public ItineraryUpload(){

    }
}
