package com.travelex.asus.realtimeproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

public class ChooseUser extends AppCompatActivity {

    RadioButton travel, travelagency;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        travel = (RadioButton) findViewById(R.id.radioButton3);
        travelagency = (RadioButton) findViewById(R.id.radioButton4);
        next = (Button) findViewById(R.id.button6);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(travel.isChecked()){
                    Intent intent = new Intent(ChooseUser.this, TravelProfile.class);
                    startActivity(intent);
                }
                else{
                    Intent intent2 = new Intent(ChooseUser.this, OrganizerProfile.class);
                    startActivity(intent2);
                }
            }
        });
    }
}
