package com.example.brunocoelho.navalbattle.Game.Models.Ships;

import com.example.brunocoelho.navalbattle.Game.Models.Position;

public class ShipFive extends Ship {
    public ShipFive() {
    }

    @Override
    public Position getPointPosition() {
        return positionList.get(1);
    }

    @Override
    public void setPointPosition(Position position) {
        positionList.set(1,position);

        Position aux;

        if(rotation==0)
        {
            aux = positionList.get(0);
            aux.setNumber(position.getNumber());
            aux.setLetter(position.getLetter() - 1);

            aux = positionList.get(2);
            aux.setNumber(position.getNumber());
            aux.setLetter(position.getLetter() + 1);

            aux = positionList.get(3);
            aux.setNumber(position.getNumber() + 1);
            aux.setLetter(position.getLetter());

            aux = positionList.get(4);
            aux.setNumber(position.getNumber() + 2);
            aux.setLetter(position.getLetter());
        }
        else if(rotation==90)
        {
            aux = positionList.get(0);
            aux.setNumber(position.getNumber() -1);
            aux.setLetter(position.getLetter());

            aux = positionList.get(2);
            aux.setNumber(position.getNumber() +1);
            aux.setLetter(position.getLetter());


            aux = positionList.get(3);
            aux.setNumber(position.getNumber() );
            aux.setLetter(position.getLetter() - 1);

            aux = positionList.get(4);
            aux.setNumber(position.getNumber() );
            aux.setLetter(position.getLetter() - 2);
        }else if(rotation==180)
        {
            aux = positionList.get(0);
            aux.setNumber(position.getNumber());
            aux.setLetter(position.getLetter() - 1);

            aux = positionList.get(2);
            aux.setNumber(position.getNumber());
            aux.setLetter(position.getLetter() + 1);

            aux = positionList.get(3);
            aux.setNumber(position.getNumber() - 1);
            aux.setLetter(position.getLetter());

            aux = positionList.get(4);
            aux.setNumber(position.getNumber() - 2);
            aux.setLetter(position.getLetter());
        }
        else if(rotation == 270)
        {
            aux = positionList.get(0);
            aux.setNumber(position.getNumber()+1);
            aux.setLetter(position.getLetter());

            aux = positionList.get(2);
            aux.setNumber(position.getNumber() -1);
            aux.setLetter(position.getLetter());

            aux = positionList.get(3);
            aux.setNumber(position.getNumber());
            aux.setLetter(position.getLetter() + 1);

            aux = positionList.get(4);
            aux.setNumber(position.getNumber());
            aux.setLetter(position.getLetter() + 2);
        }

    }

    @Override
    public void rotate() {
        rotation+=90;
        rotation = rotation==360? 0 : rotation;
    }
}
