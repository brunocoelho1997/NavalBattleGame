package com.example.brunocoelho.navalbattle.Game.Models.Ships;

import com.example.brunocoelho.navalbattle.Game.Models.Position;

public class ShipFive extends Ship {
    @Override
    public Position getPointPosition() {
        return positionList.get(1);
    }

    @Override
    public void setPointPosition(Position position) {
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
