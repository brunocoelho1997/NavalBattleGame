package com.example.brunocoelho.navalbattle.Game.Models;

import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team implements Serializable{

    private List<Ship> ships;
    private ArrayList<Position> firedPositions;

    public Team() {
        this.ships = new ArrayList<>();
        this.firedPositions = new ArrayList<>();
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

}
