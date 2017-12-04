package com.travelex.asus.realtimeproject;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Asus on 7/23/2017.
 */

public class TravelViewAdapter extends ArrayAdapter<TourDetails3> {
    private Context context;
    private int resource;
    private List<TourDetails3> listImage;

    public TravelViewAdapter (Context context, int resource, List<TourDetails3> objects){
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
        row = layoutInflater.inflate(R.layout.travellayout,parent,false);
        TextView txtloc = (TextView)row.findViewById(R.id.textView18);
        TextView txtdate = (TextView)row.findViewById(R.id.textView31);
        TextView daterem = (TextView)row.findViewById(R.id.textView28);
        TextView joiners = (TextView)row.findViewById(R.id.textView19);
        LinearLayout layout = (LinearLayout)row.findViewById(R.id.travellocation);

        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy");
        Date d1;
        try{
            String d = listImage.get(position).getTourDate();
            d1 = dateFormat.parse(d);

            Time today = new Time(Time.getCurrentTimezone());
            today.setToNow();
            int day2 = today.monthDay;
            int month2 = today.month;
            int year2 = today.year-1900;

            Date d2 = new Date(year2, month2, day2);

            long diff = d1.getTime() - d2.getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);

            String daysleft = String.valueOf(diffDays);
            txtloc.setText(listImage.get(position).getTourLocation());
            String loc = listImage.get(position).getTourLocation().toLowerCase();
            String finalloc =  loc.replaceAll("\\s","");
            int resid = context.getResources().getIdentifier(finalloc, "drawable", context.getPackageName());
            layout.setBackgroundResource(resid);
            txtdate.setText(listImage.get(position).getTourDate());
            String join = listImage.get(position).getTourJoiner() + " slots left! ";
            joiners.setText(join);
            daterem.setText(daysleft);

        }
        catch(Exception e){
            e.printStackTrace();
        }
        return row;
    }
}
