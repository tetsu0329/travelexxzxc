package com.travelex.asus.realtimeproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by Asus on 9/19/2017.
 */

public class CommentListAdapter extends ArrayAdapter<RateUser> {
    private Context context;
    private int resource;
    private List<RateUser> listImage;

    public CommentListAdapter(Context context, int resource, List<RateUser> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        DatabaseReference mdata = FirebaseDatabase.getInstance().getReference();
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.comment_rowlayout, parent, false);
        final TextView textView = (TextView) row.findViewById(R.id.textView26);
        final TextView textView2 = (TextView) row.findViewById(R.id.textView65);
        final TextView textView3 = (TextView) row.findViewById(R.id.textView66);


        String number = "Ratings: " + listImage.get(position).getRateNum();
        String comm = listImage.get(position).getRateComment();


        textView2.setText(number);
        textView3.setText(comm);

        Query search = mdata.child("userinfo").orderByChild("userID").equalTo(listImage.get(position).getRateUser());
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UserAccount user = snapshot.getValue(UserAccount.class);
                    String person = "Rated by: " + user.getUserName();
                    textView.setText(person);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return row;
    }
}