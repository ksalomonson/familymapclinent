package com.example.familymaplogin;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import Utilities.DataCache;

public class MainActivity extends AppCompatActivity {

    String birthID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iconify.with(new FontAwesomeModule());

        FragmentManager fm = getSupportFragmentManager();
        Fragment loginFrag = fm.findFragmentById(R.id.firstLayout);
        Fragment mapFrag = fm.findFragmentById(R.id.firstLayout);

        if (!DataCache.getInstance().getLoggedIn()){
//            if (loginFrag == null) {
                loginFrag = LoginFragment.newInstance(this);
                fm.beginTransaction()
                        .add(R.id.firstLayout, loginFrag)
                        .commit();
//            }
        } else {
            if(mapFrag==null) {
                mapFrag=new MapsFragment();
                fm.beginTransaction()
                        .replace(R.id.firstLayout, mapFrag)
                        .commit();
            }
        }
    }


    public void onLoginRegisterSuccess(){
        DataCache dataCache = DataCache.getInstance();
        dataCache.setLoggedIn(true);

        String birthIDOfUser = dataCache.getPersonEvents().get(dataCache.getAuthTokenModel().getUsername())[0].getEventID();

        birthID = birthIDOfUser;

        FragmentManager fm = getSupportFragmentManager();
        MapsFragment mapFrag = (MapsFragment) fm.findFragmentById(R.id.myMapFragmentDifferent);


        if(mapFrag==null) {
            mapFrag=new MapsFragment();
            mapFrag.setEventId(birthIDOfUser);
            mapFrag.setContext();

            fm.beginTransaction()
                    .replace(R.id.firstLayout, mapFrag)
                    .commit();
        }
    }

}