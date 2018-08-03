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


    //temp vars;
    private ArrayList<Position> invalidPositions;
    private ArrayList<Position> firedPositionsTemp;

    private Ship selectedShip;
    private Position onDownPosition = null;
    private Position onMovePosition= null;
    private Position onUpPosition = null;
    private Position lasValidPosition = null;
    private Position initialPositionShip = null; //used when we hitted in 3 positions... isMayChangeShipPosition



    private boolean mayChangeShipPosition;

    public NavalBattleGame() {
        this.data = new Data();
        invalidPositions = new ArrayList<>();
        this.firedPositionsTemp = new ArrayList<>();
        this.selectedShip = null;
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

        int hittedFiredPositions = 0;

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

                        position.setColor(Constants.BLACK_CROSS_SQUARE);
                        position.setDestroyed(true);

                        hittedFiredPositions++;

//                        if(isTeamATurn())
//                            Log.d("verifyFiredPosition","TeamA hited in position: " + position);
//                        else
//                            Log.d("verifyFiredPosition","TeamB hited in position: " + position);

                        break;
                    }
                    else
                    {
                        position.setColor(Constants.CROSS_SQUARE);

                    }
                }
            }
        }
        if(hittedFiredPositions==3)
            mayChangeShipPosition=true;
    }

    public boolean verifyEndOfGame() {

        Team atualTeam = getAtualTeam();

        Team opositeTeam = getOpositeTeam();

        Log.d("verifyEndOfGame","Positions that atual team fired: " + atualTeam.getFiredPositions().toString());



        //for all oposite team...
        //percorre a equipa contrária
        for(Ship ship : opositeTeam.getShips())
        {
            //for each ship verify if do not exits any position which that atual team doesnt yet fired.
            //para cada barco vai verificar se existe alguma posicao em que a equipa atual ainda n tenha disparado
            for(Position position : ship.getPositionList())
            {
                if(!atualTeam.getFiredPositions().contains(position))
                {
//                    Log.d("verifyEndOfGame","Found not fired position in another team. Position: " + position);
                    return false;
                }
            }
        }

//        since atual team fired all positions of another team the the game is ended
//        visto que a equipa atual disparou em todos os barcos contrarios esta nahou
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

    public boolean isMyTurnToPlay() {
        //if the atual player is temA and is time to teamA play or if the atual player is temB and is time to teamB play return true
        return ((isTeamATurn() && isAmITeamA()) || (!isTeamATurn() && !isAmITeamA()));
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

    public boolean isMayChangeShipPosition() {
        return mayChangeShipPosition;
    }

    public void setMayChangeShipPosition(boolean mayChangeShipPosition) {
        this.mayChangeShipPosition = mayChangeShipPosition;
    }

//    -
//    -
//    -
//    clicked positions
//    -
//    -
//    -
//    -
    public void onDown(Position onDownPosition) {

        this.onDownPosition = onDownPosition;


        //if the game already started and we may NOT change position in a ship...
        if(isStarted() && !isMayChangeShipPosition())
        {

        }
        else
        {
            selectedShip = getShip(onDownPosition);

            Log.d("onDown", "\n---" + onDownPosition.toString());

            if(selectedShip!=null)
            {
                Log.d("onDown", selectedShip.toString());

                initialPositionShip = selectedShip.getPointPosition();
                Log.d("onDown", "initialPositionShip:" + initialPositionShip);

            }
        }

    }

    public void onMove(Position onMovePosition) {
        this.onMovePosition = onMovePosition;

        //if the game already started and we may NOT change position in a ship...
        if(isStarted() && !isMayChangeShipPosition())
        {

        }
        else
        {
            if(selectedShip!= null)
            {
                selectedShip.setPointPosition(onMovePosition);

                //verifica se está dentro do tabuleiro... se nao estiver volta a definir a ship na ultima posicao guardada que era valida...
                //se a nova posicao for valida armazenar como sendo a ultima posicao valida efetuada
                if(isInsideView(selectedShip))
                    lasValidPosition = onMovePosition;
                else
                    selectedShip.setPointPosition(lasValidPosition);

                refreshInvalidPositions(selectedShip);

            }

        }
    }

    public void onUp(Position onUpPosition) {
        this.onUpPosition = onUpPosition;

        //if the game already started and we may NOT change position in a ship...
        if(isStarted() && !isMayChangeShipPosition())
        {

            //if is avaiable next turn we cant click more...
            if(!isAvaibleNextTurn())
                addFiredPosition(onUpPosition);

            verifyFiredPosition();
        }
        else //when selecting position of the ships...
        {
            if(selectedShip!=null)
            {

                verifyRotate();


                selectedShip.setPointPosition(onUpPosition);

//              TODO: understand this again... and comment
                //verifica se está dentro do tabuleiro... se nao estiver volta a definir a ship na ultima posicao guardada que era valida...
                //se a nova posicao for valida armazenar como sendo a ultima posicao valida efetuada
                if(isInsideView(selectedShip))
                    lasValidPosition = onUpPosition;
                else
                    selectedShip.setPointPosition(lasValidPosition);

                refreshInvalidPositions(selectedShip);

//                if current team may change a ship position...
                if(isMayChangeShipPosition())
                {
                    //tried to change position but... if it was invalid put the ship in last position which it was
                    if(verifyIsValidPositionChangeShipPosition())
                        setMayChangeShipPosition(false);
                    else
                    {
                        Log.d("onUp", "tried to change position but exists an invalid Positions");

                        selectedShip.setPointPosition(initialPositionShip);
                        refreshInvalidPositions(selectedShip);
                    }
                }
                selectedShip = null;
            }
        }
    }

    private boolean verifyIsValidPositionChangeShipPosition() {

        //tried to change position but exists at least an invalid Position... so put the ship in last position which it was
        if(invalidPositions.size()>0)
            return false;

        //if inst adjancet return false
        if(!initialPositionShip.isAdjacent(onUpPosition))
            return false;

        return true;
    }

    private void verifyRotate() {
        //if down, move and up same position so this is a rotate
        if(onDownPosition.equals(onUpPosition))
        {
            //if is to change ship position after 3 hitted fired positions return... dont allow rotate
            if(isMayChangeShipPosition())
                return;

            selectedShip.rotate();
            //rotate while doesn't find valid rotation
            while(true)
            {
                selectedShip.setPointPosition(onUpPosition);
                if(!isInsideView(selectedShip))
                    selectedShip.rotate();
                else
                    break;
            }
        }
    }
}
