package com.example.brunocoelho.navalbattle.Game.Models.Ships;

import com.example.brunocoelho.navalbattle.Game.Models.Position;

public class ShipTwo extends Ship {

    @Override
    public Position getPointPosition() {
        return positionList.get(0);
    }

    @Override
    public void setPointPosition(Position position) {
        positionList.set(0,position);
        Position aux = positionList.get(1);
        aux.setLetter(position.getLetter() + 1);
        aux.setNumber(position.getNumber());
    }

}
