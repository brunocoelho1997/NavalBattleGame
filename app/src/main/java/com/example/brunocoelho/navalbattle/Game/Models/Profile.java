package com.example.brunocoelho.navalbattle.Game.Models;

import com.example.brunocoelho.navalbattle.Game.Constants;

import java.io.Serializable;

public class Profile implements Serializable{

    private String objectType;
    private String name;
    private boolean selected;

    public Profile(String name) {
        this.objectType = Constants.CLASS_PROFILE; //className

        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "objectType='" + objectType + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
