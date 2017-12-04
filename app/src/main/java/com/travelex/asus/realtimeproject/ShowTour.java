package com.travelex.asus.realtimeproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ShowTour extends AppCompatActivity {

    private DatabaseReference mDatabaseRef, mDatabaseRef2, mDatabaseRef3;
    private StorageReference mStorageRef;
    FirebaseAuth auth;
    public static final String FB_STORAGE_PATH = "tours/";
    public static final String FB_DATABASE_PATH = "tourinfo";
    public static final String FB_DATABASE_PATH2 = "tourrequest";
    ImageView imageView1, imageView2, imageView3;
    TextView location, description, itinerary, inclusion, date, number, slots;
    String tourID, tourslots;
    ProgressDialog progressDialog;
    Button join, back;
    Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_tour);


        Bundle bundle = getIntent().getExtras();
        tourID = bundle.getString("tourID");
        tourslots = bundle.getString("slots");
        auth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH).child(tourID);
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef3 = FirebaseDatabase.getInstance().getReference(FB_DATABASE_PATH2);

        imageView1 = (ImageView) findViewById(R.id.imageView6);
        imageView2 = (ImageView) findViewById(R.id.imageView7);
        imageView3 = (ImageView) findViewById(R.id.imageView8);

        location = (TextView) findViewById(R.id.textView33);
        description = (TextView) findViewById(R.id.textView35);
        itinerary = (TextView) findViewById(R.id.textView42);
        inclusion = (TextView) findViewById(R.id.textView44);
        date = (TextView) findViewById(R.id.textView37);
        number = (TextView) findViewById(R.id.textView38);
        slots = (TextView) findViewById(R.id.textView40);

        join = (Button) findViewById(R.id.joinbtn);
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showbox();
            }
        });

        back = (Button) findViewById(R.id.mmbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowTour.this, TouristDashboard.class);
                startActivity(intent);
            }
        });

        imageView1.setVisibility(View.GONE);
        imageView2.setVisibility(View.GONE);
        imageView3.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while loading travel information..... ");
        progressDialog.show();

        mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("tourImage3")){
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.VISIBLE);

                    Query search = mDatabaseRef2.child("tourinfo").orderByChild("tourID").startAt(tourID).endAt(tourID+"\uf8ff");
                    search.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                TourDetails3 tourDetails3 = snapshot.getValue(TourDetails3.class);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
                                Date d1;
                                try{
                                    String d = tourDetails3.getTourDate();
                                    d1 = dateFormat.parse(d);

                                    Time today = new Time(Time.getCurrentTimezone());
                                    today.setToNow();
                                    int day2 = today.monthDay;
                                    int month2 = today.month;
                                    int year2 = today.year-1900;

                                    Date d2 = new Date(year2, month2, day2);

                                    long diff = d1.getTime() - d2.getTime();
                                    long diffDays = diff / (24 * 60 * 60 * 1000);

                                    String daysleft = String.valueOf(diffDays)+ "day/s left";

                                    number.setText(daysleft);
                                    location.setText(tourDetails3.getTourLocation());
                                    description.setText(tourDetails3.getTourDescription());
                                    itinerary.setText(tourDetails3.getTourItinerary());
                                    inclusion.setText(tourDetails3.getTourInclusion());
                                    date.setText(tourDetails3.getTourDate());
                                    slots.setText(tourDetails3.getTourJoiner());
                                    Picasso.with(getApplicationContext()).load(tourDetails3.getTourImage1()).placeholder(R.drawable.progress_animation).into(imageView1);
                                    Picasso.with(getApplicationContext()).load(tourDetails3.getTourImage2()).placeholder(R.drawable.progress_animation).into(imageView2);
                                    Picasso.with(getApplicationContext()).load(tourDetails3.getTourImage3()).placeholder(R.drawable.progress_animation).into(imageView3);
                                    progressDialog.dismiss();

                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(dataSnapshot.hasChild("tourImage2")){
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    Query search = mDatabaseRef2.child("tourinfo").orderByChild("tourID").startAt(tourID).endAt(tourID+"\uf8ff");
                    search.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                TourDetails3 tourDetails3 = snapshot.getValue(TourDetails3.class);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
                                Date d1;
                                try{
                                    String d = tourDetails3.getTourDate();
                                    d1 = dateFormat.parse(d);

                                    Time today = new Time(Time.getCurrentTimezone());
                                    today.setToNow();
                                    int day2 = today.monthDay;
                                    int month2 = today.month;
                                    int year2 = today.year-1900;

                                    Date d2 = new Date(year2, month2, day2);

                                    long diff = d1.getTime() - d2.getTime();
                                    long diffDays = diff / (24 * 60 * 60 * 1000);

                                    String daysleft = String.valueOf(diffDays)+ "day/s left";

                                    number.setText(daysleft);
                                    location.setText(tourDetails3.getTourLocation());
                                    description.setText(tourDetails3.getTourDescription());
                                    itinerary.setText(tourDetails3.getTourItinerary());
                                    inclusion.setText(tourDetails3.getTourInclusion());
                                    date.setText(tourDetails3.getTourDate());
                                    slots.setText(tourDetails3.getTourJoiner());
                                    Picasso.with(getApplicationContext()).load(tourDetails3.getTourImage1()).placeholder(R.drawable.progress_animation).into(imageView1);
                                    Picasso.with(getApplicationContext()).load(tourDetails3.getTourImage2()).placeholder(R.drawable.progress_animation).into(imageView2);
                                    progressDialog.dismiss();

                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                else if(dataSnapshot.hasChild("tourImage1")){
                    imageView1.setVisibility(View.VISIBLE);
                    Query search = mDatabaseRef2.child("tourinfo").orderByChild("tourID").startAt(tourID).endAt(tourID+"\uf8ff");
                    search.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                                TourDetails3 tourDetails3 = snapshot.getValue(TourDetails3.class);
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
                                Date d1;
                                try{
                                    String d = tourDetails3.getTourDate();
                                    d1 = dateFormat.parse(d);

                                    Time today = new Time(Time.getCurrentTimezone());
                                    today.setToNow();
                                    int day2 = today.monthDay;
                                    int month2 = today.month;
                                    int year2 = today.year-1900;

                                    Date d2 = new Date(year2, month2, day2);

                                    long diff = d1.getTime() - d2.getTime();
                                    long diffDays = diff / (24 * 60 * 60 * 1000);

                                    String daysleft = String.valueOf(diffDays)+ "day/s left";

                                    number.setText(daysleft);
                                    location.setText(tourDetails3.getTourLocation());
                                    description.setText(tourDetails3.getTourDescription());
                                    itinerary.setText(tourDetails3.getTourItinerary());
                                    inclusion.setText(tourDetails3.getTourInclusion());
                                    date.setText(tourDetails3.getTourDate());
                                    slots.setText(tourDetails3.getTourJoiner());
                                    Picasso.with(getApplicationContext()).load(tourDetails3.getTourImage1()).placeholder(R.drawable.progress_animation).into(imageView1);
                                    progressDialog.dismiss();

                                }
                                catch(Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public void showbox(){
        final Query search = mDatabaseRef2.child("tourinfo").orderByChild("tourID").startAt(tourID).endAt(tourID+"\uf8ff");
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    TourDetails3 tourDetails3 = snapshot.getValue(TourDetails3.class);
                    final String userID = tourDetails3.getUserID();
                    Query search2 = mDatabaseRef2.child("userinfo").orderByChild("userID").startAt(userID).endAt(userID+"\uf8ff");
                    search2.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            for(DataSnapshot snapshot1: dataSnapshot1.getChildren()){
                                UserAccount account = snapshot1.getValue(UserAccount.class);
                                dialog = new Dialog(ShowTour.this);
                                dialog.setTitle("Contact Travel Organizer");
                                dialog.setContentView(R.layout.showtravelbox);
                                TextView textView = (TextView)dialog.findViewById(R.id.textView19);
                                textView.setText(account.getUserName());

                                ImageView imageView = (ImageView)dialog.findViewById(R.id.imageView5);
                                Picasso.with(getApplicationContext()).load(account.getUserPhoto()).placeholder(R.drawable.progress_animation).into(imageView);

                                TextView textView2 = (TextView)dialog.findViewById(R.id.textView45);
                                textView2.setText(account.getUserAddress());

                                TextView textView3 = (TextView)dialog.findViewById(R.id.textView47);
                                textView3.setText(account.getUserDescription());

                                final Button ask = (Button)dialog.findViewById(R.id.button10);
                                ask.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        String userID = auth.getCurrentUser().getUid();
                                        String requestID = mDatabaseRef3.push().getKey();
                                        EditText editText = (EditText)dialog.findViewById(R.id.editText9);
                                        String nums = editText.getText().toString();
                                        int req = Integer.parseInt(nums);
                                        int max = Integer.parseInt(tourslots);
                                        if(max>=req){
                                            RequestTour requestTour = new RequestTour(requestID,nums,tourID,userID);
                                            mDatabaseRef3.child(requestID).setValue(requestTour);
                                            ask.setEnabled(false);
                                            editText.setEnabled(false);
                                            Toast.makeText(getApplicationContext(),"You have requested to Join", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                        else{
                                            Toast.makeText(getApplicationContext(),"Already in Maximum number", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                                dialog.show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}