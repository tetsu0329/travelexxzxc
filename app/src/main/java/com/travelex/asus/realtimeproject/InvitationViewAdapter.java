package com.travelex.asus.realtimeproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Asus on 6/7/2017.
 */

public class InvitationViewAdapter extends ArrayAdapter <Invitation> {
    private Context context;
    private int resource;
    private List<Invitation> listImage;

    public InvitationViewAdapter(Context context, int resource, List<Invitation> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        DatabaseReference mDatabaseRef2 = FirebaseDatabase.getInstance().getReference();
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.invitation_row_layout,parent,false);
        final TextView textView = (TextView)row.findViewById(R.id.textView18);
        final TextView textView4 = (TextView)row.findViewById(R.id.textView29);
        final ImageView imageView = (ImageView)row.findViewById(R.id.imageView9);

        String tourid = listImage.get(position).getTourID();
        Query search = mDatabaseRef2.child("tourinfo").orderByChild("tourID").equalTo(tourid);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                    TourDetails3 productUpload = snapshot1.getValue(TourDetails3.class);
                    textView.setText(productUpload.categoryplace);
                    textView4.setText(productUpload.getTourJoiner());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        String uemail = listImage.get(position).getInviter().toLowerCase();
        Query search2 = mDatabaseRef2.child("userinfo").orderByChild("useremail").equalTo(uemail);
        search2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot2) {
                for (DataSnapshot snapshot3 : dataSnapshot2.getChildren()) {
                    UserAccount productUpload = snapshot3.getValue(UserAccount.class);
                    Picasso.with(context).load(productUpload.getUserPhoto()).into(imageView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TextView textView1 = (TextView)row.findViewById(R.id.textView27);
        textView1.setText(listImage.get(position).getInviter());

        TextView textView2 = (TextView)row.findViewById(R.id.textView28);
        textView2.setText(listImage.get(position).getTourID());

        return row;
    }
}
