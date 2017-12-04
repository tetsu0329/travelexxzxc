package com.travelex.asus.realtimeproject;

/**
 * Created by Asus on 7/30/2017.
 */

public class Joiner {
    public String joiner;
    public String tour;
    public String slots;

    public String getJoiner() {
        return joiner;
    }

    public String getTour() {
        return tour;
    }

    public String getSlots(){
        return slots;
    }

    public Joiner (String joiner, String tour, String slots){
        this.joiner = joiner;
        this.tour = tour;
        this.slots = slots;
    }
    public Joiner (){

    }
}
