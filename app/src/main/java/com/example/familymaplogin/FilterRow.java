package com.example.familymaplogin;

public class FilterRow {
    private String topRow;
    private String bottomRow;

    private String type;

    public FilterRow(String eventName, String type){
        this.type = type;
        if (type.equals("event")){
            this.topRow = eventName + " Events";
            this.bottomRow = "FILTER BY " + eventName.toUpperCase() + " EVENTS";
        } else if (type.equals("side")){
            this.topRow = eventName + " Side";
            this.bottomRow = "FILTER BY " + eventName.toUpperCase() + " SIDE OF FAMILY";
        } else if (type.equals("gender female")){
            this.topRow = eventName + " Events";
            this.bottomRow = "FILTER EVENTS BASED ON GENDER";
        } else {
            this.topRow = eventName + " Events";
            this.bottomRow = "FILTER EVENTS BASED ON GENDER";
        }
    }

    public String getBottomRow() {
        return bottomRow;
    }

    public String getTopRow() {
        return topRow;
    }

    public String getType() {
        return type;
    }
}
