package com.example.familymaplogin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.List;

import Utilities.DataCache;
import model.Event;
import model.PersonsModel;


public class SearchActivity extends AppCompatActivity {

    private static final int personViewType = 0;
    private static final int EventViewType = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);

        RecyclerView recyclerView = findViewById(R.id.search_recy);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        List<PersonsModel> personsModels = new ArrayList<>();
        List<Event> events = new ArrayList<>();
        events = DataCache.getInstance().getValidEvents();
        DataCache.getInstance().setPersons();
        for(PersonsModel p: DataCache.getInstance().getPersons() ){
            personsModels.add(p);
        }
        SearchAdapter adapter = new SearchAdapter(personsModels, events);
        recyclerView.setAdapter(adapter);

        SearchView search = findViewById(R.id.searchViewWidget);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                SearchAdapter tempAdapt = new SearchAdapter(DataCache.getInstance().searchPersons(query), DataCache.getInstance().searchEvents(query));
                recyclerView.setAdapter(tempAdapt);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String query){
                SearchAdapter tempAdapt = new SearchAdapter(DataCache.getInstance().searchPersons(query), DataCache.getInstance().searchEvents(query));
                recyclerView.setAdapter(tempAdapt);

                return true;
            }
        });

    }

    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {
        private final List<PersonsModel> persons;
        private final List<Event> events;

        SearchAdapter(List<PersonsModel> skiResorts, List<Event> hikingTrails) {
            this.persons = skiResorts;
            this.events = hikingTrails;
        }

        @Override
        public int getItemViewType(int position) {
            return position < persons.size() ? personViewType : EventViewType;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewIndex) {
            View view;

            if(viewIndex == personViewType) {
                view = getLayoutInflater().inflate(R.layout.search_list_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.search_list_item, parent, false);
            }

            return new SearchViewHolder(view, viewIndex);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int index) {
            if(index < persons.size()) {
                holder.bind(persons.get(index));
            } else {
                holder.bind(events.get(index - persons.size()));
            }
        }

        @Override
        public int getItemCount() {
            return persons.size() + events.size();
        }
    }

    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView name;
        private final TextView location;
        private final ImageView imageView;

        private final int viewType;
        private PersonsModel person;
        private Event event;

        SearchViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == personViewType) {
                name = itemView.findViewById(R.id.top_text);
                location = itemView.findViewById(R.id.bottom_text);
                imageView = itemView.findViewById(R.id.imageView);
            } else {
                name = itemView.findViewById(R.id.top_text);
                location = itemView.findViewById(R.id.bottom_text);
                imageView = itemView.findViewById(R.id.imageView);

            }
        }
        //creates rows for persons in search
        private void bind(PersonsModel person) {
            this.person = person;
            name.setText(person.getFirstName() + " " + person.getLastName());
            location.setText("");
            if(person.getGender().equals("f")){
                Drawable genderIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_female).colorRes(R.color.femaleColor).sizeDp(40);
                imageView.setImageDrawable(genderIcon);
            } else {
                Drawable genderIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_male).colorRes(R.color.maleColor).sizeDp(40);
                imageView.setImageDrawable(genderIcon);
            }

        }
        //Do other stuff
        private void bind(Event event) {
            this.event= event;
            name.setText(event.getEventType() + ": " + event.getCity() + ", " + event.getCountry() + "(" + Integer.toString(event.getYear()) + ")");
            PersonsModel tempPerson = DataCache.getInstance().getPersonById(event.getPersonID());
            location.setText(tempPerson.getFirstName() + " " + tempPerson.getLastName());
            Drawable markerIcon = new IconDrawable(getBaseContext(), FontAwesomeIcons.fa_map_marker).colorRes(R.color.maleColor).sizeDp(40);
            imageView.setImageDrawable(markerIcon);
        }
        //do stuff on click
        @Override
        public void onClick(View view) {
            if(viewType == personViewType) {

                Intent intent = new Intent(SearchActivity.this, PersonActivity.class);
                Bundle mBundle  = new Bundle();
                mBundle.putString("personID", person.getPersonID());
                intent.putExtras(mBundle);
                startActivity(intent);
            } else {
                // Anything that isn't a person is an event
                Intent intent = new Intent(SearchActivity.this, EventActivity.class);
                Bundle mBundle = new Bundle();
                DataCache.getInstance().setEventString(event.getEventID());

                mBundle.putString("eventID", event.getEventID());
                intent.putExtras(mBundle);

                startActivity(intent);
            }
        }
    }
    //inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_menu, menu);
        MenuItem personMenuItem = menu.findItem(R.id.back);
        return true;
    }
    //Menu implementation
    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch(menu.getItemId()) {
            case R.id.back:
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_CLEAR_TOP);
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


}
