package com.travelex.asus.realtimeproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Asus on 6/7/2017.
 */

public class userViewAdapter extends ArrayAdapter <UserAccount> {
    private Context context;
    private int resource;
    private List<UserAccount> listImage;

    public userViewAdapter(Context context, int resource, List<UserAccount> objects){
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
        row = layoutInflater.inflate(R.layout.useraccount_row_layout,parent,false);
        final TextView textView = (TextView)row.findViewById(R.id.textView18);
        final TextView textView4 = (TextView)row.findViewById(R.id.textView27);
        final TextView textView5 = (TextView)row.findViewById(R.id.textView28);
        final TextView textView6 = (TextView)row.findViewById(R.id.textView30);
        final TextView textView7 = (TextView)row.findViewById(R.id.textView31);
        final ImageView imageView = (ImageView)row.findViewById(R.id.imageView9);

        textView.setText(listImage.get(position).getUserName());
        textView4.setText(listImage.get(position).getUseremail());
        textView5.setText(listImage.get(position).getUserID());
        textView6.setText(listImage.get(position).getUserType());
        textView7.setText(listImage.get(position).getUserStatus());
        Picasso.with(context).load(listImage.get(position).getUserPhoto()).into(imageView);


        return row;
    }
}
