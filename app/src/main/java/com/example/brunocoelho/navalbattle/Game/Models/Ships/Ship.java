package com.example.brunocoelho.navalbattle.Game.Models.Ships;

import android.util.Log;

import com.example.brunocoelho.navalbattle.Game.Constants;
import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.R;

import java.util.ArrayList;
import java.util.List;

public abstract class Ship {
    protected int rotation;
    protected List<Position> positionList;

    private List<Position> initialPositionList;

    public List<Position> getPositionList() {
        return positionList;
    }

    public void setPositionList(List<Position> positionList) {
        this.positionList = positionList;
    }

    public Ship() {
        this.rotation = 0;
        this.positionList=new ArrayList<>();
        this.initialPositionList = new ArrayList<>();
    }

    public List<Position> getInitialPositionList() {
        return initialPositionList;
    }

    public void setInitialPositionList(List<Position> initialPositionList) {
        this.initialPositionList = initialPositionList;
    }

    public Position getPointPosition(){ return null;}

    public void setPointPosition(Position position) { }

    public List<Position> getAdjacentPositions()
    {
        List<Position> adjacentPositions = new ArrayList<>();

        List<Position> finalAdjacentPositions = new ArrayList<>();

        for(Position position: positionList)
        {
            adjacentPositions.add(new Position(position.getNumber()-1, position.getLetter()));
            adjacentPositions.add(new Position(position.getNumber()-1, position.getLetter()+1));
            adjacentPositions.add(new Position(position.getNumber()-1, position.getLetter()-1));
            adjacentPositions.add(new Position(position.getNumber(), position.getLetter()+1));
            adjacentPositions.add(new Position(position.getNumber(), position.getLetter()-1));
            adjacentPositions.add(new Position(position.getNumber()+1, position.getLetter()+1));
            adjacentPositions.add(new Position(position.getNumber()+1, position.getLetter()));
            adjacentPositions.add(new Position(position.getNumber()+1, position.getLetter()-1));
        }

        for(Position position: adjacentPositions)
        {
            if(position.getNumber()>0 && position.getLetter()>0 && position.getNumber()<9 && position.getLetter()<9)
                finalAdjacentPositions.add(position);

//            if(!positionList.contains(position))
//                finalAdjacentPositions.add(position);
        }

        return finalAdjacentPositions;
    }

    public void restoreInitialPosition()
    {
        for(int i= 0; i<positionList.size(); i++)
        {
            positionList.get(i).setLetter(initialPositionList.get(i).getLetter());
            positionList.get(i).setNumber(initialPositionList.get(i).getNumber());
        }
    }

    public void rotate(){}

    @Override
    public String toString() {
        return "Ship{" +
                "rotation=" + rotation +
                ", positionList=" + positionList +
                ", initialPositionList=" + initialPositionList +
                '}';
    }
}
