package com.travelex.asus.realtimeproject;

/**
 * Created by Asus on 7/23/2017.
 */

public class TourDetails3 {
    public String tourID;
    public String userID;
    public String tourLocation;
    public String tourDate;
    public String tourJoiner;
    public String tourDescription;
    public String tourItinerary;
    public String tourInclusion;
    public String tourImage1;
    public String tourImage2;
    public String tourImage3;
    public String categoryisland;
    public String categoryplace;

    public String getTourID(){
        return tourID;
    }
    public String getTourImage1() {
        return tourImage1;
    }

    public String getTourImage2() {
        return tourImage2;
    }

    public String getTourImage3() {
        return tourImage3;
    }

    public String getUserID() {
        return userID;
    }

    public String getTourLocation() {
        return tourLocation;
    }

    public String getTourDate() {
        return tourDate;
    }

    public String getTourJoiner() {
        return tourJoiner;
    }

    public String getTourDescription() {
        return tourDescription;
    }

    public String getTourItinerary() {
        return tourItinerary;
    }

    public String getTourInclusion() {
        return tourInclusion;
    }

    public String getCategoryisland(){
        return categoryisland;
    }
    public String getCategoryplace(){
        return categoryplace;
    }

    public TourDetails3(String tourID, String userID, String tourLocation, String tourDate, String tourJoiner, String tourDescription, String tourItinerary,
                        String tourInclusion, String tourImage1, String tourImage2, String tourImage3, String categoryisland, String categoryplace){
        this.tourID = tourID;
        this.userID = userID;
        this.tourLocation = tourLocation;
        this.tourDate = tourDate;
        this.tourJoiner = tourJoiner;
        this.tourDescription = tourDescription;
        this.tourItinerary = tourItinerary;
        this.tourInclusion = tourInclusion;
        this.tourImage1 = tourImage1;
        this.tourImage2 = tourImage2;
        this.tourImage3 = tourImage3;
        this.categoryisland = categoryisland;
        this.categoryplace = categoryplace;
    }

    public TourDetails3(){

    }
}
