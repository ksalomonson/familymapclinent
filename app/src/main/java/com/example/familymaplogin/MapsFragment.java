package com.example.familymaplogin;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.ArraySet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import Utilities.DataCache;
import model.Event;
import model.PersonsModel;


public class MapsFragment extends Fragment {
    private  String centeredID;
    private GoogleMap mMap;
    private Event mapsEventSelected;
    private Boolean markerClicked = false;
    private Boolean comingFromPerson = false;
    private final Vector<Marker> markers = new Vector<>();
    private final Vector<Polyline> poly_lines= new Vector<>();


    public MapsFragment() {
        // Required empty public constructor
    }

    public void setEventId(String in){
        centeredID = in;
    }

    public void setContext(){
    }


    public void setComingFromPerson(Boolean bool){
        comingFromPerson = bool;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (comingFromPerson){
            setEventId(DataCache.getInstance().getEventString());
            setHasOptionsMenu(true);
        } else {
            setHasOptionsMenu(true);
        }
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        MenuInflater inflater = getMenuInflater();
        if(getActivity() instanceof MainActivity) {
            inflater.inflate(R.menu.activity_main, menu);

            MenuItem personMenuItem = menu.findItem(R.id.search);
            personMenuItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_search)
                    .colorRes(R.color.white)
                    .actionBarSize());
            MenuItem settingsMenuItem = menu.findItem(R.id.settings);
            settingsMenuItem.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_gear)
                    .colorRes(R.color.white)
                    .actionBarSize());
        }
        else{
            inflater.inflate(R.menu.back_menu, menu);

            MenuItem back = menu.findItem(R.id.back);
            back.setIcon(new IconDrawable(getContext(), FontAwesomeIcons.fa_arrow_left)
                    .colorRes(R.color.black)
                    .actionBarSize());


        }
        return ;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if(getActivity() instanceof MainActivity) {
            switch (item.getItemId()) {
                case R.id.search:
                    Intent intentThree = new Intent(getActivity(), SearchActivity.class);
                    startActivity(intentThree);
                    return true;
                case R.id.settings:
                    Intent intent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(intent);
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
        else{
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_maps, container, false);


        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap){
//
                mMap = googleMap;

                DataCache dataCache = DataCache.getInstance();

                Map<String, Event> events = dataCache.getEvents();

                Colors colors = Colors.getInstance();
                List<String> colorsToUse = colors.getColorsList();

                Set<String> someEventList = new ArraySet<String>();
                //Get all of the events and put them into a list
                for (Event value : events.values()){
                    someEventList.add(value.getEventType().toLowerCase());
                }

                //in the event userEvents excuses an event type
                List<String> finalListOfEventTypes = new ArrayList<String>();

                finalListOfEventTypes.addAll(someEventList);

                dataCache.setAllEventTypes(finalListOfEventTypes);

                markers.clear();
                for(Event value : DataCache.getInstance().getValidEvents()){
                    Event event = value;

                    int indexOfColorToUse = finalListOfEventTypes.indexOf(event.getEventType().toLowerCase()) % 8;
                    String colorHexValue = colorsToUse.get(indexOfColorToUse);

                    LatLng eventLatLong2 = new LatLng(event.getLatitude(), event.getLongitude());

                    Marker m = mMap.addMarker(new MarkerOptions().position(eventLatLong2)
                            .icon(getMarkerIcon(colorHexValue)));

                    m.setTag(event);
                    markers.add(m);


                }

                if (comingFromPerson){
                    Event eventToCenter = dataCache.getEventById(centeredID);
                    Event eventSelected = dataCache.getEventById(centeredID);
                    LatLng placeToCenter = new LatLng(eventToCenter.getLatitude(), eventToCenter.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(placeToCenter));
                    mapsEventSelected = eventSelected;
                    clickMarker(v);

                }

                if (getActivity() instanceof EventActivity ) {
                    Event eventToCenter = dataCache.getEventById(DataCache.getInstance().getEventString());


                    mMap.getUiSettings().setZoomControlsEnabled(true);

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(eventToCenter.getLatitude(), eventToCenter.getLongitude())) // San Francisco
                            .zoom(5)
                            .build();
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    Marker marker = null;
                    for(Marker m: markers){
                        Event markerEvent = (Event) m.getTag();
                        if(markerEvent.getEventID().equals(eventToCenter.getEventID())){
                            marker = m;
                        }
                    }
                    mapsEventSelected = eventToCenter;
                    clickMarker(v);
                    drawLines(marker);
                }

                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        DataCache cm = DataCache.getInstance();
                        drawLines(marker);

                        mapsEventSelected = (Event) marker.getTag();

                        clickMarker(v);

                        Log.d("Click","was clicked");
                        return false;
                    }
                });
            } //end of onMapReady
        }); //end of get async

        LinearLayout bottomOfScreen = (LinearLayout) v.findViewById(R.id.bottomOfScreen);
        bottomOfScreen.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start person activity
                if (markerClicked){
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    Bundle mBundle  = new Bundle();

                    mBundle.putString("personID", mapsEventSelected.getPersonID());
                    intent.putExtras(mBundle);

                    startActivity(intent);
                }

            }
        });

        return v;
    }

    @Override
    public void onResume() {
        if(DataCache.getInstance().isFilterIsActive()) {
            DataCache.getInstance().setFilterIsActive(false);
            if (mMap != null) {
                mMap.clear();

            }

            for(Event value : DataCache.getInstance().getValidEvents()){
                List<String> finalListOfEventTypes = DataCache.getInstance().getFinalListOfEventTypes();
                int indexOfColorToUse = finalListOfEventTypes.indexOf(value.getEventType().toLowerCase()) % 8;
                Colors colors = Colors.getInstance();
                List<String> colorsToUse = colors.getColorsList();
                String colorHexValue = colorsToUse.get(indexOfColorToUse);

                LatLng eventLatLong2 = new LatLng(value.getLatitude(), value.getLongitude());

                Marker m = mMap.addMarker(new MarkerOptions().position(eventLatLong2)
                        .icon(getMarkerIcon(colorHexValue)));

                m.setTag(value);
                markers.add(m);


            }
        }
        super.onResume();
    }

    public void clickMarker(View v){
        DataCache dataCache = DataCache.getInstance();

        PersonsModel personSelected = dataCache.getPersonById(mapsEventSelected.getPersonID());

        ImageView img = (ImageView) v.findViewById(R.id.iconImageView);

        if (personSelected.getGender().equals("f")){
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).colorRes(R.color.femaleColor).sizeDp(40);
            img.setImageDrawable(genderIcon);
        } else {
            Drawable genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).colorRes(R.color.maleColor).sizeDp(40);
            img.setImageDrawable(genderIcon);
        }

        TextView textTop = v.findViewById(R.id.textTop);
        TextView textBottom = v.findViewById(R.id.textBottom);

        textTop.setText(personSelected.getFirstName() + " " + personSelected.getLastName());
        textBottom.setText(mapsEventSelected.getEventType());
        markerClicked = true;
    }


    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    public void drawLines(Marker marker){
        for(Polyline p: poly_lines){
            p.remove();
        }
        DataCache instance = DataCache.getInstance();

        DataCache datacache = DataCache.getInstance();
        //Set up Text
        Event markerEvent = (Event) marker.getTag();
        if(markerEvent == null){
            System.out.println("BROKEN");
            return;
        }
        //establish event
        String tempID = markerEvent.getPersonID();
        PersonsModel tempPerson = instance.getPersonById(tempID);
        String name = tempPerson.getFirstName() + " " + tempPerson.getLastName();
        String eventType = markerEvent.getEventType();
        String place = markerEvent.getCity() + " , " + markerEvent.getCountry();
        String tempYear = Integer.toString(markerEvent.getYear());
        //Create Spouse Lines
        if(instance.isSpouseLines()){
            //Check to see if Person has a spouse
            Event event = (Event)marker.getTag();
            String personID = event.getPersonID();
            //Get Person
            PersonsModel person = datacache.getPersonById(personID);
            //See if they have a Spouse
            if(person.getSpouseID() != null){
                String spouseID = person.getSpouseID();
                //Get the spouse markers
                Vector<Marker> spouseMarkers = new Vector<Marker>();
                for(Marker m: markers){
                    Event tempEvent = (Event)m.getTag();
                    if(tempEvent != null) {
                        if (tempEvent.getPersonID().equals(spouseID)) {
                            spouseMarkers.add(m);
                        }
                    }
                }
                //Figure out the first Event to happen
                int early_date = 2100;
                Marker best = null;
                for(Marker m: spouseMarkers){
                    Event tempEvent = (Event)m.getTag();
                    int year = tempEvent.getYear();
                    if(year < early_date){
                        early_date = year;
                        best = m;
                    }
                }
                if(best !=null) {
                    Polyline spouseLine = mMap.addPolyline(new PolylineOptions()
                            .add(best.getPosition(), marker.getPosition())
                            .width(5)
                            .color(Color.BLUE));
                    poly_lines.add(spouseLine);
                }

            }
            //Check to see if Spouse has an Event. I.E. Birth event
        }
        //Create life story lines
        if(instance.isLifeLines() && DataCache.getInstance().isShowMaleEvents() && DataCache.getInstance().isShowFemaleEvents()) {
            //Get all the events for the person.
            Event event = (Event) marker.getTag();
            Vector<Marker> personMarkers = new Vector<Marker>();
            String personID = event.getPersonID();
            for (Marker m : markers) {
                Event tempEvent = (Event) m.getTag();
                if(tempEvent != null) {
                    if (tempEvent.getPersonID().equals(personID)) {
                        personMarkers.add(m);
                    }
                }
            }
            Marker earliest = null;
            Marker last_used;
            int year = 2500;
            while(!personMarkers.isEmpty()){
                last_used = earliest;
                //Find the earliest
                int i = 0;
                int earliest_index = -1;
                for(Marker m: personMarkers){
                    Event tempEvent = (Event)m.getTag();
                    if(tempEvent.getYear() < year){
                        year = tempEvent.getYear();
                        earliest = m;
                        earliest_index = i;
                    }
                    i++;
                }
                personMarkers.remove(earliest_index);
                if(earliest!=null && last_used != null){
                    Polyline personLine = mMap.addPolyline(new PolylineOptions()
                            .add(earliest.getPosition(), last_used.getPosition())
                            .width(5)
                            .color(Color.BLACK));
                    poly_lines.add(personLine);
                }
                year = 2500;
            }
        }
        //Family Tree
        if(instance.isFamilyLines()){
            int width = 15;
            drawToParents(marker, width);
        }
    }
    private void drawToParents(Marker currentPerson, int width){
        Event event = (Event)currentPerson.getTag();
        DataCache dataCache = DataCache.getInstance();
        PersonsModel currPerson = dataCache.getPersonById(event.getPersonID());
        int year;

        if(currPerson.getMotherID()!= null){
            year = 2100;
            Marker earlyMom = null;
            for(Marker m: markers){
                Event motherEvent = (Event)m.getTag();
                if (motherEvent != null) {
                    if (motherEvent.getPersonID() != null) {
                        if (motherEvent.getPersonID().equals(currPerson.getMotherID())) {
                            if (motherEvent.getYear() < year) {
                                year = motherEvent.getYear();
                                earlyMom = m;
                            }
                        }
                    }
                }
            }
            if(earlyMom != null){
                Polyline momLine = mMap.addPolyline(new PolylineOptions()
                        .add(earlyMom.getPosition(), currentPerson.getPosition())
                        .width(width)
                        .color(Color.YELLOW));
                drawToParents(earlyMom, width /2);
                poly_lines.add(momLine);
            }
        }
        if(currPerson.getFatherID()!=null){
            year = 2100;
            Marker earlyDad= null;
            for(Marker m: markers){
                Event fatherEvent = (Event)m.getTag();
                if(fatherEvent != null) {
                    if (fatherEvent.getPersonID().equals(currPerson.getFatherID())) {
                        if (fatherEvent.getYear() < year) {
                            year = fatherEvent.getYear();
                            earlyDad = m;
                        }
                    }
                }
            }
            if(earlyDad != null){
                Polyline dadLine = mMap.addPolyline(new PolylineOptions()
                        .add(earlyDad.getPosition(), currentPerson.getPosition())
                        .width(width)
                        .color(Color.YELLOW));
                drawToParents(earlyDad, width/2);
                poly_lines.add(dadLine);
            }
        }
    }
}