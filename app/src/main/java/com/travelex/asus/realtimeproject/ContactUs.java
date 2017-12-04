package com.travelex.asus.realtimeproject;

/**
 * Created by Asus on 9/26/2017.
 */

public class ContactUs {
    public String contactID;
    public String contactUser;
    public String contactMessg;

    public String getContactUser() {
        return contactUser;
    }

    public String getContactMessg() {
        return contactMessg;
    }

    public String getContactID() {
        return contactID;
    }
    public ContactUs (String contactID, String contactUser, String contactMessg){
        this.contactID = contactID;
        this.contactUser = contactUser;
        this.contactMessg = contactMessg;
    }

    public ContactUs (){

    }
}
