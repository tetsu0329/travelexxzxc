package com.travelex.asus.realtimeproject;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ShowProfile extends AppCompatActivity {

    DatabaseReference mDatabaseRef2, mDatabaseRef3;
    ImageView imageView4;
    Button sendmessage, rateuser, btnrate2;
    EditText editText;
    Dialog dialog;
    RatingBar ratingBar;
    String uid;
    FirebaseAuth auth;
    ListView listView;
    List<RateUser> commentList;
    CommentListAdapter commentListAdapter;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showprofile2);
        Bundle extras = getIntent().getExtras();
        final String userID = extras.getString("key");

        auth = FirebaseAuth.getInstance();
        imageView4 = (ImageView) findViewById(R.id.imageView10);
        sendmessage = (Button) findViewById(R.id.btnmessage);
        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowProfile.this, LobbyActivity2.class);
                intent.putExtra("key", userID);
                startActivity(intent);
            }
        });
        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference();


        rateuser = (Button) findViewById(R.id.btnrate);
        rateuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new Dialog(ShowProfile.this);
                dialog.setTitle("Rate me now!");
                dialog.setContentView(R.layout.rate_layout);
                btnrate2 = (Button)dialog.findViewById(R.id.btnrate2);
                ratingBar = (RatingBar)dialog.findViewById(R.id.ratingBar);
                editText = (EditText)dialog.findViewById(R.id.etcomment);
                btnrate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mDatabaseRef3 = FirebaseDatabase.getInstance().getReference("rateuser").child(uid);

                        String id = String.valueOf(ratingBar.getRating());


                        String uploadID = mDatabaseRef3.push().getKey();
                        RateUser tourDetails = new RateUser(uploadID, auth.getCurrentUser().getUid(), editText.getText().toString(), id);
                        mDatabaseRef3.child(uploadID).setValue(tourDetails);

                        Toast.makeText(getApplicationContext(),"Thank you for your honesty!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        Query searchuser = mDatabaseRef2.child("userinfo").orderByChild("userID").startAt(userID).endAt(userID+"\uf8ff");
        searchuser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserAccount user = snapshot.getValue(UserAccount.class);
                    uid = user.getUserID();
                    Picasso.with(getApplicationContext()).load(user.getUserPhoto()).placeholder(R.drawable.progress_animation).into(imageView4);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        commentList = new ArrayList<>();
        listView = (ListView)findViewById(R.id.commentlist);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait while loading user information..... ");
        progressDialog.show();

        mDatabaseRef3 = FirebaseDatabase.getInstance().getReference("rateuser").child(userID);
        mDatabaseRef3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                commentList.clear();
                progressDialog.dismiss();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    RateUser productUpload = snapshot.getValue(RateUser.class);
                    commentList.add(productUpload);

                }
                commentListAdapter = new CommentListAdapter(ShowProfile.this, R.layout.comment_rowlayout, commentList);
                listView.setAdapter(commentListAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
