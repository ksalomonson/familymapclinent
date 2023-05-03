package Utilities;

import android.app.Person;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import model.*;

public class DataCache {
    private static DataCache instance = new DataCache();

    public static DataCache getInstance(){
        return instance;
    }

    public DataCache(){
        people = new HashMap<>();
        events = new HashMap<>();
        personEvents = new HashMap<>();
        paternalAncestors = new HashSet<>();
        maternalAncestors = new HashSet<>();
        authTokenModel = new AuthTokenModel(null, null);
        authTokenString = new String();
        childrenMap = new HashMap<>();

    }
    private AuthTokenModel authTokenModel;
    private Map<String, PersonsModel[]> people;
    private Map<String, Event> events;
    private Map<String, Event[]> personEvents;
    private Set<PersonsModel> paternalAncestors;       //replace string with the PersonID
    private Set<PersonsModel> maternalAncestors;
    private String authTokenString;
    private List<String> finalListOfEventTypes;
    private Boolean loggedIn = Boolean.FALSE;
    private String userPersonID;
    private boolean showFemaleEvents = true;
    private boolean showMaleEvents = true;
    private boolean spouseLines = true;
    private boolean familyLines = true;
    private boolean lifeLines = true;

    public boolean isSpouseLines() {
        return spouseLines;
    }

    public boolean isFamilyLines() {
        return familyLines;
    }

    public boolean isLifeLines() {
        return lifeLines;
    }

    private String eventString = new String();
    private List<PersonsModel> validPerson = new ArrayList<>();
    private Map<String, List<PersonsModel>> childrenMap;

    private List<Event> validEvents = new ArrayList<>();

    private boolean filterIsActive = false;
    private boolean showMotherSide = true;
    private boolean showFatherSide = true;
    private PersonsModel[] persons;
    public void setPersons(){
        persons = (people.get(authTokenModel.getUsername()));

    }
    public PersonsModel[] getPersons(){
        return persons;
    }
    public String getUserPersonID() {
        return userPersonID;
    }

    public void setUserPersonID(String userPersonID) {
        this.userPersonID = userPersonID;
    }

    public Map<String, PersonsModel[]> getPeople() {
        return people;
    }

    public void setPeople(Map<String, PersonsModel[]> people) {
        this.people = people;
        validPerson.addAll(Arrays.asList(people.get(authTokenModel.getUsername())));
        createPaternalAndMaternalSets();
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(Map<String, Event> events) {
        this.events = events;
        for (Event event : events.values()) {
            validEvents.add(event);
        }
    }

    public Map<String, Event[]> getPersonEvents() {
        return personEvents;
    }

    public void setPersonEvents(Map<String, Event[]> personEvents) {
        this.personEvents = personEvents;
    }

    public Set<PersonsModel> getPaternalAncestors() {
        return paternalAncestors;
    }

    public void setPaternalAncestors(Set<PersonsModel> paternalAncestors) {
        this.paternalAncestors = paternalAncestors;
    }

    public Set<PersonsModel> getMaternalAncestors() {
        return maternalAncestors;
    }

    public void setMaternalAncestors(Set<PersonsModel> maternalAncestors) {
        this.maternalAncestors = maternalAncestors;
    }

    public AuthTokenModel getAuthTokenModel() {
        return authTokenModel;
    }

    public void setAuthTokenModel(AuthTokenModel authTokenModel) {
        this.authTokenModel = authTokenModel;
    }

    public String getAuthTokenString() {
        return authTokenString;
    }

    public void setAuthTokenString(String authTokenString) {
        this.authTokenString = authTokenString;
    }

    public Boolean getLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public void setAllEventTypes(List<String> finalListOfEventTypes) {
        this.finalListOfEventTypes = finalListOfEventTypes;
    }

    public List<String> getFinalListOfEventTypes() {
        return finalListOfEventTypes;
    }

    public List<Event> getEventsOfPersonByPersonId(String selectedPersonID) {
        PersonsModel personSelected = getPersonById(selectedPersonID);
        List<Event> specificPersonEvents = new ArrayList<>();

        for (Event event : events.values()) {
            if(event.getPersonID().equals(selectedPersonID)){
                specificPersonEvents.add(event);
            }
        }

        return specificPersonEvents;
    }

    public PersonsModel getPersonById(String selectedPersonID) {
        PersonsModel personSelected = null;
        for (int i = 0; i < Objects.requireNonNull(people.get(authTokenModel.getUsername())).length; i++) {
            if (Objects.equals(Objects.requireNonNull(people.get(authTokenModel.getUsername()))[i].getPersonID(), selectedPersonID)){
                personSelected = Objects.requireNonNull(people.get(authTokenModel.getUsername()))[i];
            }
        }
        return personSelected;
    }

    public Event getEventById(String centeredID) {
        Event selectedEvent = null;

        for (int i = 0; i < personEvents.get(authTokenModel.getUsername()).length; i++) {
            if(personEvents.get(authTokenModel.getUsername())[i].getEventID() == centeredID){
                selectedEvent = personEvents.get(authTokenModel.getUsername())[i];
            }
        }
        return selectedEvent;
    }

    public boolean isShowFemaleEvents() {
        return showFemaleEvents;
    }

    public void setShowFemaleEvents(boolean showFemaleEvents) {
        this.showFemaleEvents = showFemaleEvents;
    }

    public boolean isShowMaleEvents() {
        return showMaleEvents;
    }

    public void setShowMaleEvents(boolean showMaleEvents) {
        this.showMaleEvents = showMaleEvents;
    }

    public Map<String, List<PersonsModel>> getChildrenMap() {
        return childrenMap;
    }

    public void setChildrenMap(Map<String, List<PersonsModel>> childrenMap) {
        this.childrenMap = childrenMap;
    }

    public void createPaternalAndMaternalSets(){
        PersonsModel motherOfUser = getPersonById(getPersonById(getUserPersonID()).getMotherID());
        PersonsModel fatherOfUser = getPersonById(getPersonById(getUserPersonID()).getFatherID());


        addParentsToOneSetOrTheOther(motherOfUser,true);
        addParentsToOneSetOrTheOther(fatherOfUser,false);


        for (int i = 0; i < people.get(authTokenModel.getUsername()).length; i++) {
            String possibleParentPersonID = people.get(authTokenModel.getUsername())[i].getPersonID();
            List<PersonsModel> childrenOfPossible = new ArrayList<>();

            for(int j = 0; j < people.get(authTokenModel.getUsername()).length; j++){
                PersonsModel entryTwo = people.get(authTokenModel.getUsername())[j];
                if (possibleParentPersonID.equals(entryTwo.getFatherID()) || possibleParentPersonID.equals(entryTwo.getMotherID())){
                    childrenOfPossible.add(entryTwo);
                }
            }
            childrenMap.put(possibleParentPersonID, childrenOfPossible);
        }
    }

    private void addParentsToOneSetOrTheOther(PersonsModel personReceived, Boolean isMaternalSide){
        if (isMaternalSide){
            maternalAncestors.add(personReceived);
        } else {
            paternalAncestors.add(personReceived);
        }

        if (personReceived.getFatherID() == null || personReceived.getFatherID().equals("")){ //baseCase
            return;
        } else {
            if (isMaternalSide){
                PersonsModel motherOfPersonReceived = getPersonById(personReceived.getMotherID());
                PersonsModel fatherOfPersonReceived = getPersonById(personReceived.getFatherID());
                addParentsToOneSetOrTheOther(motherOfPersonReceived, true); //true indicates this person belongs to maternal set
                addParentsToOneSetOrTheOther(fatherOfPersonReceived, true);
            } else {
                PersonsModel motherOfPersonReceived = getPersonById(personReceived.getMotherID());
                PersonsModel fatherOfPersonReceived =getPersonById(personReceived.getFatherID());
                addParentsToOneSetOrTheOther(motherOfPersonReceived,false);
                addParentsToOneSetOrTheOther(fatherOfPersonReceived,false);
            }
        }
    }

    public String getEventString() {
        return eventString;
    }

    public void setEventString(String eventString) {
        this.eventString = eventString;
    }

    public void setValidPersons(){
        validPerson.clear();
        validEvents.clear();
        if(showMotherSide){
            if(!showMaleEvents){
                Iterator iterator = maternalAncestors.iterator();
                while (iterator.hasNext()) {
                    PersonsModel tempPerson = (PersonsModel) iterator.next();
                    if(tempPerson.getGender().toLowerCase().equals("f")){
                        validPerson.add(tempPerson);
                        validEvents.addAll(getEventsOfPersonByPersonId(tempPerson.getPersonID()));
                    }
                }
            }
            else if(!showFemaleEvents){
                Iterator iterator = maternalAncestors.iterator();
                while (iterator.hasNext()) {
                    PersonsModel tempPerson = (PersonsModel) iterator.next();
                    if(tempPerson.getGender().toLowerCase().equals("m")){
                        validPerson.add(tempPerson);
                        validEvents.addAll(getEventsOfPersonByPersonId(tempPerson.getPersonID()));
                    }
                }
            } else{
                validPerson.addAll(maternalAncestors);
                for (int i = 0; i < validPerson.size(); i++) {
                    validEvents.addAll(getEventsOfPersonByPersonId(validPerson.get(i).getPersonID()));
                }
            }
        }
        else if(showFatherSide){
            if(!showMaleEvents){
                Iterator iterator = paternalAncestors.iterator();
                while (iterator.hasNext()) {
                    PersonsModel tempPerson = (PersonsModel) iterator.next();
                    if(tempPerson.getGender().toLowerCase().equals("f")){
                        validPerson.add(tempPerson);
                        validEvents.addAll(getEventsOfPersonByPersonId(tempPerson.getPersonID()));
                    }
                }
            }
            else if(!showFemaleEvents){
                Iterator iterator = paternalAncestors.iterator();
                while (iterator.hasNext()) {
                    PersonsModel tempPerson = (PersonsModel) iterator.next();
                    if(tempPerson.getGender().toLowerCase().equals("m")){
                        validPerson.add(tempPerson);
                        validEvents.addAll(getEventsOfPersonByPersonId(tempPerson.getPersonID()));
                    }
                }
            } else{
                validPerson.addAll(paternalAncestors);
                for (int i = 0; i < validPerson.size(); i++) {
                    validEvents.addAll(getEventsOfPersonByPersonId(validPerson.get(i).getPersonID()));
                }
            }
        }
        else if(showFemaleEvents){
            for (Event event : events.values()) {
                if(getPersonById(event.getPersonID()).getGender().toLowerCase().equals("f")){
                    validPerson.add(getPersonById(event.getPersonID()));
                    validEvents.add(event);
                }
            }
        }
        else if(showMaleEvents){
            for (Event event : events.values()) {
                if(getPersonById(event.getPersonID()).getGender().toLowerCase().equals("m")){
                    validPerson.add(getPersonById(event.getPersonID()));
                    validEvents.add(event);
                }
            }
        }
    }

    public void setFinalListOfEventTypes(List<String> finalListOfEventTypes) {
        this.finalListOfEventTypes = finalListOfEventTypes;
    }

    public List<PersonsModel> getValidPerson() {
        return validPerson;
    }

    public void setValidPerson(List<PersonsModel> validPerson) {
        this.validPerson = validPerson;
    }

    public List<Event> getValidEvents() {
        return validEvents;
    }

    public void setValidEvents(List<Event> validEvents) {
        this.validEvents = validEvents;
    }

    public boolean isFilterIsActive() {
        return filterIsActive;
    }

    public void setFilterIsActive(boolean filterIsActive) {
        this.filterIsActive = filterIsActive;
    }

    public boolean isShowMotherSide() {
        return showMotherSide;
    }

    public void setShowMotherSide(boolean showMotherSide) {
        this.showMotherSide = showMotherSide;
    }

    public boolean isShowFatherSide() {
        return showFatherSide;
    }

    public void setShowFatherSide(boolean showFatherSide) {
        this.showFatherSide = showFatherSide;
    }
    public ArrayList<PersonsModel> searchPersons(String query){
        query = query.toLowerCase();
        ArrayList<PersonsModel> searchResults = new ArrayList<>();
        for(PersonsModel p: validPerson){
            //Check First name
            if(p.getFirstName().toLowerCase().contains(query)){
                searchResults.add(p);
            }
            //Check Last name
            else if(p.getLastName().toLowerCase().contains(query)){
                searchResults.add(p);
            }
        }
        return searchResults;
    }
    public ArrayList<Event> searchEvents(String query){
        query = query.toLowerCase();
        ArrayList<Event> searchResults = new ArrayList<>();
        for(Event e: validEvents){
            if(e.getEventType().toLowerCase().contains(query)){
                searchResults.add(e);
            }
            else if(e.getCountry().toLowerCase().contains(query)){
                searchResults.add(e);
            }
            else if(e.getCity().toLowerCase().contains(query)){
                searchResults.add(e);
            }
            else if(Integer.toString(e.getYear()).toLowerCase().contains(query)){
                searchResults.add(e);
            }
        }
        return searchResults;
    }

    public void setSpouseLines(boolean spouseLines) {
        this.spouseLines = spouseLines;
    }

    public void setFamilyLines(boolean familyLines) {
        this.familyLines = familyLines;
    }

    public void setLifeLines(boolean lifeLines) {
        this.lifeLines = lifeLines;
    }
}
