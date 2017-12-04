package com.travelex.asus.realtimeproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Asus on 7/23/2017.
 */

public class RequestViewAdapter extends ArrayAdapter<RequestTour> {
    private Context context;
    private int resource;
    private List<RequestTour> listuser;
    String tourID;
    String requestSlots = null;

    public RequestViewAdapter(Context context, int resource, List<RequestTour> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listuser = objects;
    }
    @NonNull
    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.owntravellayout,parent,false);

        final TextView txtname = (TextView)row.findViewById(R.id.textView49);
        final TextView txtnum = (TextView)row.findViewById(R.id.textView50);
        final ImageView imageView = (ImageView)row.findViewById(R.id.imageView9);
        final TextView txtslots = (TextView)row.findViewById(R.id.textView52);

        final DatabaseReference mDatabaseRef2 = FirebaseDatabase.getInstance().getReference();
        final String requestID = listuser.get(position).getRequestID();
        final String tourID = listuser.get(position).getTourID();
        final String userID = listuser.get(position).getUserID();


        Query search2 = mDatabaseRef2.child("userinfo").orderByChild("userID").startAt(userID).endAt(userID+"\uf8ff");
        search2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    UserAccount userAccount = snapshot.getValue(UserAccount.class);
                    txtname.setText(userAccount.getUserName());
                    txtnum.setText(userAccount.getUserDescription());
                    Picasso.with(context).load(userAccount.getUserPhoto()).into(imageView);
                    Query search3 = mDatabaseRef2.child("tourrequest").orderByChild("requestID").equalTo(requestID);
                    search3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            for(DataSnapshot snapshot1: dataSnapshot1.getChildren()) {
                                RequestTour requestTour = snapshot1.getValue(RequestTour.class);
                                txtslots.setText(requestTour.getNumjoiner());
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
        ImageButton imageButton1 = (ImageButton)row.findViewById(R.id.imageButton);
        imageButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ShowProfile.class);

                i.putExtra("key",userID);
                view.getContext().startActivity(i);
            }
        });

        ImageButton imageButton = (ImageButton)row.findViewById(R.id.imageButton3);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String id = txtslots.getText().toString();
                Toast.makeText(context,id,Toast.LENGTH_SHORT).show();
                final DatabaseReference mDatabaseRef3 = FirebaseDatabase.getInstance().getReference("pushnotif").child(userID);
                String uploadID = mDatabaseRef3.push().getKey();
                PushNotification pushNotification = new PushNotification (uploadID,userID,"You have been accepted in a tour check your Profile");
                mDatabaseRef3.child(uploadID).setValue(pushNotification);

                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
                Query search = ref2.child("tourinfo").orderByChild("tourID").startAt(tourID).endAt(tourID+"\uf8ff");
                search.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(final DataSnapshot snapshot: dataSnapshot.getChildren()){
                            TourDetails3 tourDetails3 = snapshot.getValue(TourDetails3.class);
                            final String joiner = tourDetails3.getTourJoiner();

                            Query search2 = ref2.child("tourrequest").orderByChild("tourID").startAt(tourID).endAt(tourID+"\uf8ff");
                            search2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot1) {
                                    String numholder = null;
                                    for(DataSnapshot snapshot1: dataSnapshot1.getChildren()) {
                                        //RequestTour requestTour = snapshot1.getValue(RequestTour.class);
                                        int num = Integer.parseInt(joiner);
                                        int newnum = Integer.parseInt(id);
                                        int total = num - newnum;
                                        String parsednum = String.valueOf(total);
                                        //numholder = String.valueOf(newnum);
                                        snapshot.getRef().child("tourJoiner").setValue(parsednum);


                                    }
                                    final DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference("joinerinfo");
                                    String joinerID = ref3.push().getKey();
                                    Joiner joiner= new Joiner(userID, tourID, id);
                                    ref3.child(joinerID).setValue(joiner);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //int num = Integer.parseInt(joiner);
                            //int newnum = num -1;
                            //String parsednum = String.valueOf(newnum);
                            //snapshot.getRef().child("tourJoiner").setValue(parsednum);

                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                Query delete = ref2.child("tourrequest").orderByChild("requestID").startAt(requestID).endAt(requestID+"\uf8ff");
                delete.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
        ImageButton imageButton2 = (ImageButton)row.findViewById(R.id.imageButton2);
        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference();
                Query delete2 = ref2.child("tourrequest").orderByChild("requestID").equalTo("requestID");
                delete2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        return row;
    }
}
