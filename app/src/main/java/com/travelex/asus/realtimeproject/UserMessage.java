package com.travelex.asus.realtimeproject;

/**
 * Created by Asus on 7/28/2017.
 */

public class UserMessage {
    private String messageID, user1, user2;

    public UserMessage(String messageID, String user1, String user2){
        this.messageID = messageID;
        this.user1 = user1;
        this.user2 = user2;

    }
    public UserMessage(){

    }
    public String getMessageID() {
        return messageID;
    }

    public String getUser1(){ return user1; }

    public String getUser2(){ return  user2; }

    public void setMessageID(String messageID, String user1, String user2) {
        this.messageID = messageID;
        this.user1 = user1;
        this.user2 = user2;
    }

}
