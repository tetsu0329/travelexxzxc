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
 * Created by Asus on 7/23/2017.
 */

public class AcceptedJoinerAdapter extends ArrayAdapter<Joiner> {
    private Context context;
    private int resource;
    private List<Joiner> listImage;

    public AcceptedJoinerAdapter(Context context, int resource, List<Joiner> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }
    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        DatabaseReference mDatabaseRef2 = FirebaseDatabase.getInstance().getReference();

        View row = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.acceptedjoiner,parent,false);

        final TextView txtname = (TextView)row.findViewById(R.id.textView49);
        final TextView txtslots = (TextView)row.findViewById(R.id.textView53);
        final TextView txtnum = (TextView)row.findViewById(R.id.textView50);
        final ImageView imageView = (ImageView)row.findViewById(R.id.imageView9);

        String userID = listImage.get(position).getJoiner();

        Query searchinfo2 = mDatabaseRef2.child("userinfo").orderByChild("userID").startAt(userID).endAt(userID+"\uf8ff");
        searchinfo2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                for(DataSnapshot snapshot1: dataSnapshot1.getChildren()) {
                    UserAccount account = snapshot1.getValue(UserAccount.class);
                    txtname.setText(account.getUserName());
                    txtnum.setText(account.getUserDescription());
                    Picasso.with(context).load(account.getUserPhoto()).into(imageView);
                    txtslots.setText(listImage.get(position).getSlots());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return row;
    }
}
