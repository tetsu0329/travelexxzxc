package com.travelex.asus.realtimeproject;

/**
 * Created by Asus on 7/28/2017.
 */

public class Invitation {
    private String tourID, invited, inviter, invitationid;

    public Invitation(String tourID, String invited, String inviter, String invitationid){
        this.tourID = tourID;
        this.invited = invited;
        this.inviter = inviter;
        this.invitationid = invitationid;

    }
    public Invitation(){

    }
    public String getTourID() {
        return tourID;
    }

    public String getInvited(){ return invited; }

    public String getInviter(){ return  inviter; }

    public String getInvitationid() { return invitationid; }

}
