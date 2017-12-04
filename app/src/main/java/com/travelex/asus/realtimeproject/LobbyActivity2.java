package com.travelex.asus.realtimeproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LobbyActivity2 extends AppCompatActivity {

    RelativeLayout layout;
    FloatingActionButton fab;
    private List<ChatMessage> msgList;
    DatabaseReference mDatabaseRef, mDatabaseRef2, mDatabaseRef3;
    MessageAdapter messageAdapter;
    ListView listofMessage;
    FirebaseAuth auth;
    String userID, email, userID2;
    String uploadID, uploadID2;
    String messageID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);
        auth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();

        userID = bundle.getString("key"); //joiner
        userID2 = auth.getCurrentUser().getUid(); //host

        mDatabaseRef3 = FirebaseDatabase.getInstance().getReference("messageuser");
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference("privatemessage");

        uploadID = mDatabaseRef2.push().getKey();
        uploadID2 = mDatabaseRef2.push().getKey();

        email = auth.getCurrentUser().getEmail();

        Query search = mDatabaseRef3.orderByChild("user1").equalTo(userID2);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot movieSnapshot : dataSnapshot.getChildren()) {
                        final UserMessage userMessage = movieSnapshot.getValue(UserMessage.class);
                        if (userMessage.getUser2().equals(userID)) {
                            messageID = userMessage.getMessageID();

                            Query search2 = mDatabaseRef3.orderByChild("user1").equalTo(userID);
                            search2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot2) {
                                    if(dataSnapshot2.exists()){
                                        for(DataSnapshot movieSnapshot2 : dataSnapshot2.getChildren()){
                                            UserMessage userMessage2 = movieSnapshot2.getValue(UserMessage.class);
                                            if (userMessage2.getUser2().equals(userID2)) {// last code not sure
                                                final String messageID2 = userMessage2.getMessageID();
                                                Toast.makeText(getApplicationContext(), "Have History", Toast.LENGTH_SHORT).show();
                                                history(messageID, messageID2);

                                                displayChatMessage2(messageID2);

                                                mDatabaseRef = FirebaseDatabase.getInstance().getReference("privatemessage").child(messageID);
                                                mDatabaseRef.addChildEventListener(new ChildEventListener() {
                                                    @Override
                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                        displayChatMessage2(messageID);
                                                    }

                                                    @Override
                                                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                                                    }

                                                    @Override
                                                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                                                    }

                                                    @Override
                                                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                            }
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            break;

                        }
                        if(!userMessage.getUser2().equals(userID)){
                            Toast.makeText(getApplicationContext(), "No History", Toast.LENGTH_SHORT).show();
                            nohistory();

                        }
                    }
                }
                else{
                    nohistory();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void displayChatMessage(){
        msgList = new ArrayList<>();
        msgList.clear();
        listofMessage = (ListView)findViewById(R.id.list_of_message);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("privatemessage").child(uploadID);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //image upload class require default constructor
                    ChatMessage messageUpload = snapshot.getValue(ChatMessage.class);
                    msgList.add(messageUpload);
                }
                messageAdapter = new MessageAdapter(LobbyActivity2.this, R.layout.list_item, msgList);
                messageAdapter.notifyDataSetChanged();
                listofMessage.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void displayChatMessage2(final String messageID){
        msgList = new ArrayList<>();
        msgList.clear();
        listofMessage = (ListView)findViewById(R.id.list_of_message);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("privatemessage").child(messageID);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //image upload class require default constructor
                    ChatMessage messageUpload = snapshot.getValue(ChatMessage.class);
                    msgList.add(messageUpload);
                }
                messageAdapter = new MessageAdapter(LobbyActivity2.this, R.layout.list_item, msgList);
                messageAdapter.notifyDataSetChanged();
                listofMessage.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
    public void nohistory(){
        layout = (RelativeLayout)findViewById(R.id.activity_main);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textMessage = (EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference("privatemessage").child(uploadID).push().setValue(new ChatMessage(textMessage.getText().toString(),email));
                FirebaseDatabase.getInstance().getReference("privatemessage").child(uploadID2).push().setValue(new ChatMessage(textMessage.getText().toString(),email));

                UserMessage messageDetails = new UserMessage(uploadID, userID, userID2);
                mDatabaseRef3.child(uploadID).setValue(messageDetails);

                UserMessage messageDetails2 = new UserMessage(uploadID2, userID2, userID);
                mDatabaseRef3.child(uploadID2).setValue(messageDetails2);

                textMessage.setText("");
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("privatemessage").child(uploadID);
                mDatabaseRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        displayChatMessage2(uploadID);
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }
    public void history(final String messageID, final String messageID2){

        layout = (RelativeLayout)findViewById(R.id.activity_main);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textMessage = (EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference("privatemessage").child(messageID).push().setValue(new ChatMessage(textMessage.getText().toString(),email));
                FirebaseDatabase.getInstance().getReference("privatemessage").child(messageID2).push().setValue(new ChatMessage(textMessage.getText().toString(),email));

                textMessage.setText("");

            }
        });
    }
}
