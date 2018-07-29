package com.example.brunocoelho.navalbattle.Game.Models;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    List<Position> positionList;
    List<Position> hitedPositions;

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public List<Position> getHitedPositions() {
        return hitedPositions;
    }

    public Ship() {
        positionList=new ArrayList<>();
        hitedPositions=new ArrayList<>();
    }

    public void setHitedPositions(List<Position> hitedPositions) {
        this.hitedPositions = hitedPositions;
    }
}
