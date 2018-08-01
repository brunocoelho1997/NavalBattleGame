package com.example.brunocoelho.navalbattle.Game;

import android.util.Log;

import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NavalBattleGame implements Serializable{

    private Data data;


    private ArrayList<Position> invalidPositions;

    public NavalBattleGame() {
        this.data = new Data();invalidPositions = new ArrayList<>();
    }

    public Ship getShip(Position position)
    {
        for(Ship ship: data.getTeamA())
        {
            if(ship.getPositionList().contains(position))
                return ship;

        }
        for(Ship ship: data.getTeamB())
        {
            if(ship.getPositionList().contains(position))
                return ship;
        }
        return null;
    }

    public List<Ship> getTeamA() {
        return data.getTeamA();
    }

    public void setTeamA(List<Ship> teamA) {
        data.setTeamA(teamA);
    }

    public List<Ship> getTeamB() {
        return data.getTeamB();
    }

    public void setTeamB(List<Ship> teamB) {
        data.setTeamB(teamB);
    }

    public boolean isInsideView(Ship selectedShip) {

        for(Position position : selectedShip.getPositionList()) {
            if (position.getNumber() >= 9 || position.getLetter() >= 9)
                return false;
            if (position.getNumber() <= 0 || position.getLetter() <= 0)
                return false;
        }

        return true;
    }

    public boolean isValidatedShip(Ship selectedShip) {

        if(!isInsideView(selectedShip))
            return false;

        for(Position position : selectedShip.getPositionList())
        {
            //if there is a ship
            for(Ship otherShip: getTeamA())
            {
                if(!otherShip.equals(selectedShip) && otherShip.getPositionList().contains(position))
                    return false;
            }
        }

        return true;
    }

    public ArrayList<Position> getInvalidPositions() {
        return invalidPositions;
    }

    public void setInvalidPositions(ArrayList<Position> invalidPositions) {
        this.invalidPositions = invalidPositions;
    }

    public void refreshInvalidPositions(Ship selectedShip) {

        invalidPositions = new ArrayList<>();


        for(Ship ship : getTeamA())
        {

            //colision with another ship
            for(Position position: ship.getPositionList())
            {
                //if exist a ship that constains this position and isnt't the selected one...
                if(selectedShip.getPositionList().contains(position) && !selectedShip.equals(ship))
                    invalidPositions.add(position);

            }

            //adjacent positions
            for(Position position: ship.getAdjacentPositions())
            {
                //if exist a ship that constains this position and isnt't the selected one...
                if(selectedShip.getPositionList().contains(position) && !selectedShip.equals(ship))
                    invalidPositions.add(position);

            }
        }
    }
}
