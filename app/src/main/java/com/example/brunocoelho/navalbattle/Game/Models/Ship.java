package com.example.brunocoelho.navalbattle.Game.Models;

import android.util.Log;

import com.example.brunocoelho.navalbattle.Game.Constants;
import com.example.brunocoelho.navalbattle.R;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private String color;
    private List<Position> positionList;
    private List<Position> hitedPositions;

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
        this.color = Constants.SHIP_DEFAULT_COLOR;
    }

    public void setHitedPositions(List<Position> hitedPositions) {
        this.hitedPositions = hitedPositions;
    }

    public List<Position> getInitialPositionList() {
        return initialPositionList;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setInitialPositionList(List<Position> initialPositionList) {
        this.initialPositionList = initialPositionList;
    }

    public Position getPointPosition(){
        //if is the ship 1x1
        if(positionList.size()==1)
            return positionList.get(0);
        else if(positionList.size()==2)
            return positionList.get(0);
        else if(positionList.size()==3)
            return positionList.get(1);
        else if(positionList.size()==5)
            return positionList.get(1);


        return null;
    }
    public void setPointPosition(Position position)
    {
        if(positionList.size() ==1)
        {
            positionList.set(0,position);
        }
        else if(positionList.size() == 2){
            positionList.set(0,position);
            Position aux = positionList.get(1);
            aux.setLetter(position.getLetter() + 1);
            aux.setNumber(position.getNumber());

        }
        else if(positionList.size() == 3){
            positionList.set(1,position);

            Position aux = positionList.get(0);
            aux.setLetter(position.getLetter() - 1);
            aux.setNumber(position.getNumber());

            aux = positionList.get(2);
            aux.setLetter(position.getLetter() + 1);
            aux.setNumber(position.getNumber());
        }
        else if(positionList.size() == 5){
            positionList.set(1,position);

            Position aux = positionList.get(0);
            aux.setLetter(position.getLetter() - 1);
            aux.setNumber(position.getNumber());

            aux = positionList.get(2);
            aux.setLetter(position.getLetter() + 1);
            aux.setNumber(position.getNumber());

            aux = positionList.get(3);
            aux.setNumber(position.getNumber() + 1);
            aux.setLetter(position.getLetter());

            aux = positionList.get(4);
            aux.setNumber(position.getNumber() + 2);
            aux.setLetter(position.getLetter());
        }

        Log.d("initialPositionList", initialPositionList.toString());

    }

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
