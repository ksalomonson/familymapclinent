package com.example.familymaplogin;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.bignerdranch.expandablerecyclerview.model.Parent;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import Utilities.DataCache;
import model.Event;
import model.PersonsModel;

public class PersonActivity extends AppCompatActivity {
    String selectedPersonID = new String();
    private RecyclerView recyclerView;
    private Adapter adapter;
    private Context here = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        selectedPersonID = getIntent().getExtras().getString("personID");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        TextView firstNameTextView = (TextView) findViewById(R.id.firstNameTitle);
        TextView lastNameTextView = (TextView) findViewById(R.id.lastNameTitle);
        TextView genderTextView = (TextView) findViewById(R.id.genderTitle);
        DataCache dataCache = DataCache.getInstance();
        PersonsModel personSelected = dataCache.getPersonById(selectedPersonID);
        if(personSelected == null){
            //TODO handle exception
        }

        firstNameTextView.setText(personSelected.getFirstName());
        lastNameTextView.setText(personSelected.getLastName());
        if (personSelected.getGender().equals("m")){
            genderTextView.setText("Male");
        } else {
            genderTextView.setText("Female");
        }

        //Recycler view
        recyclerView = (RecyclerView) findViewById(R.id.expandableList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateUI();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_menu, menu);
        MenuItem personMenuItem = menu.findItem(R.id.back);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.back:
                Intent intent = new Intent(here, MapsActivity.class);
                Bundle mBundle  = new Bundle();
                mBundle.putString("personID", DataCache.getInstance().getUserPersonID());
                DataCache.getInstance().setEventString(DataCache.getInstance().getEventsOfPersonByPersonId(DataCache.getInstance().getUserPersonID()).get(0).getEventID());
                intent.putExtras(mBundle);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    void updateUI() {
        DataCache dataCache = DataCache.getInstance();

        List<Event> eventsOfPerson = dataCache.getEventsOfPersonByPersonId(selectedPersonID);
        PersonsModel person = dataCache.getPersonById(selectedPersonID);
        List<DisplayRow> eventRows = new ArrayList<>();
        List<DisplayRow> peopleRows = new ArrayList<>();


        List<PersonsModel> personsChildren = new ArrayList<>(); //could have none
        List<PersonsModel> personsParents = new ArrayList<>(); //could have none
        PersonsModel spouse = null;


        //Beginning people

        Map<String, List<PersonsModel>> childrenMap = dataCache.getChildrenMap();
        if (childrenMap.containsKey(person.getPersonID())){ //person has children
            personsChildren = childrenMap.get(person.getPersonID());
        }

        personsParents.add(dataCache.getPersonById(person.getFatherID()));
        personsParents.add(dataCache.getPersonById(person.getMotherID()));

        spouse = dataCache.getPersonById(person.getSpouseID());

        //Now we should check and see if personParents, personsChildren, and spouse are empty

        if (personsParents.size() != 0 && personsParents.get(0) != null){ //has parents, make display rows
            for (int i = 0; i < personsParents.size(); i++){
                if (personsParents.get(i).getGender().equals("f")){
                    DisplayRow temp = new DisplayRow(personsParents.get(i).getFirstName() + " " + personsParents.get(i).getLastName(),"Mother", "female", personsParents.get(i).getPersonID());
                    peopleRows.add(temp);
                } else {
                    DisplayRow temp = new DisplayRow(personsParents.get(i).getFirstName() + " " + personsParents.get(i).getLastName(),"Father", "male", personsParents.get(i).getPersonID());
                    peopleRows.add(temp);
                }

            }
        }

        if (personsChildren.size() != 0){
            for (int i = 0; i < personsChildren.size(); i++){
                if (personsChildren.get(i).getGender().equals("f")){
                    DisplayRow temp = new DisplayRow(personsChildren.get(i).getFirstName() + " " + personsChildren.get(i).getLastName(), "Daughter", "female", personsChildren.get(i).getPersonID());
                    peopleRows.add(temp);
                } else {
                    DisplayRow temp = new DisplayRow(personsChildren.get(i).getFirstName() + " " + personsChildren.get(i).getLastName(), "Son", "male", personsChildren.get(i).getPersonID());
                    peopleRows.add(temp);
                }
            }
        }
        if(spouse != null) {
            if (spouse.getPersonID() != null && !spouse.getPersonID().equals("")) {
                if (spouse.getGender().equals("f")) {
                    DisplayRow temp = new DisplayRow(spouse.getFirstName() + " " + spouse.getLastName(), "Wife", "female", spouse.getPersonID());
                    peopleRows.add(temp);
                } else {
                    DisplayRow temp = new DisplayRow(spouse.getFirstName() + " " + spouse.getLastName(), "Husband", "Male", spouse.getPersonID());
                    peopleRows.add(temp);
                }
            }
        }


        //Beginning events
        Boolean sorted = false;
        while (!sorted){
            sorted = true;
            for (int i = 0; i < eventsOfPerson.size() - 1; i++) {
                if (eventsOfPerson.get(i).getYear() > eventsOfPerson.get(i + 1).getYear()){
                    Event temp = eventsOfPerson.get(i);
                    eventsOfPerson.set(i, eventsOfPerson.get(i + 1));
                    eventsOfPerson.set(i + 1, temp);
                    sorted = false;
                }
            }
        }

        for (int i = 0; i < eventsOfPerson.size(); i++){
            DisplayRow temp = new DisplayRow(eventsOfPerson.get(i).getEventType(), person.getFirstName() + " " + person.getLastName(), "event", eventsOfPerson.get(i).getEventID());
            eventRows.add(temp);
        }

        Group eventGroup = new Group("LIFE EVENTS", eventRows);
        Group peopleGroup = new Group("FAMILY", peopleRows);

        List<Group> groups = new ArrayList<>();
        groups.add(eventGroup);
        groups.add(peopleGroup);




        adapter = new Adapter(this, groups);
        recyclerView.setAdapter(adapter);

        adapter.setExpandCollapseListener(
                new ExpandableRecyclerAdapter.ExpandCollapseListener() {
                    @Override
                    public void onParentExpanded(int parentPosition) {
                        adapter.expandParent(parentPosition);
                    }
                    @Override
                    public void onParentCollapsed(int parentPosition) {
                        adapter.collapseParent(parentPosition);
                    }
                });

    }

    class Group implements Parent<DisplayRow> {
        String name;
        List<DisplayRow> rows;

        Group(String name, List<DisplayRow> rows){
            this.name = name;
            this.rows = rows;
        }

        @Override
        public List<DisplayRow> getChildList() {

            return rows;
        }
        @Override
        public boolean isInitiallyExpanded() {
            return false;
        }

    }



    class Adapter extends ExpandableRecyclerAdapter<Group, DisplayRow, GroupHolder, Holder> {

        private LayoutInflater inflater;

        public Adapter(Context context, List<Group> groups) {
            super(groups);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public GroupHolder onCreateParentViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.group, viewGroup, false);
            return new GroupHolder(view);
        }

        @Override
        public Holder onCreateChildViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = inflater.inflate(R.layout.display_row, viewGroup, false);
            return new Holder(view);
        }

        @Override
        public void onBindParentViewHolder(@NonNull GroupHolder holder, int i, Group group) {
            holder.bind(group);
        }

        @Override
        public void onBindChildViewHolder(@NonNull Holder holder, int i, int j, DisplayRow item) {
            holder.bind(item);
        }
    }

    class GroupHolder extends ParentViewHolder {

        private TextView textView;

        public GroupHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.groupHolderId);
        }

        void bind(Group group) {
            textView.setText(group.name);
        }

    }

    class Holder extends ChildViewHolder implements View.OnClickListener {

        private TextView textView;
        private TextView textViewTwo;
        private ImageView iconForRow;
        private String rowType;
        private String Id;

        public Holder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.topTextOfRow);
            textViewTwo = (TextView) view.findViewById(R.id.bottomTextOfRow);
            iconForRow = (ImageView) view.findViewById(R.id.rowIcon);
            LinearLayout row = (LinearLayout) view.findViewById(R.id.rowLayout);
            row.setOnClickListener(this);
            rowType = new String();
            Id = new String();
        }

        void bind(DisplayRow row) {
            rowType = row.getType();
            Id = row.getRowId();

            textView.setText(row.getTopRow());
            textViewTwo.setText(row.getBottomRow());
            if (row.getType().equals("event")){
                Drawable eventIcon = new IconDrawable(here, FontAwesomeIcons.fa_map_marker).colorRes(R.color.green).sizeDp(40);
                iconForRow.setImageDrawable(eventIcon);
            } else if (row.getType().equals("female")){
                Drawable genderIcon = new IconDrawable(here, FontAwesomeIcons.fa_female).colorRes(R.color.femaleColor).sizeDp(40);
                iconForRow.setImageDrawable(genderIcon);
            } else {
                Drawable genderIcon = new IconDrawable(here, FontAwesomeIcons.fa_male).colorRes(R.color.maleColor).sizeDp(40);
                iconForRow.setImageDrawable(genderIcon);
            }
        }

        @Override
        public void onClick(View view) {
            if (rowType.equals("event")){

                Intent intent = new Intent(here, EventActivity.class);
                Bundle mBundle = new Bundle();
                DataCache.getInstance().setEventString(Id);

                mBundle.putString("eventID", Id);
                intent.putExtras(mBundle);

                startActivity(intent);

            } else {
                Intent intent = new Intent(here, PersonActivity.class);
                Bundle mBundle  = new Bundle();

                mBundle.putString("personID", Id);
                intent.putExtras(mBundle);
                startActivity(intent);
            }

        }

    }

    public BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

}
