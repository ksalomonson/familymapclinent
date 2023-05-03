package com.example.familymaplogin;

import java.util.ArrayList;
import java.util.List;

public class Colors {
    private static Colors colors;

    private final List<String> colorsList;


    private Colors() {
        colorsList = new ArrayList<>();

        colorsList.add("#ff1d18"); //red
        colorsList.add("#3bc3ea"); //blue
        colorsList.add("#a4f161"); //green
        colorsList.add("#ffe049"); // Yellow
        colorsList.add("#51f0c3"); //Cyan
        colorsList.add("#eb78c8"); //Pink
        colorsList.add("#f9903b"); //orange
        colorsList.add("#761999"); // purple
    }

    public static Colors getInstance() {
        if (colors == null) {
            colors = new Colors();
        }
        return colors;
    }
    public List<String> getColorsList(){
        return colorsList;
    }
}
