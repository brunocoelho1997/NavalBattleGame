package com.example.brunocoelho.navalbattle.Game.Models;

import java.util.ArrayList;
import java.util.List;

public class Ship {
    private List<Position> positionList;
    private List<Position> hitedPositions;

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

    public Position getPointPosition(){
        //if is the ship 1x1
        if(positionList.size()==1)
            return positionList.get(0);
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

    }
    @Override
    public String toString() {
        return "Ship{" +
                "positionList=" + positionList +
                ", hitedPositions=" + hitedPositions +
                '}';
    }
}
