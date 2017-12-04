package com.travelex.asus.realtimeproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LobbyActivity extends AppCompatActivity {

    RelativeLayout layout;
    FloatingActionButton fab;
    private List<ChatMessage> msgList;
    DatabaseReference mDatabaseRef;
    MessageAdapter messageAdapter;
    ListView listofMessage;
    FirebaseAuth auth;
    String tourID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lobby);
        auth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();

        tourID = bundle.getString("tourID");
        final String email = auth.getCurrentUser().getEmail();

        layout = (RelativeLayout)findViewById(R.id.activity_main);
        fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText textMessage = (EditText)findViewById(R.id.input);
                FirebaseDatabase.getInstance().getReference("messagelobby").child(tourID).push().setValue(new ChatMessage(textMessage.getText().toString(),email));
                textMessage.setText("");

            }
        });
        displayChatMessage();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("messagelobby").child(tourID);
        mDatabaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                displayChatMessage();
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
    public void displayChatMessage(){
        msgList = new ArrayList<>();
        msgList.clear();
        listofMessage = (ListView)findViewById(R.id.list_of_message);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("messagelobby").child(tourID);
        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                msgList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    //image upload class require default constructor
                    ChatMessage messageUpload = snapshot.getValue(ChatMessage.class);
                    msgList.add(messageUpload);
                }
                messageAdapter = new MessageAdapter(LobbyActivity.this, R.layout.list_item, msgList);
                messageAdapter.notifyDataSetChanged();
                listofMessage.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
