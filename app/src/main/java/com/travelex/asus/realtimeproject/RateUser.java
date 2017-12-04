package com.travelex.asus.realtimeproject;

/**
 * Created by Asus on 7/30/2017.
 */

public class RateUser {
    public String rateID;
    public String rateUser;
    public String rateComment;
    public String rateNum;

    public String getRateID() {
        return rateID;
    }

    public String getRateUser (){
        return rateUser;
    }

    public String getRateComment() {
        return rateComment;
    }

    public String getRateNum() {
        return rateNum;
    }

    public RateUser(String rateID, String rateUser, String rateComment, String rateNum){
        this.rateID = rateID;
        this.rateUser = rateUser;
        this.rateComment = rateComment;
        this.rateNum = rateNum;
    }
    public RateUser(){

    }
}
