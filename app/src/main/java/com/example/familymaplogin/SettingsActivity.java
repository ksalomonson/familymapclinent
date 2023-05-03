package com.example.familymaplogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import Utilities.DataCache;

public class SettingsActivity extends AppCompatActivity {

    Context here = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        RelativeLayout logoutView = findViewById(R.id.logoutView);

        logoutView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getInstance().setLoggedIn(false);
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                startActivity(intent);
            }
        });

        Switch fatherSwitch = findViewById(R.id.switch_father_side);
        fatherSwitch.setChecked(DataCache.getInstance().isShowFatherSide());
        fatherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setFilterIsActive(true);
                DataCache.getInstance().setShowFatherSide(isChecked);
            }
        });

        Switch motherSwitch = findViewById(R.id.switch_mother_side);
        motherSwitch.setChecked(DataCache.getInstance().isShowMotherSide());
        motherSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setFilterIsActive(true);
                DataCache.getInstance().setShowMotherSide(isChecked);
            }
        });

        Switch boysOnlySwitch = findViewById(R.id.switch_male_show);
        boysOnlySwitch.setChecked(DataCache.getInstance().isShowMaleEvents());
        boysOnlySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setFilterIsActive(true);
                DataCache.getInstance().setShowMaleEvents(isChecked);
            }
        });

        Switch girlsOnlySwitch = findViewById(R.id.switch_female_show);
        girlsOnlySwitch.setChecked(DataCache.getInstance().isShowFemaleEvents());
        girlsOnlySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setFilterIsActive(true);
                DataCache.getInstance().setShowFemaleEvents(isChecked);
            }
        });

        Switch drawFamilyLines = findViewById(R.id.switch_Family_tree);
        drawFamilyLines.setChecked(DataCache.getInstance().isFamilyLines());
        drawFamilyLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setFamilyLines(isChecked);
            }
        });

        Switch drawSpouseLines = findViewById(R.id.switch_spouse_lines);
        drawSpouseLines.setChecked(DataCache.getInstance().isSpouseLines());
        drawSpouseLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setSpouseLines(isChecked);
            }
        });
        Switch drawStoryLines = findViewById(R.id.switch_story_lines);
        drawStoryLines.setChecked(DataCache.getInstance().isLifeLines());
        drawStoryLines.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DataCache.getInstance().setLifeLines(isChecked);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.back_menu, menu);
        MenuItem personMenuItem = menu.findItem(R.id.back);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case R.id.back:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle mBundle = new Bundle();
                mBundle.putString("personID", DataCache.getInstance().getUserPersonID());
                DataCache.getInstance().setEventString(DataCache.getInstance().getEventsOfPersonByPersonId(DataCache.getInstance().getUserPersonID()).get(0).getEventID());
                DataCache.getInstance().setValidPersons();
                intent.putExtras(mBundle);
                startActivity(intent);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }
}