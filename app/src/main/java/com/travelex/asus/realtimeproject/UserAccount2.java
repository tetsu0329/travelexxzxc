package com.travelex.asus.realtimeproject;

/**
 * Created by Asus on 7/16/2017.
 */

public class UserAccount2 {
    public String userID;
    public String userName;
    public String userAddress;
    public String userPhoto;
    public String userDescription;
    public String userType;
    public String useremail;
    public String useraff;
    public String userStatus;

    public String getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public String getUserDescription() {
        return userDescription;
    }
    public String getUserType(){
        return userType;
    }

    public String getUseremail(){ return useremail; }

    public String getUseraff (){ return useraff; }

    public String getUserStatus (){ return userStatus; }

    public UserAccount2(String userID, String userName, String userAddress, String userPhoto, String userDescription, String userType, String useremail, String useraff, String userStatus){
        this.userID = userID;
        this.userName = userName;
        this.userAddress = userAddress;
        this.userPhoto = userPhoto;
        this.userDescription = userDescription;
        this.userType = userType;
        this.useremail = useremail;
        this.useraff = useraff;
        this.userStatus = userStatus;
    }
    public UserAccount2(){

    }

}
