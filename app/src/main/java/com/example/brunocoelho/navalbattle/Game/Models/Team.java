package com.example.brunocoelho.navalbattle.Game.Models;

import com.example.brunocoelho.navalbattle.Game.Constants;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team implements Serializable{


    private String objectType;
    private List<Ship> ships;
    private ArrayList<Position> firedPositions;
    private boolean positionedShips;


    public Team() {
        this.ships = new ArrayList<>();
        this.firedPositions = new ArrayList<>();
        this.objectType = Constants.CLASS_TEAM; //className
        this.positionedShips = false;

    }

    public List<Ship> getShips() {
        return ships;
    }

    public void setShips(List<Ship> ships) {
        this.ships = ships;
    }

    public ArrayList<Position> getFiredPositions() {
        return firedPositions;
    }

    public void setFiredPositions(ArrayList<Position> firedPositions) {
        this.firedPositions = firedPositions;
    }

    public boolean isPositionedShips() {
        return positionedShips;
    }

    public void setPositionedShips(boolean positionedShips) {
        this.positionedShips = positionedShips;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    @Override
    public String toString() {
        return "Team{" +
                "objectType='" + objectType + '\'' +
                ", ships=" + ships +
                ", firedPositions=" + firedPositions +
                ", positionedShips=" + positionedShips +
                '}';
    }
}
