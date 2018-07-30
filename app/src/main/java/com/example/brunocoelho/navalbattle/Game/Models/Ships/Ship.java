package com.example.brunocoelho.navalbattle.Game.Models.Ships;

import android.util.Log;

import com.example.brunocoelho.navalbattle.Game.Constants;
import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.R;

import java.util.ArrayList;
import java.util.List;

public abstract class Ship {
    protected List<Position> positionList;
    protected List<Position> hitedPositions;

    private List<Position> initialPositionList;

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
        this.positionList=new ArrayList<>();
        this.hitedPositions=new ArrayList<>();
        this.initialPositionList = new ArrayList<>();
    }

    public void setHitedPositions(List<Position> hitedPositions) {
        this.hitedPositions = hitedPositions;
    }

    public List<Position> getInitialPositionList() {
        return initialPositionList;
    }

    public void setInitialPositionList(List<Position> initialPositionList) {
        this.initialPositionList = initialPositionList;
    }

    public Position getPointPosition(){ return null;}

    public void setPointPosition(Position position) { }

    public void restoreInitialPosition()
    {
        for(int i= 0; i<positionList.size(); i++)
        {
            positionList.get(i).setLetter(initialPositionList.get(i).getLetter());
            positionList.get(i).setNumber(initialPositionList.get(i).getNumber());
        }
    }
    @Override
    public String toString() {
        return "Ship{" +
                "positionList=" + positionList +
                ", hitedPositions=" + hitedPositions +
                '}';
    }
}
