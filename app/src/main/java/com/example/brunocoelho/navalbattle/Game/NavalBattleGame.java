package com.example.brunocoelho.navalbattle.Game;

import android.util.Log;

import com.example.brunocoelho.navalbattle.Game.Models.Position;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.Ship;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipFive;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipOne;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipThree;
import com.example.brunocoelho.navalbattle.Game.Models.Ships.ShipTwo;
import com.example.brunocoelho.navalbattle.Game.Models.Team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NavalBattleGame implements Serializable{

    private Data data;


    private ArrayList<Position> invalidPositions;
    private ArrayList<Position> firedPositionsTemp;


    public NavalBattleGame() {
        this.data = new Data();
        invalidPositions = new ArrayList<>();
        this.firedPositionsTemp = new ArrayList<>();
    }

    public Ship getShip(Position position)
    {

        Team team;

        if(isAmITeamA())
            team = getTeamA();
        else
            team = getTeamB();

        for(Ship ship : team.getShips())
        {
            if(ship.getPositionList().contains(position))
                return ship;

        }
        return null;
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




//    public boolean isValidatedShip(Ship selectedShip) {
//
//        if(!isInsideView(selectedShip))
//            return false;
//
////        for(Position position : selectedShip.getPositionList())
////        {
////            //if there is a ship
////            for(Ship otherShip: getTeamA())
////            {
////                if(!otherShip.equals(selectedShip) && otherShip.getPositionList().contains(position))
////                    return false;
////            }
////        }
//
//        return true;
//    }


    public boolean existInvalidPositions()
    {
        return invalidPositions.size()!=0;
    }
    public void refreshInvalidPositions(Ship selectedShip) {

        invalidPositions = new ArrayList<>();

        Team team;

        if(isAmITeamA())
            team = getTeamA();
        else
            team = getTeamB();

        for(Ship ship : team.getShips())
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



    public void setAIPositions() {


    }



    private Ship createShip(List<Position> positionList)
    {

        Ship ship = null;

        if(positionList.size() ==1)
            ship = new ShipOne();
        else if(positionList.size() == 2)
            ship = new ShipTwo();
        else if(positionList.size() == 3)
            ship = new ShipThree();
        else if(positionList.size() == 5)
            ship = new ShipFive();


        ship.setPositionList(positionList);
        for(Position position : ship.getPositionList())
        {
            Position p = new Position(position.getNumber(), position.getLetter());
            ship.getInitialPositionList().add(p);
        }

        return ship;
    }

    public void setTeamsPositions() {
        List<Position> positionList = new ArrayList<>();
        List<Ship> teamA = data.getTeamA().getShips();
        List<Ship> teamB = data.getTeamB().getShips();

        Ship ship;

        //2x1
        positionList = new ArrayList<>();
        positionList.add(new Position(1,1));
        teamA.add(createShip(positionList));
        teamB.add(createShip(positionList));


        positionList = new ArrayList<>();
        positionList.add(new Position(1,3));
        teamA.add(createShip(positionList));
        teamB.add(createShip(positionList));

        //2x2
        positionList = new ArrayList<>();
        positionList.add(new Position(1,5));
        positionList.add(new Position(1,6));
        teamA.add(createShip(positionList));
        teamB.add(createShip(positionList));

        positionList = new ArrayList<>();
        positionList.add(new Position(3,2));
        positionList.add(new Position(3,3));
        teamA.add(createShip(positionList));
//        teamB.add(createShip(positionList));

        //2x3
        positionList = new ArrayList<>();
        positionList.add(new Position(3,5));
        positionList.add(new Position(3,6));
        positionList.add(new Position(3,7));
        teamA.add(createShip(positionList));
        teamB.add(createShip(positionList));
        positionList = new ArrayList<>();
        positionList.add(new Position(5,1));
        positionList.add(new Position(5,2));
        positionList.add(new Position(5,3));
        teamA.add(createShip(positionList));
//        teamB.add(createShip(positionList));

        //1x5 T
        positionList = new ArrayList<>();
        positionList.add(new Position(5,5));
        positionList.add(new Position(5,6));
        positionList.add(new Position(5,7));
        positionList.add(new Position(6,6));
        positionList.add(new Position(7,6));
        teamA.add(createShip(positionList));
//        teamB.add(createShip(positionList));
    }

    public void addFiredPosition(Position onDownPosition) {

        //if the position clicked is inside table...
        if (onDownPosition.getNumber() <= 9 && onDownPosition.getLetter() <= 9 && onDownPosition.getLetter()>=1 && onDownPosition.getNumber()>=1)
        {
            //if is teamA and is his turn or if is teamB and is his turn to play
            if(isAmITeamA() && isTeamATurn() || !isAmITeamA() && !isTeamATurn())
            {
                //verify if the user already not fired to this position
                if(!firedPositionsTemp.contains(onDownPosition) && !getAtualTeam().getFiredPositions().contains(onDownPosition))
                    firedPositionsTemp.add(onDownPosition);
            }
        }
    }

    public Team getAtualTeam()
    {
        if(data.isTeamATurn())
            return data.getTeamA();
        else
            return data.getTeamB();
    }
    public Team getOpositeTeam()
    {
        if(data.isTeamATurn())
            return data.getTeamB();
        else
            return data.getTeamA();
    }


    public void verifyFiredPosition() {

        Team team = getAtualTeam();

        if(firedPositionsTemp.size()==3)
        {
            //add as fired positions...
            team.getFiredPositions().addAll(firedPositionsTemp);

            //if shots hit teamA change icon to cross black.... if not change to cross
            for(Position position : firedPositionsTemp)
            {
                for(Ship ship : getOpositeTeam().getShips()) {
                    if(ship.getPositionList().contains(position))
                    {

                        if(isAmITeamA())
                            Log.d("verifyFiredPosition","TeamA hited in position: " + position);
                        else
                            Log.d("verifyFiredPosition","TeamB hited in position: " + position);

                        position.setColor(Constants.BLACK_CROSS_SQUARE);
                        break;
                    }
                    else
                    {
                        position.setColor(Constants.CROSS_SQUARE);
                    }
                }

                //verify if the atual player didn't win
                if(verifyEndOfGame())
                {
                    if(isTeamATurn())
                        Log.d("verifyFiredPosition","TeamA won!!!");
                    else
                        Log.d("verifyFiredPosition","TeamB won!!!");
                }

                if(isTeamATurn())
                    Log.d("verifyFiredPosition","TeamA missed in position: " + position);
                else
                    Log.d("verifyFiredPosition","TeamB missed in position: " + position);

            }
        }
    }

    private boolean verifyEndOfGame() {

        Team team = getOpositeTeam();

        for(Ship ship : team.getShips())
        {
            for(Position position : ship.getPositionList())
                if(position.getColor() == Constants.FULL_SQUARE)
                    return false;
        }
        return true;
    }

    public void AIFire() {

        Random rand = new Random();
        int min = 1; int max = 8;

        //while is team B turn to play... since AI is TeamB
        while(firedPositionsTemp.size()!=3)
        {
            firedPositionsTemp.add(new Position(rand.nextInt((max - min) + 1) + min,rand.nextInt((max - min) + 1) + min ));

            Log.d("AIFire","AI Fired!!!!! Positions:" + firedPositionsTemp.toString());

            verifyFiredPosition();
        }
    }

    public void nextTurn() {
        getFiredPositionsTemp().clear();

        if(isTeamATurn())
            setTeamATurn(false);
        else
            setTeamATurn(true);

        Log.d("nextTurn","isTeamATurn: " + isTeamATurn());
    }

    public boolean isAvaibleNextTurn() {
        return firedPositionsTemp.size()==3;
    }
    public Team getTeamA() {
        if(isTeamATurn())
            return getAtualTeam();
        else
            return getOpositeTeam();
    }

    public Team getTeamB() {
        if(!isTeamATurn())
            return getAtualTeam();
        else
            return getOpositeTeam();
    }
//    -
//    -
//    -
//    getters and setters
//    -
//    -
//    -
//
    public ArrayList<Position> getFiredPositionsTemp() {
        return firedPositionsTemp;
    }

    public void setFiredPositionsTemp(ArrayList<Position> firedPositionsTemp) {
        this.firedPositionsTemp = firedPositionsTemp;
    }

    public boolean isStarted()
    {
        return data.isStarted();
    }
    public void startGame()
    {
        data.setStarted(true);
    }
    public void endGame()
    {
        data.setStarted(false);
    }
    public ArrayList<Position> getInvalidPositions() {
        return invalidPositions;
    }

    public void setInvalidPositions(ArrayList<Position> invalidPositions) {
        this.invalidPositions = invalidPositions;
    }
    public boolean isTwoPlayer() {
        return data.isTwoPlayer();
    }

    public void setTwoPlayer(boolean twoPlayer) {
        data.setTwoPlayer(twoPlayer);
    }

    public boolean isTeamATurn() {
        return data.isTeamATurn();
    }

    public void setTeamATurn(boolean playerATurn) {
        data.setTeamATurn(playerATurn);
    }

    public boolean isAmITeamA() {
        return data.isAmITeamA();
    }

    public void setAmITeamA(boolean amITeamA) {
        data.setAmITeamA(amITeamA);
    }

}
