package com.example.brunocoelho.navalbattle.Game.Models.Ships;

import com.example.brunocoelho.navalbattle.Game.Models.Position;

public class ShipOne extends Ship {

    @Override
    public Position getPointPosition() {
        return positionList.get(0);
    }

    @Override
    public void setPointPosition(Position position) {
        positionList.set(0,position);
    }
}
