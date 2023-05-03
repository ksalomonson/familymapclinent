package com.example.familymaplogin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class MapsActivity extends AppCompatActivity {

    private String IdOfEvent = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        IdOfEvent = getIntent().getExtras().getString("personID");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        FragmentManager fm = getSupportFragmentManager();
        MapsFragment mapFrag = (MapsFragment) fm.findFragmentById(R.id.myMapFragmentDifferent);


        if(mapFrag==null) {
            mapFrag=new MapsFragment();
            mapFrag.setEventId(IdOfEvent);
            mapFrag.setComingFromPerson(true);
            mapFrag.setContext();
            fm.beginTransaction()
                    .replace(R.id.mapLayout, mapFrag)
                    .commit();
        }
    }

}