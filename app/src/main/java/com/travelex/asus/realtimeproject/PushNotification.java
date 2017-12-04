package com.travelex.asus.realtimeproject;

/**
 * Created by Asus on 9/19/2017.
 */

public class PushNotification {
    public String notifID;
    public String notifUser;
    public String notifComment;

    public String getnotifID() {
        return notifID;
    }

    public String getnotifUser (){
        return notifUser;
    }

    public String getnotifComment() {
        return notifComment;
    }

    public PushNotification(String notifID, String notifUser, String notifComment){
        this.notifID = notifID;
        this.notifUser = notifUser;
        this.notifComment = notifComment;
    }
    public PushNotification(){

    }
}
