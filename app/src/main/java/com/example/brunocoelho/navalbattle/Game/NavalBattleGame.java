package com.example.brunocoelho.navalbattle.Game;

import android.util.Log;

import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Ship;

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

    public boolean isValidatedShip(Ship selectedShip) {

        for(Position position : selectedShip.getPositionList())
        {
            //define as bad position... if it isnt in the end of the function is changed
//            position.setColor(Constants.RED_SQUARE);

            if(position.getNumber()>=9 || position.getLetter()>=9)
                return false;
            if(position.getNumber()<=0 || position.getLetter()<=0)
                return false;


            //if there is a ship
            for(Ship otherShip: getTeamA())
            {
                if(!otherShip.equals(selectedShip) && otherShip.getPositionList().contains(position))
                    return false;
            }
        }

//        List<Position> adjancentPositions = getAdjacentPositions(selectedShip);

        return true;
    }

    private List<Position> getAdjacentPositions(Ship selectedShip) {
        List<Position> adjancentPositions = new ArrayList<>();
        Position aux = null;

        for(Position position : selectedShip.getPositionList()){
            aux = new Position(position.getNumber(), position.getLetter());
            adjancentPositions.add(aux);
        }

        if(selectedShip.getPositionList().size()==1)
        {
            adjancentPositions.add(new Position());
        }

        return adjancentPositions;
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
            for(Position position: ship.getPositionList())
            {
                if(selectedShip.getPositionList().contains(position) && !selectedShip.equals(ship))
                    invalidPositions.add(position);
            }
        }
    }
}
