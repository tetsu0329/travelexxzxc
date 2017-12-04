package com.travelex.asus.realtimeproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShowOwnTour extends AppCompatActivity {

    private List<RequestTour> accountList;
    ListView listView;

    ProgressDialog progressDialog;
    DatabaseReference mDatabaseRef2, mDatabaseRef3;
    String tourID;
    RequestViewAdapter requestViewAdapter;
    Dialog dialog, dialog2;
    private List<Joiner> joinerList;
    AcceptedJoinerAdapter acceptedJoinerAdapter;

    ImageButton imgbtn;
    FirebaseAuth auth;
    String emailfromuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_own_tour);

        Bundle bundle = getIntent().getExtras();
        tourID = bundle.getString("tourID");
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef3 = FirebaseDatabase.getInstance().getReference("invitation");
        accountList = new ArrayList<>();
        listView = (ListView)findViewById(R.id.requestList);
        auth = FirebaseAuth.getInstance();

        Button viewjoiner = (Button)findViewById(R.id.button9);
        viewjoiner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showjoiner();
            }
        });
        Button canceltour = (Button)findViewById(R.id.button14);
        canceltour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Query searchinfo = mDatabaseRef2.child("joinerinfo").orderByChild("tour").startAt(tourID).endAt(tourID+"\uf8ff");
                searchinfo.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            Joiner joiner = snapshot.getValue(Joiner.class);
                            String userid = joiner.getJoiner();
                            final DatabaseReference mDatabaseRef3 = FirebaseDatabase.getInstance().getReference("pushnotif").child(userid);
                            String uploadID = mDatabaseRef3.push().getKey();
                            PushNotification pushNotification = new PushNotification (uploadID,userid,"One of your tour has been cancelled check your profile");
                            mDatabaseRef3.child(uploadID).setValue(pushNotification);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Query searchinfo2 = mDatabaseRef2.child("tourinfo").orderByChild("tourID").startAt(tourID).endAt(tourID+"\uf8ff");
                searchinfo2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                        Intent intent = new Intent(ShowOwnTour.this, TouristDashboard.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while loading travel requests..... ");
        progressDialog.show();

        Query search2 = mDatabaseRef2.child("tourrequest").orderByChild("tourID").startAt(tourID).endAt(tourID+"\uf8ff");
        search2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accountList.clear();
                progressDialog.dismiss();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    RequestTour userAccount = snapshot.getValue(RequestTour.class);
                    accountList.add(userAccount);

                }
                requestViewAdapter = new RequestViewAdapter(ShowOwnTour.this, R.layout.owntravellayout, accountList);
                listView.setAdapter(requestViewAdapter);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imgbtn = (ImageButton)findViewById(R.id.imageButton);
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog2 = new Dialog(ShowOwnTour.this);
                dialog2.setTitle("Invite a joiner");
                dialog2.setContentView(R.layout.invitationlayout);
                dialog2.show();
                Button btn = (Button)dialog2.findViewById(R.id.button16);
                final ImageView imageView = (ImageView)dialog2.findViewById(R.id.imageView11);
                final TextView textView = (TextView)dialog2.findViewById(R.id.textView63);
                final TextView textView1 = (TextView)dialog2.findViewById(R.id.textView64);
                final Button btn2 = (Button)dialog2.findViewById(R.id.button17);
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText editText = (EditText)dialog2.findViewById(R.id.editText8);
                        emailfromuser = editText.getText().toString();
                        auth.fetchProvidersForEmail(emailfromuser).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                            @Override
                            public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                                if(task.isSuccessful()){
                                    imageView.setVisibility(View.VISIBLE);
                                    textView.setVisibility(View.VISIBLE);
                                    textView1.setVisibility(View.VISIBLE);
                                    btn2.setVisibility(View.VISIBLE);
                                    String finalemail = emailfromuser.toLowerCase();
                                    Query searchinfo2 = mDatabaseRef2.child("userinfo").orderByChild("useremail").startAt(finalemail).endAt(finalemail+"\uf8ff");
                                    searchinfo2.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                                                UserAccount userAccount = snapshot.getValue(UserAccount.class);
                                                Picasso.with(getApplicationContext()).load(userAccount.getUserPhoto()).into(imageView);
                                                textView.setText(userAccount.getUserName());
                                                textView1.setText(userAccount.getUserDescription());
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                                else{
                                    textView.setVisibility(View.VISIBLE);
                                    textView.setText("No Data Found");
                                }
                            }
                        });
                    }

                });
                btn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String uploadID = mDatabaseRef3.push().getKey();
                        Invitation invitation = new Invitation(tourID,emailfromuser, auth.getCurrentUser().getEmail(), uploadID);
                        mDatabaseRef3.child(uploadID).setValue(invitation);
                        Toast.makeText(getApplicationContext(), "Invited Successfully", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }
    public void showjoiner(){
        dialog = new Dialog(ShowOwnTour.this);
        dialog.setTitle("Accepted Joiners");
        dialog.setContentView(R.layout.acceptedlayout);

        joinerList = new ArrayList<>();
        listView = (ListView)dialog.findViewById(R.id.listView);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while loading travel information..... ");
        progressDialog.show();

        Query searchinfo = mDatabaseRef2.child("joinerinfo").orderByChild("tour").startAt(tourID).endAt(tourID+"\uf8ff");
        searchinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                joinerList.clear();
                progressDialog.dismiss();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Joiner joiner = snapshot.getValue(Joiner.class);
                    joinerList.add(joiner);
                    acceptedJoinerAdapter = new AcceptedJoinerAdapter(ShowOwnTour.this, R.layout.acceptedjoiner, joinerList);
                    listView.setAdapter(acceptedJoinerAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        dialog.show();
    }
}
