package com.travelex.asus.realtimeproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.List;

/**
 * Created by Asus on 6/7/2017.
 */

public class ItineraryViewAdapter extends ArrayAdapter <ItineraryUpload> {
    private Context context;
    private int resource;
    private List<ItineraryUpload> listImage;

    public ItineraryViewAdapter(Context context, int resource, List<ItineraryUpload> objects){
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
        row = layoutInflater.inflate(R.layout.checklayout,parent,false);


        CheckBox cbName = (CheckBox) row.findViewById(R.id.checkBox);

        cbName.setText(listImage.get(position).getItinerary());

        return row;
    }
}
