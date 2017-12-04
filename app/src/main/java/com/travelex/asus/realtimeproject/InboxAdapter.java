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

public class InboxAdapter extends ArrayAdapter<UserMessage> {
    private Context context;
    private int resource;
    private List<UserMessage> listImage;
    DatabaseReference mDatabaseRef;

    public InboxAdapter(Context context, int resource, List<UserMessage> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        listImage = objects;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = layoutInflater.inflate(R.layout.ownmessagelayout,parent,false);
        final TextView txtname = (TextView)row.findViewById(R.id.textView49);
        final TextView userID2 = (TextView)row.findViewById(R.id.textView50);
        userID2.setText(listImage.get(position).getUser2());

        final ImageView img = (ImageView)row.findViewById(R.id.imageView9);

        //txtname.setText(listImage.get(position).getUser2());
        String user2id = listImage.get(position).getUser2();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("userinfo");
        Query search = mDatabaseRef.orderByChild("userID").equalTo(user2id);
        search.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    UserAccount acc = snapshot.getValue(UserAccount.class);
                    txtname.setText(acc.getUserName());

                    Picasso.with(context).load(acc.getUserPhoto()).into(img);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return row;
    }
}
