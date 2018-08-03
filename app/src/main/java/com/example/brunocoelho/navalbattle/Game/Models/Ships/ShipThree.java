package com.example.brunocoelho.navalbattle.Game.Models.Ships;

import com.example.brunocoelho.navalbattle.Game.Models.Position;

public class ShipThree extends Ship {
    public ShipThree() {
    }

    @Override
    public Position getPointPosition() {
        return positionList.get(1);
    }

    @Override
    public void setPointPosition(Position position) {
        positionList.set(1,position);

        Position aux = positionList.get(0);

        if(rotation==0)
        {
            aux.setLetter(position.getLetter() - 1);
            aux.setNumber(position.getNumber());

            aux = positionList.get(2);
            aux.setLetter(position.getLetter() + 1);
            aux.setNumber(position.getNumber());
        }
        else
        {
            aux.setLetter(position.getLetter());
            aux.setNumber(position.getNumber()-1);

            aux = positionList.get(2);
            aux.setLetter(position.getLetter());
            aux.setNumber(position.getNumber()+1);
        }

    }


    @Override
    public void rotate() {
        rotation = rotation==0? 90 : 0;
    }
}
