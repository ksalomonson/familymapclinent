package com.example.familymaplogin;

public class DisplayRow {
    String topRow;
    String bottomRow;
    String type;
    String rowId;

    public DisplayRow(String topRow, String bottomRow, String type, String Id){
        this.topRow = topRow;
        this.bottomRow = bottomRow;
        this.type = type;
        this.rowId = Id;
    }

    public String getTopRow(){
        return topRow;
    }

    public String getBottomRow(){
        return bottomRow;
    }

    public String getType(){
        return type;
    }

    public String getRowId() {
        return rowId;
    }
}
