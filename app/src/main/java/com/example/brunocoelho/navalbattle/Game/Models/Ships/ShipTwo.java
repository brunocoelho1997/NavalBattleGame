package com.example.brunocoelho.navalbattle.Game.Models.Ships;

import com.example.brunocoelho.navalbattle.Game.Models.Position;

public class ShipTwo extends Ship {

    public ShipTwo() {
    }

    @Override
    public Position getPointPosition() {
        return positionList.get(0);
    }

    //set a point and define the others positions depeding on the type of the ship
    @Override
    public void setPointPosition(Position position) {
        positionList.set(0,position);
        Position aux = positionList.get(1);

        if(rotation==0)
        {
            aux.setLetter(position.getLetter() + 1);
            aux.setNumber(position.getNumber());
        }
        else
        {
            aux.setNumber(position.getNumber()-1);
            aux.setLetter(position.getLetter());
        }
    }

    @Override
    public void rotate() {
        rotation = rotation==0? 90 : 0;
    }
}
