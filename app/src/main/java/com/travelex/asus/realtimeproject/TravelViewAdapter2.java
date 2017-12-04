package com.travelex.asus.realtimeproject;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Asus on 7/23/2017.
 */

public class TravelViewAdapter2 extends ArrayAdapter<Joiner> {
    private Context context;
    private int resource;
    private List<Joiner> listImage;

    public TravelViewAdapter2(Context context, int resource, List<Joiner> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }
    @NonNull
    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        DatabaseReference mDatabaseRef2 = FirebaseDatabase.getInstance().getReference();
        View row = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.showprofile_layout,parent,false);
        final TextView txtloc = (TextView)row.findViewById(R.id.textView57);
        final TextView txtdate = (TextView)row.findViewById(R.id.textView58);
        final TextView txtslot = (TextView)row.findViewById(R.id.textView61);
        ImageButton btn = (ImageButton)row.findViewById(R.id.imageButton6);
        final String tourID = listImage.get(position).getTour();
        Query search = mDatabaseRef2.child("tourinfo").orderByChild("tourID").equalTo(tourID);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot1) {
                for (DataSnapshot snapshot1 : dataSnapshot1.getChildren()) {
                    TourDetails3 productUpload = snapshot1.getValue(TourDetails3.class);
                    txtloc.setText(productUpload.getTourLocation());
                    txtdate.setText(productUpload.getTourDate());
                    txtslot.setText(listImage.get(position).getSlots());


                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LobbyActivity.class);
                intent.putExtra("tourID", tourID);
                context.startActivity(intent);

            }
        });

        return row;
    }
}
